package vermaa.msoe.lab09.sample;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.awt.image.Kernel;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;

public final class ImageUtil {
    public ImageUtil() {

    }

    public static void writeImage(Path path, Image image) throws IOException {
        BufferedImage bufferedImag = SwingFXUtils.fromFXImage(image, (BufferedImage) null);
        String extension = getExtension(path);
        String[] validExtensions = new String[]{"gif", "jpg", "png", "tiff"};
        if (Arrays.binarySearch(validExtensions, extension) < 0) {
            throw new IllegalArgumentException("File extension: ." + extension + " does not specify a supported image format.");
        } else {
            OutputStream out = Files.newOutputStream(path);
            Throwable var6 = null;

            try {
                if (extension.equals("jpg")) {
                    BufferedImage convertedImage = new BufferedImage(bufferedImag.getWidth(), bufferedImag.getHeight(), 5);
                    convertedImage.getGraphics().drawImage(bufferedImag, 0, 0, (ImageObserver) null);
                    convertedImage.getGraphics().dispose();
                    bufferedImag = convertedImage;
                }

                ImageIO.write(bufferedImag, extension, out);
            } catch (Throwable var15) {
                var6 = var15;
                throw var15;
            } finally {
                if (out != null) {
                    if (var6 != null) {
                        try {
                            out.close();
                        } catch (Throwable var14) {
                            var6.addSuppressed(var14);
                        }
                    } else {
                        out.close();
                    }
                }

            }

        }
    }

    public static Image readImage(Path path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        } else {
            InputStream in = Files.newInputStream(path);
            Throwable var2 = null;

            WritableImage var3;
            try {
                var3 = SwingFXUtils.toFXImage(ImageIO.read(in), (WritableImage) null);
            } catch (Throwable var12) {
                var2 = var12;
                throw var12;
            } finally {
                if (in != null) {
                    if (var2 != null) {
                        try {
                            in.close();
                        } catch (Throwable var11) {
                            var2.addSuppressed(var11);
                        }
                    } else {
                        in.close();
                    }
                }

            }

            return var3;
        }
    }

    public static Image convolve(Image image, double[] kernel) {
        int[] validKernelSizes = new int[]{1, 4, 9, 16, 25, 36};
        if (Arrays.binarySearch(validKernelSizes, kernel.length) < 0) {
            throw new IllegalArgumentException("The kernel array must be a power of two no greater than 36");
        } else {
            float[] kernelFloats = new float[kernel.length];

            int kernelSize;
            for (kernelSize = 0; kernelSize < kernel.length; ++kernelSize) {
                kernelFloats[kernelSize] = (float) kernel[kernelSize];
            }

            kernelSize = (int) Math.sqrt((double) kernel.length);
            BufferedImageOp op = new ConvolveOp(new Kernel(kernelSize, kernelSize, kernelFloats));
            BufferedImage result = op.filter(SwingFXUtils.fromFXImage(image, (BufferedImage) null), (BufferedImage) null);
            return SwingFXUtils.toFXImage(result, (WritableImage) null);
        }
    }

    private static String getExtension(Path path) {
        String filename = path.toString();
        int dotIndex = filename.lastIndexOf(46);
        if (dotIndex != -1 && dotIndex != filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        } else {
            throw new IllegalArgumentException("No file extension for " + path);
        }
    }
}