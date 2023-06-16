public class Main {

    public static void main(String[] args) {
        //detect and set the operating system of the device
        System.out.println(System.getProperty("os.name"));
        new FolderPathsManager(System.getProperty("os.name"));

        //start the title screen
        new titleScreen();
    
    }
    
}
