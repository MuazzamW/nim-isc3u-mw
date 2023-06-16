
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.Border;

//this clas is to display the game logs whenever the user wants to see their logs
public class gameLogScreen extends JFrame{

    private  int frameWidth = 400;
    private  int frameHeight = 300;
    private  HashMap<String, JButton> buttons;
    public   gameLogFolder folder;
    private  JLabel title;
    private JPanel titlePanel;
    private  JPanel buttonsPanel;
    private JPanel returnToTitlePanel;
    private JButton returnToTitle;
    private ArrayList<String> allLogs;
    
    public gameLogScreen (){

        //hashmap to hold the buttons based on the log name
        buttons = new HashMap<>();
        folder = new gameLogFolder();

        buttonsPanel = new JPanel();
        titlePanel = new JPanel();

        //gameLogScreen setup
        new JFrame("Game Logs");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        setSize(frameWidth,frameHeight);

        setResizable(false);

        setLocationRelativeTo(null);
        setVisible(true);
        addReturnToTitle();
        refresh();
        
    }

    //add the button that allows user to return to the title screen
    public void addReturnToTitle(){

        returnToTitlePanel = new JPanel(new FlowLayout());
        returnToTitle = new JButton("Return to Title Screen");
        returnToTitle.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            new titleScreen();
            dispose();
            }
        });
        returnToTitlePanel.add(returnToTitle);
        add(returnToTitlePanel);
    }

    //refresh the screen every time a log is deleted so that it is no longer shown on the screen
    public void refresh(){
        //buttonsPanel.remove(buttons.get("test1"));
        loadLogs();
        System.out.println("entered refresh method");

        buttonsPanel.removeAll();
        buttonsPanel.revalidate();

        titlePanel.removeAll();
        titlePanel.revalidate();
        
        //getContentPane().removeAll();
        setTitle();
        setButtonPanel();
        add(returnToTitle); 

    }

    //method to set the title of the screen
    private void setTitle(){
        Border border = BorderFactory.createLineBorder(Color.black,2);
        titlePanel.setBackground(ColorManager.LIGHT_YELLOW);
        title = new JLabel("Game Logs");
        title.setFont(new Font("Times New Roman",Font.BOLD,50));
        title.setBorder(border);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setHorizontalAlignment(JLabel.CENTER);
        //title.setBounds(0,0,100,100);

        titlePanel.add(title); 
        add(titlePanel);
    }

    //sets all the buttons in a panel with a GridLayout so that it can be converted into a JScrollPane
    private void setButtonPanel(){
        buttonsPanel.setBackground(Color.GRAY); 
        buttonsPanel.setLayout(new GridLayout(allLogs.size(),1));
        buttonsPanel.setSize(100,100);
        //add each button generated to the button pannel with it's own actionlistener
        for(String logName:allLogs){
            JButton button = buttons.get(logName);
            System.out.println(button.getName());
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    summaryScreen(button.getText());
                }
            });
            buttonsPanel.add(button);
        }

        //add to scroll bar so user can scroll
        JScrollPane scrollPane = new JScrollPane(buttonsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        getContentPane().add(scrollPane);

        
    }

    //method to load all the logs as buttons
    private void loadLogs(){
        allLogs = folder.getAllLogs();
        Collections.reverse(allLogs);
        buttons.clear();
        for(String logName : allLogs){
            JButton button = new JButton(logName);
            button.setName(logName);
            buttons.put(logName, button);
        }
        
    }
    //load the log summary every time the user clicks the summary button
    private String loadLogSummary(String logName){
        return folder.getLogSummary(logName);
    }

    //run every time the user clicks the button of the log. A new frame is displayed with the log details
    private void summaryScreen(String logName){
        JFrame summaryFrame = new JFrame(logName+" Summary");
        summaryFrame.setLayout(new FlowLayout());

        summaryFrame.setSize(300,300);
        summaryFrame.setVisible(true);
       
        //formats the summary line by line so that at every "\n" character, the string prints a new line 
        JLabel sum = new JLabel("<html>" + 
        loadLogSummary(logName).replaceAll("\n", "<br>") + "</html>");
        summaryFrame.add(sum);


        //create delete log button
        JButton delete = new JButton("Delete Log");
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                folder.deleteLog(logName);
                summaryFrame.dispose();
                refresh();
            }
        });

        summaryFrame.add(delete);
        summaryFrame.pack();
        
    }
    
}
