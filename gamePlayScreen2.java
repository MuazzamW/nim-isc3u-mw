/*
 * THIS CLASS IS NOT USED
 * I made this class originally with the intent on using the drag and drop mechanic for the chips
 * I wasn't able to implement the mechanic, but this class still has some code which demonstrates
 * that I was able to implement a new concept in a way
 */


import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public class gamePlayScreen2 extends JFrame{
    
    private final int chipWidth = (int)ChipPanel.CHIP_DIMENSIONS.getWidth();
    private final int chipHeight = (int)ChipPanel.CHIP_DIMENSIONS.getHeight(); 
    
    
    private int frameWidth;
    private int frameHeight;
    private final int vGap = 150;

    private final int turnPanelWidth = 200;
    private final int turnPanelHeight = turnPanelWidth/2;
    private final int timerPanelWidth = turnPanelWidth;
    private final int timerPanelHeight = turnPanelHeight;
    private int maxPileChips;


    private ArrayList<Integer> gameState;

    private ChipPanel chipPanel;
    private JPanel turnPanel;
    private JPanel timerPanel;
    //private JPanel stack;


    private final gameEngine engine;

    public gamePlayScreen2(gameEngine gameEngine){
        this.engine = gameEngine;
        gameState = engine.getState();

        if(turnPanelWidth+timerPanelWidth>gameState.size()*chipWidth+5){
            frameWidth = turnPanelWidth+timerPanelWidth;
        }else{
            frameWidth = gameState.size()*chipWidth+5;
        }
        System.out.println("Frame width: "+frameWidth);
        //declare pilePanel
        maxPileChips = getMaxPileHeight(gameState);
        System.out.println("turn panel height "+turnPanelHeight);
        frameHeight = maxPileChips*chipHeight+vGap+turnPanelHeight;
        
        System.out.println("frame height "+ frameHeight);

        //set layout of the main JFrame
        setLayout(null);
        setSize(frameWidth, frameHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setResizable(false);

        refresh();

        setVisible(true);
        
    }

    private void setPanels(){
        setTurnPanel();
        setTimerPanel();
        setPilePanel(maxPileChips);
    }

    private void loadPanels(){
        getContentPane().add(chipPanel);
        getContentPane().add(turnPanel);
        getContentPane().add(timerPanel);
        
    }

    public void refresh(){
        setPanels();
        loadPanels();
    }

    private void setTurnPanel(){
        turnPanel = new JPanel();
        turnPanel.setBackground(Color.green);
        turnPanel.setSize(turnPanelWidth,turnPanelHeight);
        turnPanel.setBounds(0,0,turnPanel.getWidth(),turnPanel.getHeight());
    }

    private void setTimerPanel(){
        timerPanel = new JPanel();
        timerPanel.setBackground(Color.BLUE);
        timerPanel.setSize(timerPanelWidth,timerPanelHeight);
        timerPanel.setBounds(frameWidth-timerPanelWidth,0,timerPanel.getWidth(),timerPanel.getHeight());
    }

    private void setPilePanel(int maxPileHeight){
        int pilePanelHeight = maxPileHeight*chipHeight+100;
        chipPanel = new ChipPanel(pilePanelHeight);
        setPileStacks();
    }

    private void setPileStacks() {
        for(int i = 0; i<gameState.size();i++){
            for(int j = 0; j<gameState.get(i);j++){
                int chipX = chipWidth*(i+1);
                int chipY = frameHeight-(gameState.get(i)*chipHeight)+(gameState.get(i)-(i+1))*chipHeight;
                chipPanel.addNewChip(chipX, chipY);
            }
        }

    }

    private class ChipPanel extends JPanel{

        ImageIcon chip = ImageFolderManager.getImageIcon("Game Chip.png");
        ImageIcon darkenedChip = darkenImage(chip);
        public static final Dimension CHIP_DIMENSIONS = ImageFolderManager.getImageDimensions("Game Chip.png");

        Point imageCorner;
        Point prevPoint;

        public ChipPanel(int pilePanelHeight){

            this.setLayout(null);
            this.setBackground(Color.GRAY);
            this.setSize(frameWidth, pilePanelHeight);
            this.setBounds(0,frameHeight-pilePanelHeight, this.getWidth(),this.getHeight());

            
            //this.addMouseListener(enterListener);
            //this.addMouseListener(exitListener);
        }

        public void addNewChip(int chipX, int chipY){
            imageCorner = new Point(chipX,chipY);
            ClickListener clickListener = new ClickListener();
            DragListener dragListener = new DragListener();
            //EnterListener enterListener = new EnterListener();
            //ExitListener exitListener = new ExitListener();

            this.addMouseListener(clickListener);
            this.addMouseMotionListener(dragListener);
        }


        public void paintComponent(Graphics g){
            super.paintComponent(g);
            chip.paintIcon(this,g, (int)imageCorner.getX(),(int)imageCorner.getY());
        }

        private class ClickListener extends MouseAdapter{
            public void mousePressed(MouseEvent e){
                prevPoint = e.getPoint();
            }
        }

        private class DragListener extends MouseMotionAdapter{
            public void mouseDragged(MouseEvent e){
                Point currentPt = e.getPoint();
                imageCorner.translate(
                    (int)(currentPt.getX()-prevPoint.getX()),
                    (int)(currentPt.getY()-prevPoint.getY())
                );
                prevPoint = currentPt;
                repaint();
            }
        }

        private class EnterListener extends MouseAdapter{
            public void mouseEntered(MouseEvent e){
                chip.setImage(darkenedChip.getImage());
            }
        }

        private class ExitListener extends MouseAdapter{
            public void mouseExited(MouseEvent e){
                chip.setImage(chip.getImage());
            }
        }

        private ImageIcon darkenImage(ImageIcon imageIcon) {
            Image image = imageIcon.getImage();
            System.out.println(image.getWidth(null));
            System.out.println(image.getHeight(null));
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.drawImage(image, 0, 0, null);

            float darkenFactor = 0.6f; // Adjust the darkness level as desired

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

    }

    private int getMaxPileHeight(ArrayList<Integer> list){
        int max = 0;
        for(int num:list){
            if (num>max){max=num;}
        }
        return max;
    }

   
    
}
