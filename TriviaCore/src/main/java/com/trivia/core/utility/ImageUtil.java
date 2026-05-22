package com.trivia.core.utility;

import com.trivia.core.exception.InvalidInputException;
import com.trivia.core.exception.SystemException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

// TODO: This has to be refactored, pay attention to performance.

public final class ImageUtil extends FileUtil {
    public final static Path IMAGE_DIR = Paths.get(SERVER_DIR + "/data/images");
    public static String IMAGE_FILE_PREFIX = "img_";
    public static int IMAGE_MAX_SIZE = 2048 * 1024 * 5;
    private static String IMAGE_FORMAT = "png";
    private static int IMAGE_WIDTH = 720;
    private static int IMAGE_HEIGHT = 480;

    private ImageUtil() {}

    public static InputStream getPreviewImage(InputStream originalImageStream) throws IOException {
        BufferedImage bufferedImage = getAndValidateImage(originalImageStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, IMAGE_FORMAT, byteArrayOutputStream);
        InputStream previewImageStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return previewImageStream;
    }

    public static String saveImage(InputStream inputStream) {
        try {
            // Do the full check again just to be sure.
            BufferedImage bufferedImage = getAndValidateImage(inputStream);

            Path filePath = Files.createTempFile(IMAGE_DIR, IMAGE_FILE_PREFIX, "." + IMAGE_FORMAT);
            ImageIO.write(bufferedImage, IMAGE_FORMAT, filePath.toFile());

            String fullPath = filePath.getFileName().toString();
            String imagePath = fullPath.substring(fullPath.lastIndexOf('/') + 1, fullPath.length());

            return imagePath;
        }
        catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static byte[] getImage(String imagePath) throws IOException {
        return Files.readAllBytes(Paths.get(ImageUtil.IMAGE_DIR + "/" + imagePath));
    }

    private static BufferedImage getAndValidateImage(InputStream imageStream) throws IOException {
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageStream)) {
            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);
            ImageReader imageReader = imageReaders.next();

            try {
                imageReader.setInput(imageInputStream, true, true);

                Dimension imageSize = new Dimension(imageReader.getWidth(0), imageReader.getHeight(0));

                validateImageFormat(imageReader.getFormatName());
                validateImageSize(imageSize);

                BufferedImage bufferedImage = imageReader.read(0);
                bufferedImage = getResizedImage(bufferedImage);

                return bufferedImage;
            }
            finally {
                imageReader.dispose();
            }
        }
    }

    private static void validateImageFormat(String format) {
        if (!Arrays.asList(ImageIO.getReaderFileSuffixes()).contains(format.toLowerCase())) {
            throw new InvalidInputException(String.format("The %s image format is not supported.", format));
        }
    }

    private static void validateImageSize(Dimension dimension) {
        if (dimension.getWidth() < IMAGE_HEIGHT || dimension.getWidth() < IMAGE_WIDTH) {
            throw new InvalidInputException(String.format("Image resolution needs to be at least %sx%s.", IMAGE_WIDTH, IMAGE_HEIGHT));
        }
    }

    private static BufferedImage getResizedImage(BufferedImage originalImage) {
        int resizedHeight = (int) (originalImage.getHeight() / ((double) originalImage.getWidth() / IMAGE_WIDTH));
        BufferedImage resizedImage = new BufferedImage(IMAGE_WIDTH, resizedHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(originalImage, 0, 0, IMAGE_WIDTH, resizedHeight, null);
        g2d.dispose();

        return resizedImage;
    }

    public static void deleteImage(String imagePath) {
        File file = new File(IMAGE_DIR + "/" + imagePath);
        if (!file.exists()) {
            throw new SystemException("File not found.");
        }
        else if (!file.delete()) {
            throw new SystemException("File was not deleted successfully.");
        }
    }

    // I would love to know how to circumvent these kinds of useless checks used to keep the integrity of our entities.
    // One possible way could be to disable the entity setImage setter altogether?
    // TODO: This doesn't belong here. Maybe put in entity or better yet, a new service such as ImageService?
    public static void validateImagePath(String oldPath, String newPath) {
        // No image path at all so we don't care either way.
        if (newPath == null && oldPath == null) {
            return;
        }

        // Some manual changes are not allowed:
        // Image path has been added manually.
        else if (newPath != null && oldPath == null ||
            // New image path is not equal but is not null either (would mark it for delete).
            newPath != null && !newPath.equals(oldPath)) {
            throw new InvalidInputException("Manual image path change is not allowed! Pass the image as an argument to create or set null to delete.");
        }
    }

    public static Date getDateCreated(String imagePath) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(
                Paths.get(IMAGE_DIR + "/" + imagePath), BasicFileAttributes.class);
            return new Date(attributes.creationTime().toMillis());
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }
}