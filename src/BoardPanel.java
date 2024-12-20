

package gui.panel;
 
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import java.util.Stack;
import gui.ICommon;
import gui.ITrans;
import logic.Board;
import logic.Square;
 
public class BoardPanel extends JPanel implements ICommon {
  private static final long serialVersionUID = -6403941308246651773L;
  private Label[][] lbSquare;
  private ITrans listener;
  private int numSquareClosed;
 
  public BoardPanel() {
    initComp();
    addComp();
    addEvent();
  }
 
  @Override
  public void initComp() {
    setLayout(new GridLayout(Board.NUM_ROWS, Board.NUM_COLUMNS));
  }
 
  @Override
  public void addComp() {
    Border border = BorderFactory.createLineBorder(Color.gray, 1);
    lbSquare = new Label[Board.NUM_ROWS][Board.NUM_COLUMNS];
    for (int i = 0; i < lbSquare.length; i++) {
      for (int j = 0; j < lbSquare[0].length; j++) {
        lbSquare[i][j] = new Label();
        lbSquare[i][j].setOpaque(true);
        lbSquare[i][j].setBackground(new Color(242, 242, 242));
        lbSquare[i][j].setBorder(border);
        lbSquare[i][j].setHorizontalAlignment(JLabel.CENTER);
        lbSquare[i][j].setVerticalAlignment(JLabel.CENTER);
        add(lbSquare[i][j]);
      }
    }
  }
 
  @Override
  public void addEvent() {
    for (int i = 0; i < lbSquare.length; i++) {
      for (int j = 0; j < lbSquare[0].length; j++) {
        lbSquare[i][j].x = i;
        lbSquare[i][j].y = j;
        lbSquare[i][j].addMouseListener(new MouseAdapter() {
          @Override
          public void mouseReleased(MouseEvent e) {
            Label label = (Label) e.getComponent();
            if (e.getButton() == MouseEvent.BUTTON1) {
                if(!listener.getListSquare()[label.x][label.y].isOpen()){
                    listener.saveToStatusStack();
                }
              listener.play(label.x, label.y);
            } else if (e.getButton() == MouseEvent.BUTTON3) {
              listener.target(label.x, label.y);
            }
          }
        });
      }
    }
  }
 
  public void addListener(ITrans event) {
    listener = event;
  }
 
  // display update
  public void updateBoard() {
    Font font = new Font("VNI", Font.PLAIN, 20);
    numSquareClosed = 0;
    Square[][] listSquare = listener.getListSquare();
    for (int i = 0; i < listSquare.length; i++) {
      for (int j = 0; j < listSquare[0].length; j++) {
        lbSquare[i][j].setFont(font);
        if (!listSquare[i][j].isOpen()) {
          lbSquare[i][j].setBackground(new Color(242, 242, 242));
          lbSquare[i][j].setForeground(Color.black);
          numSquareClosed++;
          if (!listSquare[i][j].isTarget()) {
            lbSquare[i][j].setText("");
          } else {
            lbSquare[i][j].setText("\uD83D\uDEA9"); // character 'flag'
          }
        } else {
          if (listSquare[i][j].isHasMine()) {
            lbSquare[i][j].setText("\uD83D\uDCA3"); // character 'bomb'
          } else {
            int numMineAround = listSquare[i][j].getNumMineAround();
            if (numMineAround == 0) {
              lbSquare[i][j].setText("");
            } else {
              lbSquare[i][j].setText(numMineAround + "");
              // Set colour
              switch (numMineAround) {
              case 1:
                lbSquare[i][j].setForeground(new Color(128, 128, 128));
                break;
              case 2:
                lbSquare[i][j].setForeground(new Color(255, 0, 0));
                break;
              case 3:
                lbSquare[i][j].setForeground(new Color(0, 204, 0));
                break;
              case 4:
                lbSquare[i][j].setForeground(new Color(102, 0, 255));
                break;
              case 5:
                lbSquare[i][j].setForeground(new Color(128, 128, 128));
                break;
              case 6:
                lbSquare[i][j].setForeground(new Color(255, 0, 0));
                break;
              case 7:
                lbSquare[i][j].setForeground(new Color(0, 204, 0));
                break;
              case 8:
                lbSquare[i][j].setForeground(new Color(102, 0, 255));
                break;
              }
            }
          }
          lbSquare[i][j].setBackground(Color.white);
        }
      }
    }
  }
 
  private class Label extends JLabel {
    private static final long serialVersionUID = 6099893043079770073L;
    private int x;
    private int y;
  }
   
  public int getNumSquareClosed() {
    return numSquareClosed;
  }
  
  public void undo() {
    Stack<boolean[][]> undoList = listener.getBoard().getUndoList();
    if (!undoList.isEmpty()) {
        boolean[][] previousState = undoList.pop();
        Square[][] listSquare = listener.getListSquare();
        for (int i = 0; i < listSquare.length; i++) {
            for (int j = 0; j < listSquare[0].length; j++) {
                listSquare[i][j].setOpen(previousState[i][j]);
            }
        }
        updateBoard();
    }
}

}