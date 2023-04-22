import java.awt.Color; // the color type used in StdDraw
import java.awt.Font; // the font type used in StdDraw
import java.awt.event.KeyEvent; // for the key codes used in StdDraw ###tuş aksiyonları
import java.util.Random;


// The main class to run the Tetris 2048 game
public class Tetris2048 {
   public static void main(String[] args) throws CloneNotSupportedException {
   
      // set the size of the game grid
      int gridH = 20, gridW = 16;
      // set the size of the drawing canvas
      int canvasH = 40 * gridH, canvasW = 40 * gridW;
      StdDraw.setCanvasSize(canvasW, canvasH);
      // set the scale of the coordinate system
      StdDraw.setXscale(-0.5, gridW - 0.5);
      StdDraw.setYscale(-0.5, gridH - 0.5);
      // double buffering enables computer animations, creating an illusion of
      // motion by repeating four steps: clear, draw, show and pause
      StdDraw.enableDoubleBuffering();

      // set the dimension values stored and used in the Tetromino class
      Tetromino.gridHeight = gridH;
      Tetromino.gridWidth = gridW;

      // create the game grid
      GameGrid grid = new GameGrid(gridH, gridW);
      // create the first tetromino to enter the game grid
      // by using the createTetromino method defined below


      
      // display a simple menu before opening the game
      // by using the displayGameMenu method defined below
      displayGameMenu(gridH, gridW);
      gameLoop(grid);
      System.out.println("Game Over");
   }


   public static void gameLoop(GameGrid grid) throws CloneNotSupportedException{
      Tetromino currentTetromino = createTetromino();
      grid.setCurrentTetromino(currentTetromino);
      grid.latest_tetromino = currentTetromino;
      Tetromino nextTetromino = createTetromino();
      grid.setNextTetromino(nextTetromino);
      grid.ShowNextTetromino();

      // the main game loop (using some keyboard keys for moving the tetromino)
      // -----------------------------------------------------------------------
      int iterationCount = 0; // used for the speed of the game
      while (true) {

         // check user interactions via the keyboard
         // --------------------------------------------------------------------
         // if the left arrow key is being pressed
         if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
            // move the active tetromino left by one
            currentTetromino.move("left", grid);
         // if the right arrow key is being pressed
         else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
            // move the active tetromino right by one
            currentTetromino.move("right", grid);
         // if the down arrow key is being pressed
         else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))
            // move the active tetromino down by one
            currentTetromino.move("down", grid);
         else if (StdDraw.isKeyPressed(KeyEvent.VK_Z)) {
        	 if(currentTetromino.canBeRotated(grid))
         		currentTetromino.rotate_ct();     }    
         else if (StdDraw.isKeyPressed(KeyEvent.VK_X))
        	 if(currentTetromino.canBeRotated(grid))
             	currentTetromino.rotate();


         

         // move the active tetromino down by 1 once in 10 iterations (auto fall)
         boolean success = true;
         if (iterationCount % 10 == 0)
            success = currentTetromino.move("down", grid);
         iterationCount++;

         // place the active tetromino on the grid when it cannot go down anymore
         if (!success) {
            // get the tile matrix of the tetromino without empty rows and columns
            currentTetromino.createMinBoundedTileMatrix();
            Tile[][] tiles = currentTetromino.getMinBoundedTileMatrix();
            Point pos = currentTetromino.getMinBoundedTileMatrixPosition();
            // update the game grid by locking the tiles of the landed tetromino
            boolean gameOver = grid.updateGrid(tiles, pos);
            // end the main game loop if the game is over
            if (gameOver)
               break;
            // create the next tetromino to enter the game grid
            // by using the createTetromino function defined below

            currentTetromino = nextTetromino;
            grid.setCurrentTetromino(currentTetromino);
            nextTetromino = createTetromino();
            grid.setNextTetromino(nextTetromino);
            
         }

         // check whether there is a full line in the game grid.
         int fullLine = grid.checkFullLine();

         // update the game grid by clearing the full line
         if (fullLine != -1) {
            grid.update_points_after_clear(fullLine);
            grid.clearFullLine(fullLine);
         }

