import java.awt.Color; // the color type used in StdDraw
import java.awt.Font; // the font type used in StdDraw
import java.awt.event.KeyEvent; // for the key codes used in StdDraw ###tuş aksiyonları
import java.util.Random;
import javax.xml.stream.events.StartDocument;
import java.util.concurrent.TimeUnit;

// A class used for modelling the game grid
public class GameGrid implements Cloneable{
   // data fields
   private int gridHeight, gridWidth; // the size of the game grid
   private char [] type = { 'I', 'O', 'Z','L','J','S','T' };
   private Tile[][] tileMatrix; // to store the tiles locked on the game grid
   // the tetromino that is currently being moved on the game grid
   private Tetromino currentTetromino = null;
   public Tetromino latest_tetromino = null;
   private Tetromino nextTetromino = null;
   // the gameOver flag shows whether the game is over or not
   private boolean gameOver = false;
   private Color emptyCellColor; // the color used for the empty grid cells
   private Color lineColor; // the color used for the grid lines
   private Color boundaryColor; // the color used for the grid boundaries
   private double lineThickness; // the thickness used for the grid lines
   private double boxThickness; // the thickness used for the grid boundaries
   public Tile[][] tileUpdate;
   public boolean isMerged = true;
   private int points = 0;
   boolean gamePaused = false;
   //GameGrid grid = new GameGrid(gridHeight, gridWidth);


   

   // A constructor for creating the game grid based on the given parameters
   public GameGrid(int gridH, int gridW) {
      // set the size of the game grid as the given values for the parameters
      gridHeight = gridH;
      gridWidth = gridW;
      // create the tile matrix to store the tiles locked on the game grid
      tileMatrix = new Tile[gridHeight][gridWidth];
      // set the color used for the empty grid cells
      emptyCellColor = new Color(42, 69, 99);
      // set the colors used for the grid lines and the grid boundaries
      lineColor = new Color(0, 100, 200);
      boundaryColor = new Color(0, 100, 200);
      // set the thickness values used for the grid lines and the grid boundaries
      lineThickness = 0.002;
      boxThickness = 10 * lineThickness;


   }

   // A setter method for the currentTetromino data field

   public void setCurrentTetromino(Tetromino currentTetromino) {
      this.currentTetromino = currentTetromino;
  
   }
   public void setNextTetromino(Tetromino nextTetromino){
      this.nextTetromino = nextTetromino;
   }
   public void setNewTetromino(Tetromino newTetromino){

   }

   
   public void merge_all() {
	   
	   boolean keep = true;
	      
	   while(keep) {
		   keep = false;
		   for(int i = 0 ; i < gridWidth ; i++) {  // 13
			   for(int j = 1 ; j < gridHeight ; j++) { //17
				   if(tileMatrix[j][i] == null || tileMatrix[j-1][i] == null) {
					   continue;
				   }
				   else if(tileMatrix[j-1][i].getNumber() == tileMatrix[j][i].getNumber()) {
					   this.points+=tileMatrix[j-1][i].getNumber()*2;
					   tileMatrix[j-1][i].updateTile(tileMatrix[j-1][i].getNumber()*2);
					   tileMatrix[j][i] = null;
					   this.fix_column(i, j);
					   keep = true;
				   }
			   }
		   }
		   
	   }
	   
   }  
   
   public void fix_column(int column , int row) {
	   
	   int x = latest_tetromino.tileMatrix.length;
	   
	   boolean keep = true;
	   while(keep) {
		   keep = false;
		   for(int i = row ; i < row+x ; i++) {
			   if(tileMatrix[i][column] == null && tileMatrix[i-1][column] == null) {
				   continue;
			   }
			   else if(tileMatrix[i-1][column] == null && tileMatrix[i][column] != null) {
				   tileMatrix[i-1][column] = tileMatrix[i][column];
				   tileMatrix[i][column] = null;
				   keep = true;
			   }
		   }
	   }
	   
   }
   
   public void fall() {
	  
	   
	   boolean keep = true;
	   
	   while(keep) {
		   keep = false;
		   for(int column = 0 ; column < gridWidth ; column++) {
			   if(column == 0 || column == gridWidth-1)
				   continue;
			   for(int row = 0 ; row < gridHeight ; row++) {
				   
				   if(row == gridHeight-1 || row == 0 )
					   continue;
				   
				   if   (     tileMatrix[row][column] != null
						   && tileMatrix[row+1][column-1] == null 
						   && tileMatrix[row][column-1] == null 
						   && tileMatrix[row-1][column-1] == null 
						   && tileMatrix[row+1][column] == null 
						   && tileMatrix[row+1][column+1] == null
						   && tileMatrix[row][column+1] == null 
						   && tileMatrix[row-1][column+1] == null 
						   && tileMatrix[row-1][column]== null) {
					   this.points += tileMatrix[row][column].getNumber();
					   tileMatrix[row][column] = null;
					   keep = true;
				   }
			   }	   
		   }
	   }
   }
   
