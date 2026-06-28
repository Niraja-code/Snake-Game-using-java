

import java.util.LinkedList;

class Snake {

    private LinkedList<Cell> snakePartList
        = new LinkedList<>();
    private Cell head;

    public Snake(Cell initPos)
    {
        head = initPos;
        snakePartList.add(head);
        head.setCellType(CellType.SNAKE_NODE);
    }

  
public void move(Cell nextCell, boolean grow)
{
    System.out.println("Snake is moving to "
                       + nextCell.getRow() + " "
                       + nextCell.getCol());

    if (!grow) {
        Cell tail = snakePartList.removeLast();
        tail.setCellType(CellType.EMPTY);
    }

    head = nextCell;
    head.setCellType(CellType.SNAKE_NODE);
    snakePartList.addFirst(head);
}

  public boolean checkCrash(Cell nextCell)
{
    System.out.println("Going to check for Crash");

    for (Cell cell : snakePartList) {

        // ignore tail (because it may move away)
        if (cell == snakePartList.getLast())
            continue;

        if (cell.getRow() == nextCell.getRow() &&
            cell.getCol() == nextCell.getCol()) {
            return true;
        }
    }
    return false;
}

    public LinkedList<Cell> getSnakePartList()
    {
        return snakePartList;
    }

    public void
    setSnakePartList(LinkedList<Cell> snakePartList)
    {
        this.snakePartList = snakePartList;
    }

    public Cell getHead() { return head; }

    public void setHead(Cell head) { this.head = head; }
}


class Cell {

    private final int row, col;
    private CellType cellType;

    public Cell(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public CellType getCellType() { return cellType; }

    public void setCellType(CellType cellType)
    {
        this.cellType = cellType;
    }

    public int getRow() { return row; }

    public int getCol() { return col; }
}
// Enum for different cell types

 enum CellType {

    EMPTY,
    FOOD,
    SNAKE_NODE;
}

 class Board {

    
    final int ROW_COUNT, COL_COUNT;
    private Cell[][] cells;

    public Board(int rowCount, int columnCount)
    {
        ROW_COUNT = rowCount;
        COL_COUNT = columnCount;

        cells = new Cell[ROW_COUNT][COL_COUNT];
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int column = 0; column < COL_COUNT;
                 column++) {
                cells[row][column] = new Cell(row, column);
            }
        }
    }

    public Cell[][] getCells() { return cells; }

    public void setCells(Cell[][] cells)
    {
        this.cells = cells;
    }

    public void generateFood()
    {
        System.out.println("Going to generate food");
        int row = 0, column = 0;
        while (true) {
            row = (int)(Math.random() * ROW_COUNT);
            column = (int)(Math.random() * COL_COUNT);
            if (cells[row][column].getCellType()
                != CellType.SNAKE_NODE)
                break;
        }
        cells[row][column].setCellType(CellType.FOOD);
        System.out.println("Food is generated at: " + row
                           + " " + column);
    }
}

// To represent Snake Game
public class Game {

    public static final int DIRECTION_NONE
        = 0,
        DIRECTION_RIGHT = 1, DIRECTION_LEFT = -1,
        DIRECTION_UP = 2, DIRECTION_DOWN = -2;
    private Snake snake;
    private Board board;
    private int direction;
    private boolean gameOver;

    public Game(Snake snake, Board board)
    {
        this.snake = snake;
        this.board = board;
    }

    public Snake getSnake() { return snake; }

    public void setSnake(Snake snake)
    {
        this.snake = snake;
    }

    public Board getBoard() { return board; }

    public void setBoard(Board board)
    {
        this.board = board;
    }

    public boolean isGameOver() { return gameOver; }

    public void setGameOver(boolean gameOver)
    {
        this.gameOver = gameOver;
    }

    public int getDirection() { return direction; }

    public void setDirection(int direction)
    {
        this.direction = direction;
    }

    // We need to update the game at regular intervals,
    // and accept user input from the Keyboard.
  public void update()
{
    if (gameOver || direction == DIRECTION_NONE)
        return;

    System.out.println("Going to update the game");
    

 Cell nextCell = getNextCell(snake.getHead());

if (nextCell == null) {
    gameOver = true;
    return;
}

if (snake.checkCrash(nextCell)) {
    gameOver = true;
    setDirection(DIRECTION_NONE);
    return;
}
    boolean ateFood = (nextCell.getCellType() == CellType.FOOD);

    snake.move(nextCell, ateFood);

    if (ateFood) {
        board.generateFood();
    }
}

  private Cell getNextCell(Cell currentPosition)
{
    System.out.println("Going to find next cell");

    int row = currentPosition.getRow();
    int col = currentPosition.getCol();

    if (direction == DIRECTION_RIGHT) col++;
    else if (direction == DIRECTION_LEFT) col--;
    else if (direction == DIRECTION_UP) row--;
    else if (direction == DIRECTION_DOWN) row++;

 if (row < 0 || row >= board.ROW_COUNT ||
    col < 0 || col >= board.COL_COUNT) {
    gameOver = true;
    return null;
}

    return board.getCells()[row][col];
}

  public static void main(String[] args) {

    System.out.println("Going to start game");

    Board board = new Board(10, 10);
    Cell initPos = board.getCells()[0][0];
    Snake initSnake = new Snake(initPos);
    Game newGame = new Game(initSnake, board);

    newGame.setGameOver(false);
    newGame.setDirection(Game.DIRECTION_RIGHT);

    for (int i = 0; i < 5; i++) {
        if (i == 2)
            newGame.getBoard().generateFood();

        newGame.update();

        if (i == 3)
            newGame.setDirection(Game.DIRECTION_RIGHT);

        if (newGame.isGameOver())
            break;
    }
}
}