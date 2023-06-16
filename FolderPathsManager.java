public class FolderPathsManager {
    //this class is the first class called
    //it sets the universal paths for all the folders in the project based on the operating system of the device
    
    public static String GAMELOG_FOLDER_PATH = System.getProperty("user.dir");
    public static String IMAGES_FOLDER_PATH = System.getProperty("user.dir");

    public FolderPathsManager(String OS){

        if(OS.equals("Windows 10")){
            GAMELOG_FOLDER_PATH+="\\Game_Log_Folder\\";
            IMAGES_FOLDER_PATH+="\\Images\\";
        }else{
            //this is macOS path formatting
            GAMELOG_FOLDER_PATH+="/Game_Log_Folder/";
            IMAGES_FOLDER_PATH+="/Images/";
        }
    }




}
