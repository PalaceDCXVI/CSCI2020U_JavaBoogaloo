package sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ImageLoader
{
    private static HashMap<String, ImageView > m_Images = new HashMap<>();

    public static void CreateImage(String name, String directory) throws FileNotFoundException
    {
        //Creating an image
        Image image = null;
        ImageView image_viewer = null;

        try
        {
            image = new Image(new FileInputStream(directory));
            image_viewer = new ImageView(image);

        }
        catch(FileNotFoundException e)
        {
            System.err.println("Could not load image: " + directory);
            e.printStackTrace();
            return;
        }

        m_Images.put(name, image_viewer);
    }

    public static ImageView GetImage(String name)
    {
        return m_Images.get(name);
    }
}
