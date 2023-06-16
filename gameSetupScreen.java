import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class gameSetupScreen extends JFrame implements ActionListener{

    //declare all the variables nescessary
    private static int maxEachPile=0;
    private static int pileNum=0;

    private static int frameWidth = 600;
    private static int frameHeight = 600;

    private static String difficulty = "";

    private JPanel titlePanel;
    private JPanel maxChips;
    private JPanel maxPiles;
    private JPanel playGame;
    private JPanel mainPanel;

    private static JButton enterPiles;
    private static JButton enterMax;
    private static JButton play;

    private static JTextField piles;
    private static JTextField max;


    public gameSetupScreen(){
        //setup JFrame
        new JFrame("Setup Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(frameWidth,frameHeight);
    
        //initialize all the different panels
        titlePanel = new JPanel();
        maxChips = new JPanel(); //max chips holds the JTextField to enter the max chips in each pile
        maxPiles = new JPanel(); //maxPiles holds the JTextField to enter the max piles in the game
        playGame = new JPanel();
        playGame.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

        generatePanels();
        getContentPane().add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void generatePanels() {
        addTitlePanel();
        addPilePanel();
        addChipPanel();
        addPlay();
    }

    private void addPlay() {
        //add the difficuly buttons to the play panel
        play = new JButton("PLAY!");
        play.addActionListener(this);

        JLabel selectDifficulty = new JLabel("Select Difficulty: ");

        JButton easy = new JButton("Easy");
        easy.addActionListener(this);

        JButton medium = new JButton("Medium");
        medium.addActionListener(this);

        JButton hard = new JButton("Hard");
        hard.addActionListener(this);

        JButton impossible = new JButton("Impossible");
        impossible.addActionListener(this);

        addComponents(playGame,selectDifficulty,easy,medium,hard,impossible,play);
        playGame.setAlignmentY(CENTER_ALIGNMENT);
        mainPanel.add(playGame);
    }

    public void addComponents(JPanel parent, Component ... components){
        for(Component component:components){
            parent.add(component);
        }
    }

    private void addChipPanel() {
        //add the planel to handle how many chips to add in each pile
        max = new JTextField(15);
        max.setText("Absolute Max Chips is "+gameEngine.MAX_CHIPS_IN_PILE);
        //this focus listener is to detect whether or not the cursor is in the JTextField
        //if the cursor is not in the JTextField, a filler text is displayed showing what the max chips in each pile is allowed
        max.addFocusListener(new FocusListener() {

            //method for when cursor is in the JTextField
            //set textfield to empty so user can input
            @Override
            public void focusGained(FocusEvent e) {
                Font font = max.getFont();
                font = font.deriveFont(Font.PLAIN);
                max.setFont(font);
                max.setForeground(Color.black);
                max.setText("");
            }

            //whenever focus lost, set text in text field to the filler text
            @Override
            public void focusLost(FocusEvent e) {
                Font font = max.getFont();
                font = font.deriveFont(Font.ITALIC);
                max.setFont(font);
                max.setForeground(Color.gray);
                if(max.getText().length()==0){
                    max.setText("Absolute Max Chips is "+gameEngine.MAX_CHIPS_IN_PILE);
                }
            }
        });
        enterMax = new JButton("Enter Max in Each Pile");
        enterMax.addActionListener(this);
        maxChips.add(max);
        maxChips.add(enterMax);
        maxChips.setFocusable(true);
        mainPanel.add(maxChips);

    }

    private void addPilePanel() {
        //this method follows the same logic as above ^^
        piles = new JTextField(15);
        piles.setText("Absolute Max Piles is "+gameEngine.MAX_PILES);

        piles.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                Font font = piles.getFont();
                font = font.deriveFont(Font.PLAIN);
                piles.setFont(font);
                piles.setForeground(Color.black);
                piles.setText(null);
            }
            @Override
            public void focusLost(FocusEvent e) {
                Font font = piles.getFont();
                font = font.deriveFont(Font.ITALIC);
                piles.setFont(font);
                piles.setForeground(Color.gray);
                if(piles.getText().length()==0){
                    piles.setText("Absolute Max Piles is "+gameEngine.MAX_PILES);
                }
                
            }
        });
        enterPiles = new JButton("Enter Max Piles");
        enterPiles.addActionListener(this);
        maxPiles.add(piles);
        maxPiles.add(enterPiles);
        maxPiles.setFocusable(true);
        mainPanel.add(maxPiles);
    }

    private void addTitlePanel() {
        Border border = BorderFactory.createLineBorder(Color.black,2); 
        JLabel title = new JLabel("Game Setup Screen");
        title.setFont(new Font("Times New Roman",Font.BOLD,45));
        title.setBorder(border);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(title);
        mainPanel.add(titlePanel);

    }

    public void actionPerformed(ActionEvent e) {
        //actino performed method to handle all the buttons when clicked   
        JButton source = (JButton) e.getSource();
        if(source == enterPiles){
            System.out.println(piles.getText());
            pileNum = Integer.parseInt(piles.getText());
            if(pileNum>gameEngine.MAX_PILES||pileNum<1){
                pileNum=0;
                JOptionPane.showMessageDialog(
                null,"Please enter a valid pile numer","Answer Dialog",JOptionPane.ERROR_MESSAGE);
            }
            piles.setText("");
        }else if(source == enterMax){
            maxEachPile = Integer.parseInt(max.getText());
            if(maxEachPile>gameEngine.MAX_CHIPS_IN_PILE||maxEachPile<1){
                //detection for valid input
                maxEachPile=0;
                JOptionPane.showMessageDialog(
                null,"Please enter a valid max chip value","Answer Dialog",JOptionPane.ERROR_MESSAGE);
                
            }
            max.setText("");
        }else if(source == play){
            //detection for a valid input
            if(pileNum==0||maxEachPile==0){
                JOptionPane.showMessageDialog(
                null,"Please enter valid numbers for the parameters of the game!","Answer Dialog",JOptionPane.ERROR_MESSAGE);
            }else if(difficulty.equals("")){
                JOptionPane.showMessageDialog(
                null,"Please select a difficulty!","Answer Dialog",JOptionPane.ERROR_MESSAGE);
            }else{
                System.out.println(pileNum+" "+ maxEachPile);
                dispose();
                new gamePlayScreen(new gameEngine(pileNum, maxEachPile,difficulty,new Stopwatch(1000)));
            }
            
        }else{
            //set the difficulty to the text of the difficulty button that was clicked
            difficulty = source.getActionCommand();
        }
    }

}