import java.awt.*;

public class Steganography {
    public static void main(String[] args){
        // Picture beach = new Picture ("beach.jpg");
        // beach.explore();
        // Picture copy = testClearLow(beach);
        // copy.explore();

        // Picture beach2 = new Picture("beach.jpg");
        // beach2.explore();
        // Picture copy2 = testSetLow(beach2, Color.PINK);
        // copy2.explore(); 
        // Picture copy3 = revealPicture(copy2);
        // copy3.explore();
        
        Picture flower1 = new Picture("flower1.jpg");
        // flower1.explore();
        Picture flower2 = new Picture("flower2.jpg");
        Picture modifiedflower1 = hidePicture(flower1, flower2);
        modifiedflower1.explore();
        Picture revealModification = revealPicture(modifiedflower1);
        revealModification.explore();
        //40, 35. G decreases by 3, proof hide pic works.
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
        Picture p = new Picture(pic);
        for (Pixel[] row : p.getPixels2D()){
            for(Pixel pixel : row){
                clearLow(pixel);
            }
        }
        return p;
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
     * Every pixel in a Picture steals the top two bits from a color  
     */
    public static Picture testSetLow(Picture pic, Color c){
        Picture p = new Picture(pic);
        for(Pixel pix : p.getPixels()){
            setLow(pix, c);
        }
        return p;
    }

    /**
     * Sets the highest two bits of each pixel’s colors
     * to the lowest two bits of each pixel’s color o s
    */
    public static Picture revealPicture(Picture hidden){
        Picture copy = new Picture(hidden);
        Pixel[][] pixels = copy.getPixels2D();
        Pixel[][] source = hidden.getPixels2D();
        for (int r = 0; r < pixels.length; r++){
            for (int c = 0; c < pixels[0].length; c++){
                Color col = source[r][c].getColor();
                Pixel pix = pixels[r][c];
                pix.setColor(Color.BLACK);
                setLow(pix, col);
                pix.setRed(pix.getRed() * 64);
                pix.setBlue(pix.getBlue() * 64);
                pix.setGreen(pix.getGreen() * 64);
            }
        }
        return copy;
    }

    /**
     * Determines whether secret can be hidden in source, which is
     * true if source and secret are the same dimensions.
     * @param source is not null
     * @param secret is not null
     * @return true if secret can be hidden in source, false otherwise.
     */
    public static boolean canHide(Picture source, Picture secret){
        boolean sameSize = false;
        if(source.getWidth() == secret.getWidth() && source.getHeight() == secret.getHeight()){
            sameSize = true;
        }
        return sameSize;
    }

    /**
     * Creates a new Picture with data from secret hidden in data from source
     * @param source is not null
     * @param secret is not null
     * @return combined Picture with secret hidden in source
     * precondition: source is same width and height as secret
     */
    public static Picture hidePicture(Picture source, Picture secret){
        Picture modifiedPic = new Picture(source);
        Pixel[][] modifiedPixels = modifiedPic.getPixels2D();
        Pixel[][] secretPixels = secret.getPixels2D();
        for(int r = 0; r < secretPixels.length; r++){
            for(int c = 0; c < secretPixels[r].length; c++){
                Pixel modifiedPixel = modifiedPixels[r][c];
                Color secretColor = secretPixels[r][c].getColor();
                setLow(modifiedPixel, secretColor);
            }
        }
        return modifiedPic;
    }
}
