import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class gamePlayScreen extends JFrame{

    //this is the main class of the program that handles all the logic of the user experience and user interface
    
    //declare variables
    private final ImageIcon chip = ImageFolderManager.getImageIcon("Game Chip.png");
    private ImageIcon darkenedChip;
    private final int chipWidth = chip.getIconWidth();
    private final int chipHeight = chip.getIconHeight();
    private final int vGap = 150;
    private final int turnPanelWidth = 200;
    private final int turnPanelHeight = turnPanelWidth/2;
    private final int timerPanelWidth = turnPanelWidth;
    private final int timerPanelHeight = turnPanelHeight;

    private int frameWidth;
    private int frameHeight;
    private int maxPileChips;

    private Timer moveTimer;
    private ArrayList<Integer> gameState;

    private JPanel pilePanel;
    private JPanel turnPanel;
    private JPanel timerPanel;
    private JPanel playNimPanel;

    private JLabel playNim;

    static JButton removeChips;

    private final gameEngine engine;

    //constructor, runs the setUpGUI() method after initializing the gameEngine
    public gamePlayScreen(gameEngine gameEngine){
        this.engine = gameEngine;

        //set darkened chip value
        darkenedChip = darkenImage(chip);

        setUpGUI();
        
    }

    public void setUpGUI(){
        //sets the gameState of the gamePlayScreen to the game state of the engine
        //both the gamePlay screen and game engine have a game state
        //they each update eachother whenever a move is made
        gameState = engine.getState();
        System.out.println("new game state = "+gameState);

        if(turnPanelWidth+timerPanelWidth>gameState.size()*chipWidth+5){
            frameWidth = turnPanelWidth+timerPanelWidth;
        }else{
            frameWidth = (gameState.size()+1)*(chipWidth);
        }
        System.out.println("Frame width: "+frameWidth);

        //declare pilePanel
        maxPileChips = getMaxPileHeight(gameState);
        System.out.println("max pile height is "+maxPileChips*chipHeight);
        frameHeight = maxPileChips*chipHeight+vGap+turnPanelHeight+75;

        //method calls to declare all the panels in the game
        setLayout(null);
        setSize(frameWidth, frameHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPanels();
        loadPanels();
        setLocationRelativeTo(null);
        getContentPane().setBackground(ColorManager.LIGHT_YELLOW);
        setVisible(true);
        checkStatus();
        
    }

    private void checkStatus(){
        //runs every time to check whether or not the game has ended
        //the game ends when there are no chips left. If it is the computer's turn at that point, user wins, otherwise user loses
        if(gameState.size()==0){
            engine.endGame();
            if(!engine.isEngineTurn()){
                JOptionPane.showMessageDialog(
                null,"You LOSE!","Answer Dialog",JOptionPane.ERROR_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(
                null,"You WIN","Answer Dialog",JOptionPane.INFORMATION_MESSAGE);
            }
            int dialogButton = JOptionPane.showConfirmDialog (null, 
            "Play Again?","Descision",JOptionPane.YES_NO_OPTION);
            if(dialogButton == JOptionPane.YES_OPTION) 
            {new gameSetupScreen();dispose();}else {new titleScreen();dispose();}
        }else if(engine.isEngineTurn()){
            engine.makeMove();
            //declare timer to wait a few seconds before the engine makes the move. Gives the illusion that the engine is "Thinking"
            moveTimer = new Timer();
            moveTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    refresh();
                }
                
            }, 2000);
        }

    }

    private void setPanels(){
        //set the panels up individually
        setTurnPanel();
        setTimerPanel();
        setPilePanel(maxPileChips);
        setCenterTitlePanel();
    }

    private void loadPanels(){
        //load panels onto the screen
        getContentPane().add(pilePanel);
        getContentPane().add(turnPanel);
        getContentPane().add(timerPanel);
        getContentPane().add(playNimPanel);
        
    }

    public void refresh(){
        dispose();
        new gamePlayScreen(engine);     
    }

    private void setCenterTitlePanel(){
        //title screen of the game, displays a Play Nim! graphic in the middle of the contentPane()
        playNimPanel = new JPanel(new GridBagLayout());
        playNim = new JLabel(ImageFolderManager.getImageIcon("Play Nim.png"));
        playNimPanel.setBounds((frameWidth-playNim.getWidth())/2, turnPanelHeight+50, playNim.getWidth(),playNim.getHeight());
        playNimPanel.setSize(playNim.getWidth(),playNim.getHeight());
        playNimPanel.add(playNim);
    }

    private void setTurnPanel(){
        //sets the turn panel on the top left corner, displays whether it is user or computer's turn
        turnPanel = new JPanel();
        turnPanel.setSize(turnPanelWidth,turnPanelHeight);
        turnPanel.setBounds(0,0,turnPanel.getWidth(),turnPanel.getHeight());
        turnPanel.setLayout(new GridBagLayout());
        turnPanel.setBorder(BorderFactory.createLineBorder(Color.black,1));
        JLabel turn;
        if(engine.isEngineTurn()){
            turn = new JLabel("Who's Turn: Computer");
        }else{
            turn = new JLabel("Who's Turn: User");
        }
        turnPanel.setBackground(ColorManager.LIGHT_YELLOW);
        turnPanel.add(turn);
    }

    private void setTimerPanel(){
        //sets the stopwatch panel in the top right corner
        timerPanel = new JPanel();
        timerPanel.setSize(timerPanelWidth,timerPanelHeight);
        timerPanel.setBounds(frameWidth-timerPanelWidth,0,timerPanel.getWidth(),timerPanel.getHeight());
        timerPanel.setBorder(BorderFactory.createLineBorder(Color.black,1));
        timerPanel.setLayout(new GridBagLayout());
        timerPanel.setBackground(ColorManager.LIGHT_YELLOW);
        //retrieve the stopwatch from the engine
        timerPanel.add(engine.getStopwatch());
    }

    private void setPilePanel(int maxPileHeight){
        //set the pile panel at the bottom of the frame
        //this will hold all the chips and the click mechanic
        removeChips = new JButton("Remove Chips");
        removeChips.setSize(150,30);
        removeChips.setVisible(false);
        removeChips.setBounds((frameWidth-removeChips.getWidth())/2,frameHeight-75,removeChips.getWidth(),removeChips.getHeight());
        
        int pilePanelHeight = maxPileHeight*chipHeight+100;
        pilePanel = new JPanel();
        pilePanel.setLayout(null);
        pilePanel.setBorder(BorderFactory.createLineBorder(Color.black,1));
        pilePanel.setSize(frameWidth, pilePanelHeight);
        pilePanel.setBounds(0,frameHeight-pilePanelHeight, pilePanel.getWidth(),pilePanel.getHeight());
        pilePanel.setBackground(ColorManager.LIGHT_YELLOW);
        setPileStacks();
        add(removeChips);
    }

    private void setPileStacks() {
        //for every stack in the game, create a new stack object which will hold all the chips
        for(int i = 0; i<gameState.size();i++){
            int stackHeight = gameState.get(i)*chipHeight;
            int stackWidth = chipWidth+3;

            //System.out.println("stack width " + stackWidth);
            //System.out.println("stack height "+ stackHeight);
 
            Stack stack = new Stack(stackWidth, stackHeight,i);
            stack.setBackground(ColorManager.LIGHT_YELLOW);
            Stack.stacks.add(stack);
            stack.setChips(gameState.get(i),i);
            pilePanel.add(stack);
        }

    }

    //private stack class which holds all the information regarding the number of chips in the stack and whether or not the stack is selected by the user
    private class Stack extends JPanel{
        public int STACK_NUMBER;
        public boolean isSelected;
        public static ArrayList<Stack> stacks = new ArrayList<Stack>();
        public ArrayList<Chip> chipsInStack = new ArrayList<Chip>();
        public int numChipsSelected = 0;

        public Stack(int width, int height, int stackNumber){
            //declare the initial variables when the stack is created
            STACK_NUMBER = stackNumber;
            //by default, no stack is selected
            isSelected = false;
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setSize(width,height);
            int edgeGap = (frameWidth-width*gameState.size())/2;
            this.setBounds(edgeGap+width*stackNumber,pilePanel.getHeight()-this.getHeight()-75,
            this.getWidth(),this.getHeight());
        }

        public void setChips(int chipsInPile, int stackNumber){
            //big method to set the chips in each stack
            for(int i = 0; i<chipsInPile;i++){
                //create a new chip object for every number of chips in the pile
                Chip chip1 = new Chip(chip,i);
                chipsInStack.add(chip1);
                //only set up the clicking mechanic if it is the user's turn
                //when it's the engine's turn, we do not want the user to be able to click any chips
                if(!engine.isEngineTurn()){
                    //add new mouse click listener for each chip
                    chip1.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        //determine to which stack each chip belongs by getting the parent component of each chip
                        Stack stack = (Stack)chip1.getParent();
                        //below is the logic to determine whether or not a stack is selected so that the user cannot select more than one stack at a time
                        //if the chip is displaying a regular icon, that means that it is not selected and since it is under the mouse clicked method, the user
                        //has selected this stack
                        if(chip1.getIcon()==chip){
                            //if there are no other stacks selected other than the stack which has just been selected, then set the selected stack to the stack
                            //that the user has selected, otherwise, throw an error message saying that user cannot select more than one stack
                            if(Stack.noOtherStacksSelected(stack)){
                                if(stack.isEmpty()){
                                    stack.isSelected=true;
                                    //this print line statement will only run if the stack has been properly selected
                                    System.out.println("Stack "+stack.STACK_NUMBER+" has been selected");
                                    //whenever a stack has been selected, display a button that allows the user to remove the selected chips from the stack
                                    //if no stacks are selected, there is no point in displaying the button
                                    removeChips.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            //if the remove chips button is selected, then remove that number of chips that have been selected, from the stack,
                                            //then pass that information to the game engine so that the best decision can be made based on the difficulty
                                            int chipsLeft = gameState.get(stack.STACK_NUMBER)-stack.numChipsSelected;
                                            if(chipsLeft==0){
                                                //if the stack has no more chips left, then remove that stack object compeltely
                                                gameState.remove(stack.STACK_NUMBER);
                                            }else{
                                                gameState.set(stack.STACK_NUMBER,chipsLeft);
                                            }
                                            engine.evaluateState(gameState);
                                            stack.isSelected=false;
                                            removeChips.setVisible(false);
                                            //finally refresh the frame so the user can see the descision that will be made by the computer
                                            refresh();
                                            
                                            
                                        }
                                    });
                                    removeChips.setVisible(true);
                                }
                                //if no other stack was selected, then set the chip image to the darkened chip image to indicate that the chip has been selected
                                chip1.setIcon(darkenedChip);
                                stack.numChipsSelected++;
                            }else{
                                //runs whenever the game detects that more than one stack have been selected
                                JOptionPane.showMessageDialog(
                            null,
                                String.format("You cannot select more than one stack!\nStack %d is already selected",
                                Stack.getSelectedStack()+1),
                        "Answer Dialog",JOptionPane.ERROR_MESSAGE);
                            }     
                        }else{
                            //if the chip was already a darkened chip, then set it back to the normal chip image
                            chip1.setIcon(chip);
                            //decrement the number of chips in the stack which have been selected
                            stack.numChipsSelected--;
                            //if the stack is empty, ensure that the program knows this so it does not throw an error message when the user tries to select another stack
                            if(stack.isEmpty()){
                                stack.isSelected=false;
                                System.out.println("Stack "+stack.STACK_NUMBER+" is no longer selected");
                                removeChips.setVisible(false);
                            }
                        }                        
                    }
                });
                }
                //finally, add the chip with all its conditions
                this.add(chip1);
            }
        }

        //below are all the methods which help the program determine the state of the stack and chips in each stack
        public boolean isEmpty(){
            return(this.numChipsSelected==0);
        }

        public static boolean noOtherStacksSelected(Stack selectedStack){
            for(Stack stack:stacks){
                if(stack.isSelected&&stack!=selectedStack){
                    return false;
                }
            }
            return true;
        }

        public static int getSelectedStack(){
            for(Stack stack:stacks){
                if(stack.isSelected){
                    return stack.STACK_NUMBER;
                }
            }
            return -1;
        }
    }

    //private chip class
    private class Chip extends JLabel{

        public Chip(ImageIcon icon,int stackNumber){
            this.setIcon(icon);
        }

    }

    //this method is only run once and it is to set an image of a darkened chip
    //it does so by individually darkening every pixel of the game chip image and then returning the object as a buffered image
    private ImageIcon darkenImage(ImageIcon imageIcon) {
        Image image = imageIcon.getImage();
        System.out.println(image.getWidth(null));
        System.out.println(image.getHeight(null));
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
            BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(image, 0, 0, null);

        float darkenFactor = 0.6f;

        //individually darkens every pixel of the image where the mouse is detected
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                //get original RGB value for the pixel
                int rgb = bufferedImage.getRGB(x, y);
                //set the colour of the pixel as a new colour object
                Color color = new Color(rgb, true);
                //individually darken each of the red, green and blue values by the darken factor
                int r = (int) (color.getRed() * darkenFactor);
                int g = (int) (color.getGreen() * darkenFactor);
                int b = (int) (color.getBlue() * darkenFactor);
                int a = color.getAlpha();
                //set the darkened colour as the new colour
                Color darkerColor = new Color(r, g, b, a);
                //set the values to the buffered image of the chip
                bufferedImage.setRGB(x, y, darkerColor.getRGB());
            }
        }
        //return the darkened chip image
        return new ImageIcon(bufferedImage);
    }

    //method to get the height of the tallest pile in the gameState
    private int getMaxPileHeight(ArrayList<Integer> list){
        int max = 0;
        for(int num:list){
            if (num>max){max=num;}
        }
        return max;
    }
    
}
