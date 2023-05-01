import java.util.Scanner;
import java.lang.Math;
import java.util.Random;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LineDrawing extends JPanel {

    private BufferedImage canvas;
    private int lineNum;

    // Took a lot of the graphic set up using Buffered image, Jframe and Jpanel from Utyf
    // Set up the stage/canvas for our pixel line drawing algorithms.
    public LineDrawing(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        fillCanvas(Color.WHITE);
    }

    // Return the number of lines, n, input by the user.
    public int getLineNum() {
        return lineNum;
    }

    // Setter method for instance var line num
    public void setLineNum(int n) {
        this.lineNum =  n;
    }

    // Return new dimension object width and height
    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    // Graphics component 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
    }

    // Fill Canvas - Set color of the stage to draw the lines on 
    public void fillCanvas(Color c) {
        // Chose to fill the background with grey
        int color = c.getRGB();
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                // Set each pixel to grey
                canvas.setRGB(x, y, color);
            }
        }
        repaint();
    }

    // Generalized algorithm for drawing a basic line
    public void basicLine (int x0, int y0, int x1, int y1) {
        int dx, dy, x, y, xInc;
        float slope, yInc;
        // Math abs to avoid negative slope
        dx = Math.abs(x1 - x0);
        dy =  Math.abs(y1 - y0);
         // If dx >= dy, slope <= 1
         // This also accounts for a slope of 0.
         // Assumptions / special case
            if (dx >= dy) { 
                // Starting from the left-most coordinate.
                // If x0 is less than x1, we will start from x0.
                if (x0 <= x1) { 
                    x = x0;
                    y = y0;
                    // else we will start from x1 which is the leftmost coordinate.
                } else { 
                    x = x1;
                    y = y1;
                    x1 = x0;
                    y1 = y0;
                }
                // Calculate the slope.
                slope = (float) dy / (float) dx; 
                for (int i = 0; i <= (dx - 1); i++)
                {
                    xInc = x + i;
                    if (y > y1)
                        yInc = -slope * i + y;
                    else
                        yInc = slope * i + y;
                        // Set pixel color to black
                    canvas.setRGB(xInc, (int) Math.round(yInc - 0.5),Color.BLACK.getRGB());  
                }
            }
            // Else, the slope is > 1.
            else { 
                // Set the left lowest coordinate to the starting point
                if (y0 <= y1) { 
                    x = x0;
                    y = y0;
                // Start from the right and end at left end point
                } else { 
                    x = x1;
                    y = y1;
                    x1 = x0;
                    y1 = y0;
                }
                // Calculate slope
                slope = (float) dx / (float) dy; 
                for (int i = 0; i <= (dy - 1); i++)
                {
                    yInc = y + i;
                    if (x > x1)
                        xInc = (int) - slope * i + x; 
                    else
                        xInc = (int) slope * i + x;
                        // Set pixel color to black
                        canvas.setRGB((int) Math.round(xInc - 0.5), (int)(yInc), Color.BLACK.getRGB()); 
                }
            }
        } // Basic Line Drawing Algorithm

    // Bresenham's line drawing algorithm
    public void bresenhamLine (int x0, int y0, int x1, int y1) {
    int dx = Math.abs(x1 - x0);
	int dy = Math.abs(y1 - y0);
	int x, y;
    //slope <= 1.0;
    if (dx >= dy) {
        // 2 * dy (shifted 1 bit) - dx  
        int e = (dy << 1) - dx;
        // 2 * dy
        int inc1 = dy << 1;
        // 2 * dy - dx
        int inc2 = (dy - dx) << 1
        ;	
        // Set the left point to the starting point
        if (x0 < x1) { 
            x = x0;
            y = y0;
        }   
// Swap right most coordinate to starting point and left coordinate to the end point
        else { 
            x = x1;			
            y = y1;
            x1 = x0;
            y1 = y0;
        }
        // Set color of pixel to black
        canvas.setRGB(x, y,Color.BLACK.getRGB());
        while (x < x1) {
            if (e < 0)
                e += inc1;
            else {
                if (y < y1) {
                    y++;
                    e += inc2;
                } else {
                    y--;
                    e += inc2;
                }
            }
            x++;
            canvas.setRGB(x, y,Color.BLACK.getRGB());
        } // while
    } // if
    // Else our slope is greater than 1
    else { 
        int e = (dx << 1) - dy;
        int inc1 = dx << 1;
        int inc2 = (dx - dy) << 1;
        // Set the left most coordinate to be the starting point
        if (y0 < y1) { 
            x = x0;
            y = y0;
        }
        // Set the rightmost coordinate to be the starting point and 
        // the leftmost coordinate to be the end point.
        else { 
            x = x1;			
            y = y1;
            y1 = y0;
            x1 = x0;
        }
        // Set the color of the pixel to black.
        canvas.setRGB(x, y,Color.BLACK.getRGB());
        while (y < y1) {
            if (e < 0)
                e += inc1;
            else {
                if (x > x1){
                    x--;
                    e += inc2;
                } else {
                    x++;
                    e += inc2;
                }
            }
            y++;
            // Set the color of the pixel to black.
            canvas.setRGB(x, y,Color.BLACK.getRGB());
        } // while
    } // else
    } // Bresenham's line drawing algorithm
    
    // Takes user input and calls one of the line drawing algorithms
    public void drawLines() {
    //User input for # of lines and algorithm to use...
    Scanner keyboard = new Scanner(System.in);
    System.out.println("Please enter the number of lines you want drawn.");
    int n = keyboard.nextInt();
    System.out.println("Please enter 0 for Basic line drawing or 1 for Bresenham's line drawing.");
    int algo = keyboard.nextInt();

    int x0, y0, x1, y1;
    // upper bounds for generating random numbers / coordinates
    int widthUpper = 1000;
    int heightUpper = 800;
    Random randomNum = new Random();

    // Start the timer
    long start = System.currentTimeMillis();
    for (int i = 0; i < n; i++)
    {
        // x and y coordinates determined by a random number generator
        x0 = randomNum.nextInt(widthUpper);
        y0 = randomNum.nextInt(heightUpper);
        x1 = randomNum.nextInt(widthUpper);
        y1 = randomNum.nextInt(heightUpper);
        // print out the x and y coordinates of each lines
        //Testing
        //System.out.println("Line"  + (i+1) + ":(" + x0 + "," + y0 + ")" + "(" + x1 + "," + y1 + ")");
        if (algo == 0) {
            // Call the basic line drawing algorithm
            basicLine(x0, y0, x1, y1); 
    } else if (algo == 1) {
        // Call the Bresenham line drawing algorithm
        bresenhamLine(x0, y0, x1, y1); 
    } else {
        // Basic user input protection
    System.out.println("Invalid input, program exiting...");
    System.exit(0);
    }
        } // for
        // End the timer
        long finish = System.currentTimeMillis();
        long duration = (finish - start);
       System.out.println("Line drawing algorithm finished in " + duration + " milliseconds.");
       keyboard.close();
    }

    public static void main(String Args[]) {
    // Canvas width x height
    int width = 1000;
    int height = 800;
     // Set up a canvas to draw lines on. 
    JFrame frame = new JFrame("Line drawing algorithm, Assignment 1-2");
    LineDrawing panel = new LineDrawing(width, height);
    // Single Function Call to draw the lines.
    panel.drawLines();
    // Jframe setup    
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}