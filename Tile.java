import java.awt.Color; // the color type used in StdDraw
import java.awt.Font; // the font type used in StdDraw
import java.util.HashMap;
import java.util.Random;

// A class used for modeling numbered tiles as in 2048
public class Tile {

   // Data fields: instance variables
   // --------------------------------------------------------------------------
   private int number; // the number on the tile
   private Color backgroundColor; // background (tile) color
   private Color foregroundColor; // foreground (number) color
   private Color boxColor; // box (boundary) color
   private HashMap<Integer, Color> tile_colors = new HashMap<>();


   // Data fields: class variables
   // --------------------------------------------------------------------------
   // the value of the boundary thickness (for the boundaries around the tiles)
   private static double boundaryThickness = 0.004;
   // the font used for displaying the tile number
   private static Font font = new Font("Arial", Font.PLAIN, 14);


   // Methods
   // --------------------------------------------------------------------------
   // the default constructor that creates a tile with 2 as the number on it
   public Tile() {
      // fill the color map
      tile_colors.put(2, new Color(243, 240, 202, 255));
      tile_colors.put(4, new Color(238, 195, 120));
      tile_colors.put(8, new Color(241, 167, 116));
      tile_colors.put(16, new Color(229, 117, 28));
      tile_colors.put(32, new Color(239, 85, 59));
      tile_colors.put(64,  new Color(187, 58, 38));
      tile_colors.put(128, new Color(140, 52, 52));
      tile_colors.put(256, new Color(210, 31, 85));
      tile_colors.put(512, new Color(112, 12, 55));
      tile_colors.put(1024, new Color(154, 20, 114));
      tile_colors.put(2048, new Color(101, 11, 91));

      // set the number on the tile
      int[] numberList = {2, 4};
      Random random = new Random();
      int randomTileNumber = random.nextInt(numberList.length);
      number = numberList[randomTileNumber];
      // set the colors of the tile
      backgroundColor = tile_colors.get(number);
      foregroundColor = new Color(0,0,0);
      boxColor = new Color(16, 21, 21);
   }
   public int getNumber() {
      return number;
   }

   public void updateTile(int number) {
      this.number = number;
      this.backgroundColor = tile_colors.get(number);
   }

   // a method for drawing the tile
   public void draw(Point position, double... sideLength) {
      // the default value for the side length (sLength) is 1
      double sLength;
      if (sideLength.length == 0) // sideLength is a variable-length parameter
         sLength = 1;
      else
         sLength = sideLength[0];
      // draw the tile as a filled square
      StdDraw.setPenColor(backgroundColor);
      StdDraw.filledSquare(position.getX(), position.getY(), sLength / 2);
      // draw the bounding box around the tile as a square
      StdDraw.setPenColor(boxColor);
      StdDraw.setPenRadius(boundaryThickness);
      StdDraw.square(position.getX(), position.getY(), sLength / 2);
      StdDraw.setPenRadius(); // reset the pen radius to its default value
      // draw the number on the tile
      StdDraw.setPenColor(foregroundColor);
      StdDraw.setFont(font);
      StdDraw.text(position.getX(), position.getY(), "" + number);
   }
}
