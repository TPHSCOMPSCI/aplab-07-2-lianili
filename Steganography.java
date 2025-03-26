import java.awt.*;
import java.util.ArrayList;

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

        // Picture flower1 = new Picture("flower1.jpg");
        // Picture flower2 = new Picture("flower2.jpg");
        // if (canHide(flower1, flower2)){
        //     Picture modifiedflower1 = hidePicture(flower1, flower2);
        //     modifiedflower1.explore();
        //     Picture revealModification = revealPicture(modifiedflower1);
        //     revealModification.explore();
        // }

        // Picture beach = new Picture("beach.jpg");
        // Picture robot = new Picture("robot.jpg");
        // Picture flower1 = new Picture("flower1.jpg");
        // beach.explore();
        // // these lines hide 2 pictures
        // Picture hidden1 = hidePicture(beach, robot, 65, 208);
        // Picture hidden2 = hidePicture(hidden1, flower1, 280, 110);
        // hidden2.explore();
        // Picture unhidden = revealPicture(hidden2);
        // unhidden.explore();

        // Picture swan = new Picture("swan.jpg");
        // Picture swan2 = new Picture("swan.jpg");
        // System.out.println("Swan and swan2 are the same: " +
        // isSame(swan, swan2));
        // swan = testClearLow(swan);
        // System.out.println("Swan and swan2 are the same (after clearLow run on swan): "
        // + isSame(swan, swan2));

        Picture arch = new Picture("arch.jpg");
        Picture arch2 = new Picture("arch.jpg");
        Picture koala = new Picture("koala.jpg") ;
        Picture robot1 = new Picture("robot.jpg");
        ArrayList<Point> pointList = findDifferences(arch, arch2);
        System.out.println("PointList after comparing two identical pictures " +
        "has a size of " + pointList.size());
        pointList = findDifferences(arch, koala);
        System.out.println("PointList after comparing two different sized pictures " +
        "has a size of " + pointList.size());
        Picture modifiedArch = hidePicture(arch, robot1, 65, 102);
        pointList = findDifferences(arch, modifiedArch);
        System.out.println("Pointlist after hiding a picture has a size of"
        + pointList.size());
        arch.show();
        arch2.show(); 
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
        clearLow(p);
        int newRed = p.getRed()+ (int)(c.getRed() / 64);
        int newGreen = p.getGreen() + (int)(c.getGreen() / 64);
        int newBlue = p.getBlue() + (int)(c.getBlue() / 64);
        p.setRed(newRed);
        p.setGreen(newGreen);
        p.setBlue(newBlue);
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
                // setLow(pix, col); This method would only take the two highest bits from col. We want the two lowest bits from col.
                // Then set the two highest bits in pix to col's two lowest bits.
                // How to isolate the two lowest bits of col?
                // You do modulus
                int choppedRed = col.getRed() % 4;
                int choppedBlue = col.getBlue() % 4;
                int choppedGreen = col.getGreen() % 4;
                pix.setRed(choppedRed * 64);
                pix.setBlue(choppedBlue * 64);
                pix.setGreen(choppedGreen * 64);
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

    /**
     * Creates new Picture with a smaller picture hidden in a bigger picture at the specified row and column
     * @param source
     * @param secret
     * @param row
     * @param column
     * @return
     */
    public static Picture hidePicture(Picture source, Picture secret, int row, int column){
        Picture modifiedPic = new Picture(source);
        Pixel[][] modifiedPixels = modifiedPic.getPixels2D();
        Pixel[][] secretPixels = secret.getPixels2D();
        for(int r = 0; r < secretPixels.length; r++){
            for(int c = 0; c < secretPixels[r].length; c++){
                Pixel modifiedPixel = modifiedPixels[r + row][c + column];
                Color secretColor = secretPixels[r][c].getColor();
                setLow(modifiedPixel, secretColor);
            }
        }
        return modifiedPic;
    }

    /**
     * Returns if the size and colors of the picture1 and picture2 are the same
     * @param picture1
     * @param picture2
     * @return
     */
    public static boolean isSame(Picture picture1, Picture picture2){
        boolean same = false;

        if (!canHide(picture1, picture2)){
            same = false;
        } else {
            Pixel[][] pixels1 = picture1.getPixels2D();
            Pixel[][] pixels2 = picture2.getPixels2D();
            boolean sameColor = true;
            for(int r = 0; r < pixels1.length; r++){
                for(int c = 0; c < pixels2[r].length; c++){
                    if(!pixels1[r][c].getColor().equals(pixels2[r][c].getColor())){
                        sameColor = false;
                    }
                }
            }

            if (sameColor){
                same = true;
            }
        }
        
        return same;
    }

    public static ArrayList<Point> findDifferences(Picture picture1, Picture picture2){
        ArrayList<Point> differences = new ArrayList<Point>();
        if(!canHide(picture1, picture2)){
            return differences;
        }

        Pixel[][] pixels1 = picture1.getPixels2D();
        Pixel[][] pixels2 = picture2.getPixels2D();
        for(int r = 0; r < pixels1.length; r++){
            for(int c = 0; c < pixels1[r].length; c++){
                if(!pixels1[r][c].getColor().equals(pixels2[r][c].getColor())){
                    differences.add(new Point(r,c));
                }
            }
        }
        return differences;
    }

    public static Picture showDifferentArea(Picture picture1, Picture picture2){
        
    }
}
