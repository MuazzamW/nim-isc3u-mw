import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;

//this class is to handle all the gamelogs which are created
public class gameLogFolder {
    private ArrayList<String> logNames;
    private static String PATH;
    File folder;
    public gameLogFolder(){
      PATH = FolderPathsManager.GAMELOG_FOLDER_PATH;
      //initialize folder path
      folder = new File(PATH);
      
      if (folder.isDirectory()) {
          System.out.println("Directory already Created!");
      }
      else {
          if(!folder.mkdir()==true){
          System.out.println("Unsuccessful");
          }
      }
      logNames = generateAllLogs();
    }

    private String getLogPath(String logName){
      return PATH+logName+".txt";
    }

    //method to create a new log with a summary
    public void newLog(String fileName, String summary){
      String filePath =  getLogPath(fileName);
      System.out.println(filePath); 
      try {
            File newLog = new File(filePath);
            if (newLog.createNewFile()) {
              System.out.println("File created: " + fileName);
              logNames.add(fileName);
            } else {
              System.out.println("File already exists.");
            }
          } catch (IOException e) {
            System.out.println("Error!");
            e.printStackTrace();
          }
      writeToLog(filePath,summary);
    }

    //delete the log when user clicks deleteLog button
    public void deleteLog(String fileName){
      String filePath = getLogPath(fileName);
      File badFile = new File(filePath);
      System.out.println(badFile.getPath());
      if (badFile.delete()) { 
        System.out.println("Deleted the file: " + badFile.getName());
        logNames.remove(logNames.indexOf(fileName));
      } else {
        System.out.println("Deletion not successful, File does not exist or is corrupt");
      } 
    }

    //write to the log whenever a new log is created
    private void writeToLog(String logPath, String summary){
      try{
        System.out.println("logPath is"+logPath);
        FileWriter writer = new FileWriter(logPath);
        writer.write(summary);
        writer.close();
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
      

    }

    //sort the logs using a simple bubble sort algorithm
    private void sortLogs(){
        for (int j = 0; j < logNames.size(); j++) {
			    for (int i = 0; i < logNames.size() - j - 1; i++) {
            if(isEarlier(logNames.get(i+1), logNames.get(i))){
              swap(i,i+1);
            }

			    }
        }
    }

    private void swap(int leftPointer, int rightPointer){
        String temp = logNames.get(leftPointer);
        logNames.set(leftPointer,logNames.get(rightPointer));
        logNames.set(rightPointer,temp);
    }

    public boolean logAlreadyExists(String logName){
      sortLogs();
      //simple binary search to find out whether or not the logName already
      //exists in the date folder
      System.out.println(logNames.toString());
      int left = 0, right = logNames.size() - 1;
        while (left<=right) {
            int mid = left+(right-1) / 2;
            System.out.println("mid "+mid);
            // Check if logName is the middle logName in logNames
            if (logNames.get(mid).equals(logName))
                return true;
 
            // If the logName to be found is later than mid, ignore the left half
            if (isEarlier(logNames.get(mid), logName)){
              left = mid+1;
            }else{
              right = mid-1;
            }
            
                
        }
 
        // If we reach here, then the date does not already exist
        return false;
    }
    
    public boolean isEarlier(String logName1, String logName2){
      //this method converts each of the dates to a float value by weighting
      //the months as 12/365 and the dates as 1/365
      double log1 = Float.parseFloat(logName1.substring(6, 10))+
      (12.0d/365.0d)*Float.parseFloat(logName1.substring(3,5))+
      (1.0d/365.0d)*Float.parseFloat(logName1.substring(0,2));

      double log2 = Float.parseFloat(logName2.substring(6, 10))+
      (12.0d/365.0d)*Float.parseFloat(logName2.substring(3,5))+
      (1.0d/365.0d)*Float.parseFloat(logName2.substring(0,2));

      //the dates can now be numerically compared
      //if log1 has a smaller numerical value than log2, then it must be earlier 
      if (log1<log2){
        return true;
      }
      return false;
    }

    //returns the log summary as a string
    public String getLogSummary(String logName){
      String logPath = getLogPath(logName);
      String summary = "";
      try{
        FileReader log = new FileReader(logPath);
        BufferedReader br = new BufferedReader(log);
        
        String line;
        while ((line = br.readLine()) != null){
          summary+=line+"\n"; 
        }
        System.out.println(summary);
        log.close();
        br.close();
      }catch(IOException e){
        
      }
      return summary;
    }

    //generates all the logs for the gameLogScreen class
    private ArrayList<String> generateAllLogs(){

      try{
        File[] logs = folder.listFiles();
        ArrayList<String> logNames = new ArrayList<String>();
        for(File log:logs){
          if(log.isFile() && log.getName().endsWith(".txt")){
            
            logNames.add((log.getName().substring(0, log.getName().length()-4)));
          }
        }
        return logNames;

      }catch(Exception e){
        e.printStackTrace();
      }
      return null;
    }

    //returns all the logs for the gameLogScreen class
    public ArrayList<String> getAllLogs(){
      System.out.println(logNames.toString());
      //sorts the logNames before returning it anywhere
      sortLogs();
      return logNames;
    } 
    
    
}