   public void update_points_after_clear(int line) {
	      
	   for(int i = 0 ; i < gridWidth ; i++) {		   
		   this.points += tileMatrix[line][i].getNumber();	   
	   }	   
   }
   
   public void fix_fall(int row,int column) {
	      
	   for(int i = row ; i > 0 ; i--) {
	   		if(tileMatrix[i-1][column] == null) {
	   			tileMatrix[i-1][column] = tileMatrix[i][column];
	   			tileMatrix[i][column] = null;
	   			}
	   		}
	   }
   
      public int get_points() {
	   return points;
   }

   // A method used for displaying the game grid
   public void display() throws CloneNotSupportedException {
      // clear the background to emptyCellColor
      StdDraw.clear(emptyCellColor);
      // draw the game grid
      drawGrid();
      // draw the current/active tetromino if it is not null (the case when the
      // game grid is updated)
      if (currentTetromino != null)      
         currentTetromino.draw();
      // draw a box around the game grid


      drawBoundaries();
      ShowNextTetromino();
      checkFullLine();
      clearFullLine(gridWidth);
      PauseGame();
      

   

      // show the resulting drawing with a pause duration = 50 ms
      StdDraw.show();

      StdDraw.pause(50);

   }

   // A method for drawing the cells and the lines of the game grid
   public void drawGrid() {
      // for each cell of the game grid
      for (int row = 0; row < gridHeight; row++)
         for (int col = 0; col < gridWidth; col++)
            // draw the tile if the grid cell is occupied by a tile
            if (tileMatrix[row][col] != null)
               tileMatrix[row][col].draw(new Point(col, row));
      // draw the inner lines of the grid
      StdDraw.setPenColor(lineColor);
      StdDraw.setPenRadius(lineThickness);
      // x and y ranges for the game grid
      double startX = -0.5, endX = gridWidth - 5;
      double startY = -0.5, endY = gridHeight - 0.5;
      for (double x = startX + 1; x < endX; x++) // vertical inner lines
         StdDraw.line(x, startY, x, endY);
      for (double y = startY + 1; y < endY; y++) // horizontal inner lines
         StdDraw.line(startX, y, endX-0.5, y);
      StdDraw.setPenRadius(); // reset the pen radius to its default value
   }

   // A method for drawing the boundaries around the game grid
   public void drawBoundaries() {
      // draw a bounding box around the game grid as a rectangle
      StdDraw.setPenColor(boundaryColor); // using boundaryColor
      // set the pen radius as boxThickness (half of this thickness is visible
      // for the bounding box as its lines lie on the boundaries of the canvas)
      StdDraw.setPenRadius(boxThickness);
      // the center point coordinates for the game grid
      double centerX = (gridWidth / 2 - 0.5), centerY = gridHeight / 2 - 0.5;
      StdDraw.rectangle(centerX, centerY, gridWidth / 2, gridHeight / 2);
      StdDraw.setPenRadius(); // reset the pen radius to its default value
   }

   // A method for checking whether the grid cell with given row and column
   // indexes is occupied by a tile or empty
   public boolean isOccupied(int row, double d) {
      // considering newly entered tetrominoes to the game grid that may have
      // tiles out of the game grid (above the topmost grid row)
      if (!isInside(row, d))
         return false;
      // the cell is occupied by a tile if it is not null
      return tileMatrix[row][(int) d] != null;
   }

   // A method for checking whether the cell with given row and column indexes
   // is inside the game grid or not
   public boolean isInside(int row, double d) {
      if (row < 0 || row >= gridHeight)
         return false;
      if (d < 0 || d >= gridWidth)
         return false;
      return true;
   }

