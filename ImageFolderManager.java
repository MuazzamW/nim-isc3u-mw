import java.awt.Dimension;

import javax.swing.ImageIcon;


public final class ImageFolderManager {
    //this class manages all the images in the image folder
    //it is a class that cannot be insantiated, instead it just returns the absolute path of any image in the image folder
    //it returns the absolute path to any class that calls it
    
    public static String IMAGE_FOLDER_PATH;
    private static boolean folderPathSet = false;

    private ImageFolderManager(){
        //private constructor so that the class is not instantiated
    }

    private static void setFolderPath(){
        //set the image folder path based on the OS of the computer
        IMAGE_FOLDER_PATH = FolderPathsManager.IMAGES_FOLDER_PATH;
    }

    public static ImageIcon getImageIcon(String relativePath){
        //returns the imageIcon requested
        if(!folderPathSet){
            setFolderPath();
        }
        System.out.println(IMAGE_FOLDER_PATH+relativePath);
        return new ImageIcon(IMAGE_FOLDER_PATH+relativePath);
    }

    public static Dimension getImageDimensions(String relativePath){
        if(!folderPathSet){
            setFolderPath();
        }
        //returns the dimensions of the image icon requested
        ImageIcon image = new ImageIcon(IMAGE_FOLDER_PATH+relativePath);
        System.out.println(image.getIconWidth()+" "+image.getIconHeight());
        return new Dimension(image.getIconWidth(), image.getIconHeight());
    }

}
