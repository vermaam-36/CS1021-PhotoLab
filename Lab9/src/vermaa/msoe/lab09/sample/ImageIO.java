package vermaa.msoe.lab09.sample;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.*;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ImageIO {
    private static Pattern hex = Pattern.compile("^#([0-9A-Fa-f]{6})$");

    /**
     * Takes a path, returns an image at that path if it exists
     *
     * @param path takes in path to read image from
     * @return Image to be set in Image View
     * @throws FileNotFoundException if unable to find Image file
     */
    public static Image readMSOE(Path path) throws FileNotFoundException {
        File file = path.toFile();
        Scanner in = new Scanner(file);
        if (!in.next().toUpperCase().equals("MSOE")) {
            throw new IllegalArgumentException();
        }
        int width = Integer.parseInt(in.next());
        int height = Integer.parseInt(in.next());
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                pixelWriter.setColor(x, y, stringToColor(in.next()));
            }
        }
        return image;


    }

    /**
     * Takes a path, returns an image at that path if it exists
     *
     * @param path takes in path to read image from
     * @return Image to be set in Image View
     */
    public static Image readBMSOE(Path path) {
        WritableImage image = null;
        try(  DataInputStream inputStream= new DataInputStream(new BufferedInputStream(new FileInputStream(path.toFile())));) {
            byte[] firstFive = new byte[5];
            inputStream.read(firstFive);
            for (byte b :
                    firstFive) {
                System.out.println(b);

            }
            int width = (int) inputStream.readInt();
            int height = (int) inputStream.readInt();
            image = new WritableImage(width, height);
            PixelWriter pixelWriter = image.getPixelWriter();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixelWriter.setColor(x, y, intToColor(inputStream.readInt()));
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println("file not found");
        }
        catch (IOException e){
            System.out.println("IO Exception in bmsoe");
        }
        return image;
    }
    /**
     * Writes BMSOE files
     *
     * @param path  path to be written to
     * @param image Image being converted
     * @throws IOException if anything goes wrong
     */
    public static void writeBMSOE(Path path, Image image) throws IOException {
        DataOutputStream outputStream= new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path.toFile())));
        outputStream.writeBytes("BMSOE");
        int width = (int) image.getWidth();
        int height= (int) image.getHeight();
        outputStream.writeInt(width);
        outputStream.writeInt(height);
        PixelReader pixelReader= image.getPixelReader();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int output= colorToInt(pixelReader.getColor(x, y));
                outputStream.writeInt(output);
            }
        }
        outputStream.close();
    }

    /**
     * Takes a color as a string then turns it into a Color object
     *
     * @param hexString input string
     * @return Color out
     * @throws InputMismatchException if string is in invalid format, throw exception
     */
    private static Color stringToColor(String hexString) throws InputMismatchException {
        if (hex.matcher(hexString).matches()) {
            return Color.web(hexString);
        } else {
            throw new InputMismatchException();
        }
    }

    /**
     * Reads in path and reads image at that path
     *
     * @param path location in memory where Image should be
     * @return the image as an object
     */
    public static Image read(Path path) {
        Image returnImage = null;
        if (path.toString().endsWith(".msoe")) {
            try {
                returnImage = ImageIO.readMSOE(path);
            } catch (FileNotFoundException e) {
                System.out.println("File not Found");
            } catch (IllegalArgumentException e) {
                System.out.println("no MSOE, Illegal Argument");
            }
        }
        else if(path.toString().endsWith(".bmsoe")){
            returnImage= ImageIO.readBMSOE(path);

        }
        else {
            try {
                returnImage = ImageUtil.readImage(path);
            } catch (IOException e) {
                System.out.println("IO Exception try again");
            }
        }
        return returnImage;
    }

    /**
     * Saves an image file to designated location
     *
     * @param path  Place to be saved
     * @param image Image to be saved
     */
    public static void write(Path path, Image image) {
        if (path.endsWith("png") || path.endsWith("jpg")) {
            try {
                ImageUtil.writeImage(path, image);

            } catch (IOException e) {
                System.out.println("File not found");
            } catch (IllegalArgumentException e) {
                System.out.println("invalid argument");
            }
        } else if (path.toString().toUpperCase().endsWith(".MSOE")) {
            try {
                ImageIO.writeMSOE(path, image);
            } catch (IOException e) {
                System.out.println("Io Exception");
            }
        }
        else if (path.toString().toUpperCase().endsWith(".BMSOE")) {
            try {
                ImageIO.writeBMSOE(path, image);
            } catch (IOException e) {
                System.out.println("Io Exception");
            }
        }
    }

    /**
     * Writes MSOE files
     *
     * @param path  path to be written to
     * @param image Image being converted
     * @throws IOException if anything goes wrong
     */
    public static void writeMSOE(Path path, Image image) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString(), false));
        writer.write("MSOE");
        writer.newLine();
        writer.write("" + (int) image.getWidth() + " " + (int) image.getHeight());
        writer.newLine();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = image.getPixelReader().getColor(x, y);
                int redColor = (int) (color.getRed() * 255);
                int greenColor = (int) (color.getGreen() * 255);
                int blueColor = (int) (color.getBlue() * 255);
                String string = String.format("#%02X%02x%02x", redColor, greenColor, blueColor);
                string = string + " ";
                writer.write(string);
            }
            writer.newLine();
        }
        writer.close();
    }
    private static Color intToColor(int color) {
        double red = ((color >> 16) & 0x000000FF)/255.0;
        double green = ((color >> 8) & 0x000000FF)/255.0;
        double blue = (color & 0x000000FF)/255.0;
        double alpha = ((color >> 24) & 0x000000FF)/255.0;
        return new Color(red, green, blue, alpha);
    }
    private static int colorToInt(Color color) {
        int red = ((int)(color.getRed()*255)) & 0x000000FF;
        int green = ((int)(color.getGreen()*255)) & 0x000000FF;
        int blue = ((int)(color.getBlue()*255)) & 0x000000FF;
        int alpha = ((int)(color.getOpacity()*255)) & 0x000000FF;
        return (alpha << 24) + (red << 16) + (green << 8) + blue;
    }
}
