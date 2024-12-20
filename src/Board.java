package logic;

import java.util.Random;
import java.util.Stack;
 
public class Board {
  public static final int NUM_ROWS = 15;
  public static final int NUM_COLUMNS = 20;
  public static final int NUM_MINES = NUM_ROWS * NUM_COLUMNS / 5;
  private Stack<boolean[][]> undoList;
  private Square[][] square;
 
  public Board() {
    undoList = new Stack<>();
    square = new Square[NUM_ROWS][NUM_COLUMNS];
    for (int i = 0; i < square.length; i++) {
      for (int j = 0; j < square[0].length; j++) {
        square[i][j] = new Square();
      }
    }
 
    // Place mines in random cells
    for (int i = 0; i < NUM_MINES; i++) {
      int x = genRan(NUM_ROWS);
      int y = genRan(NUM_COLUMNS);
      while (square[x][y].isHasMine()) {
        x = genRan(NUM_ROWS);
        y = genRan(NUM_COLUMNS);
      }
      square[x][y].setHasMine(true);
    }
 
    // Record the number of nearby mines in the squares
    for (int i = 0; i < square.length; i++) {
      for (int j = 0; j < square[0].length; j++) {
        int count = 0;
        for (int m = -1; m <= 1; m++) {
          if (i + m < 0) { m++; }
          if (i + m > NUM_ROWS - 1) { break; }
          for (int n = -1; n <= 1; n++) {
            if (j + n < 0) { n++; }
            if (j + n > NUM_COLUMNS - 1) { break; }
            if (!(m == 0 && n == 0) && square[i + m][j + n].isHasMine()) {
              count++;
            }
          }
        }
        square[i][j].setNumMineAround(count);
      }
    }
  }
 
  private int genRan(int range) {
    Random rd = new Random();
    return rd.nextInt(range);
  }
 
  public Square[][] getListSquare() {
    return square;
  }
 
  public boolean play(int x, int y) {
    if (!square[x][y].isOpen()) {
      square[x][y].setOpen(true);
      if (square[x][y].isHasMine()) {
        return false;
      }
      if (square[x][y].getNumMineAround() == 0) {
        for (int m = -1; m <= 1; m++) {
          if (x + m < 0) { m++; }
          if (x + m > NUM_ROWS - 1) { break; }
          for (int n = -1; n <= 1; n++) {
            if (y + n < 0) { n++; }
            if (y + n > NUM_COLUMNS - 1) { break; }
            play(x + m, y + n);
          }
        }
      }
    }
    return true;
  }
 
  public void target(int x, int y) {
    if (!square[x][y].isOpen()) { //not open
      if (!square[x][y].isTarget()) { //and has not put flag
        square[x][y].setTarget(true); //put flag
      } else {
        square[x][y].setTarget(false); //remove flag
      }
    }
  }
 
  public void showAllSquares() {
    for (int i = 0; i < square.length; i++) {
      for (int j = 0; j < square[0].length; j++) {
        square[i][j].setOpen(true);
      }
    }
  }
  
  public void saveToStatusStack(){
      boolean[][] squaresStats = new boolean[NUM_ROWS][NUM_COLUMNS];
      for (int i = 0; i < square.length; i++){
          for (int j = 0; j < square[0].length; j++){
              squaresStats[i][j]=square[i][j].isOpen();
          }
      }
      undoList.push(squaresStats);
  }
  
  public Stack<boolean[][]> getUndoList() {
        return undoList;
    }
}