   // A method that locks the tiles of the landed tetromino on the game grid while
   // checking if the game is over due to having tiles above the topmost grid row.
   // The method returns true when the game is over and false otherwise.
   public boolean updateGrid(Tile[][] tilesToLock, Point blcPosition) {
      // necessary for the display method to stop displaying the tetromino
      currentTetromino = null;
      // lock the tiles of the current tetromino (tilesToLock) on the game grid
      int nRows = tilesToLock.length, nCols = tilesToLock[0].length;
      for (int col = 0; col < nCols; col++) {
         for (int row = 0; row < nRows; row++) {
            // place each tile onto the game grid
            if (tilesToLock[row][col] != null) {
               // compute the position of the tile on the game grid
               Point pos = new Point();
               pos.setX(blcPosition.getX() + col);
               pos.setY(blcPosition.getY() + (nRows - 1) - row);
               if (isInside(pos.getY(), pos.getX()))
                  tileMatrix[pos.getY()][(int) pos.getX()] = tilesToLock[row][col];
               // the game is over if any placed tile is above the game grid
               else
                  gameOver = true;
            }
         }
      }
      // return the value of the gameOver flag
      return gameOver;
   }

   public void ShowNextTetromino() throws CloneNotSupportedException {
      Font font = new Font("Georgia", Font.BOLD, 40);
      StdDraw.setFont(font);
      //StdDraw.setPenColor(248, 184, 184);
      StdDraw.text(13, 10, "NEXT");
      Tetromino copyNext = this.nextTetromino.clone();

      if(copyNext.getType() == 'I'){
         double width = this.gridWidth-3;
         copyNext.bottomLeftCell.setX(width);    
         int height = this.gridHeight-15;  
         copyNext.bottomLeftCell.setY(height);    
      }
      else if(copyNext.getType() == 'O' ){
         double width =this.gridWidth- 3.5;
         copyNext.bottomLeftCell.setX(width);
         int height = this.gridHeight-15;  
         copyNext.bottomLeftCell.setY(height);
      }
      else if(copyNext.getType() == 'J' || copyNext.getType() == 'L' ){
         double width =this.gridWidth- 4.5;
         copyNext.bottomLeftCell.setX(width);
         int height = this.gridHeight-15;  
         copyNext.bottomLeftCell.setY(height);
      }

      else if(copyNext.getType() == 'S' ){
         double width =this.gridWidth- 4;
         copyNext.bottomLeftCell.setX(width);
         int height = this.gridHeight-16;  
         copyNext.bottomLeftCell.setY(height);
               
      }
      else if(copyNext.getType() == 'T' ){
         double width =this.gridWidth- 4;
         copyNext.bottomLeftCell.setX(width);
         int height = this.gridHeight-16;  
         copyNext.bottomLeftCell.setY(height);
      }
               
      else{
         double width = this.gridWidth-4;
         copyNext.bottomLeftCell.setX(width);
         int height = this.gridHeight-15;  
         copyNext.bottomLeftCell.setY(height);        

      }

      copyNext.draw();

      }

   public void PauseGame() throws CloneNotSupportedException{
      double buttonW = 3;
      double buttonH = 0.9;
      double buttonX = gridWidth-3;
      double buttonY = gridHeight-18;
      Color buttonColor = new Color(0, 100, 200);
      Color textColor = new Color(255,255,255);
      StdDraw.setPenColor(buttonColor);
      StdDraw.filledRectangle(buttonX, buttonY, buttonW/2, buttonH/2);
      StdDraw.setPenRadius();
      Font font = new Font("Arial", Font.BOLD, 15);
      StdDraw.setFont(font);
      StdDraw.setPenColor(textColor);
      String text = "PAUSE";
      StdDraw.text(buttonX,buttonY,text);
      if (StdDraw.isMousePressed()) {
          double mouseX = StdDraw.mouseX(), mouseY = StdDraw.mouseY();
          if (mouseX > buttonX - buttonW / 2 && mouseX < buttonX + buttonW / 2)
            if (mouseY > buttonY - buttonH / 2 && mouseY < buttonY + buttonH / 2)
               Tetris2048.PauseMenu(gridHeight, gridWidth);
      }
   

   }


 
   public void clearFullLine(int row) {
      while (true) {
         boolean isLineAboveEmpty = true;
         for (int i = 0; i < gridWidth; i++) {
            tileMatrix[row][i] = tileMatrix[row+1][i];
            if (tileMatrix[row+1][i] != null) {
               isLineAboveEmpty = false;
            }
         }
         if (isLineAboveEmpty == true) break;
         row++;
         }
      }


   
   public int checkFullLine() {
      int row = -1;
      for (int i = 0; i < gridHeight; i++) {
         boolean isFilled = true;
         for (int j = 0; j < gridWidth; j++) {
            if (tileMatrix[i][j] == null) {
               isFilled = false;
            }
         }
         if (isFilled) {
            row = i;
            break;
         }
      }
      return row;
   }
   
}


   
   
   
   



   
  
  
  
  
  
  
  
      

   






