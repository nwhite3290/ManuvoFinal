
/*
 * AssetManager.java
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * AssetManager class
 * - a utility class to load and provide access to image assets
 * - all game images (backgrounds, buttons, tiles, etc.) are loaded and stored in a static map for reuse
 *  - centralizes asset loading for maintainability
 */
public class AssetManager {
    // Static cache of loaded images by key
    private static Map<String, BufferedImage> images = new HashMap<>();

    /**
     * loadImage(String key, String path)
     * - Loads an image from disk and stores it with the given key
     * - does nothing if image is already loaded0
     * @param key  unique identifier for the image
     * @param path filesystem path to the image file
     */
    public static void loadImage(String key, String path) {
        if (images.containsKey(key)) {
            return; // already loaded
        }
        try {
            BufferedImage img = ImageIO.read(new File(path));
            images.put(key, img);
        } catch (IOException e) {
            //e.printStackTrace();
			System.out.println(e.getMessage());
            System.out.println("File not found: " + key);
            System.out.println("AssetManager/loadImage(String key, String path)");
        }
    }

    /**
     * getImage(String key)
     * - retrieves a loaded image by key
     * @param key identifier of the image
     * @return BufferedImage if found, otherwise null
     */
    public static BufferedImage getImage(String key) {
        return images.get(key);
    }
}

