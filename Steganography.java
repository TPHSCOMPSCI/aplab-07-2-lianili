import java.awt.*;

public class Steganography {
    public static void main(String[] args){
        Picture beach = new Picture("beach.jpg");
        beach.explore();
        Picture copy = testClearLow(beach);
        copy.explore(); 
    }

    /**
    * Clear the lower (rightmost) two bits in a pixel.
    */
    public static void clearLow( Pixel p ){
        int newRed = (int)(p.getRed() / 4) * 4;
        int newGreen = (int)(p.getGreen() / 4) * 4;
        int newBlue = (int)(p.getBlue() / 4) * 4;
        p.setRed(newRed);
        p.setGreen(newGreen);
        p.setBlue(newBlue);
    }

    /*
     * Clear lowest two bits in every pixel in a Picture
     */
    public static Picture testClearLow( Picture pic){
        for (Pixel[] row : pic.getPixels2D()){
            for(Pixel p : row){
                clearLow(p);
            }
        }
        return pic;
    }

    /**
    * Set the lower 2 bits in a pixel to the highest 2 bits in c
    */
    public static void setLow(Pixel p, Color c){
        int choppedRed = (int)(c.getRed() / 64);
        int choppedGreen = (int)(c.getGreen() / 64);
        int choppedBlue = (int)(c.getBlue() / 64);
        clearLow(p);
        p.setRed(p.getRed() + choppedRed);
        p.setGreen(p.getGreen() + choppedGreen);
        p.setBlue(p.getBlue() + choppedBlue);
    }

    /*
     * Set every pixel in a colro
     */
}