         // display the game grid and the current tetromino
         grid.merge_all();
         grid.fall();
         grid.display();

      }

   }
      // A method for displaying a simple menu before starting the game**********
      public static void PauseMenu(int gridHeight, int gridWidth) throws CloneNotSupportedException {
         // colors used for the menu
         GameGrid grid = new GameGrid(gridHeight, gridWidth);
         Color backgroundColor = new Color(	0, 74, 173);
         // clear the background canvas to background_color
         StdDraw.clear(backgroundColor);
         // the relative path of the image file
         String imgFile = "images/logooo.png";
         String imgFile2 = "images/resumeButton.png";
         String imgFile3 = "images/restartButton.png";

         // center coordinates to display the image
         double imgCenterX = (gridWidth - 1) / 2.0, imgCenterY = gridHeight - 8;
         // display the image
         StdDraw.picture(imgCenterX+0.45, imgCenterY, imgFile);
         // the width and the height of the start game button

         double buttonW2 = 6.5, buttonH2 = 2;
         // the center point coordinates of the start game button
         double buttonX2 = imgCenterX-4, buttonY2 = 5;


         StdDraw.picture(buttonX2+0.8, buttonY2, imgFile2);

         double buttonW3 = 6.5, buttonH3 = 2;
         // the center point coordinates of the start game button
         double buttonX3 = imgCenterX+4, buttonY3 = 5;

         StdDraw.picture(buttonX3, buttonY3, imgFile3);

   
         // menu interaction loop
         while (true) {
            // display the menu and wait for a short time (50 ms)
            StdDraw.show();
            StdDraw.pause(50);
            // check if the mouse is being pressed on the button
            if (StdDraw.isMousePressed()) {
               // get the x and y coordinates of the position of the mouse
               double mouseX = StdDraw.mouseX(), mouseY = StdDraw.mouseY();
               // break the loop to end the method and start the game
               if (mouseX > buttonX2 - buttonW2 / 2 && mouseX < buttonX2 + buttonW2 / 2)
                  if (mouseY > buttonY2 - buttonH2 / 2 && mouseY < buttonY2 + buttonH2 / 2)
                     return;// break the loop to end the method and start the game
               if (mouseX > buttonX3 - buttonW3 / 2 && mouseX < buttonX3 + buttonW3 / 2)
                  if (mouseY > buttonY3 - buttonH3 / 2 && mouseY < buttonY3 + buttonH3 / 2)
                     gameLoop(grid);; // break the loop to end the method and start the game
   
            }
         }
   
   
      }


   // A method for displaying a simple menu before starting the game**********
   public static void displayGameMenu(int gridHeight, int gridWidth) throws CloneNotSupportedException {
      // colors used for the menu
      GameGrid grid = new GameGrid(gridHeight, gridWidth);
      Color backgroundColor = new Color(	0, 74, 173);

      // clear the background canvas to background_color
      StdDraw.clear(backgroundColor);
      // the relative path of the image file
      String imgFile = "images/logooo.png";
      String imgFile2 = "images/startButton.png";
      // center coordinates to display the image
      double imgCenterX = (gridWidth - 1) / 2.0, imgCenterY = gridHeight - 8;
      // display the image
      StdDraw.picture(imgCenterX+0.45, imgCenterY, imgFile);
      // the width and the height of the start game button
      double buttonW = gridWidth - 6, buttonH = 2;
      // the center point coordinates of the start game button
      double buttonX = imgCenterX, buttonY = 5;
      // display the start game button as a filled rectangle
      StdDraw.picture(buttonX+0.2, buttonY, imgFile2);


      // menu interaction loop
      while (true) {
         // display the menu and wait for a short time (50 ms)
         StdDraw.show();
         StdDraw.pause(50);
         // check if the mouse is being pressed on the button
         if (StdDraw.isMousePressed()) {
            // get the x and y coordinates of the position of the mouse
            double mouseX = StdDraw.mouseX(), mouseY = StdDraw.mouseY();
            // check if these coordinates are inside the button
            if (mouseX > buttonX - buttonW / 2 && mouseX < buttonX + buttonW / 2)
               if (mouseY > buttonY - buttonH / 2 && mouseY < buttonY + buttonH / 2)
                  break; 

         }
      }


   }



}
