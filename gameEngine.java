import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class gameEngine {
    //declare class and object variables
    public static final int MAX_PILES = 20;
    public static final int MAX_CHIPS_IN_PILE = 20;
    //game ArrayList to store the state of the game
    private ArrayList<Integer> game;
    private int piles;
    private int difficulty;
    private  boolean isTurn;
    private  Random randomizer = new Random();
    private boolean isLost = false;
    private gameLogFolder folder;
    //initialize map of the difficulty String to the corresponding integer
    private final Map<String,Integer> gameDifficulty = Map.of(
       "Easy",25,
       "Medium",50,
       "Hard",75,
       "Impossible",100 
    );
    private int initialChips;
    private String datePlayed;
    private String timePlayed;
    //create new instance of custom class Stopwatch which initiates a stopwatch label for the Frame
    Stopwatch stopWatch;

    public gameEngine(int pileNum,int maxInPile,String difficultySetting,Stopwatch watch){
        //initilize stopwatch
        stopWatch = watch;
        
        //the difficulty of the game is internally represented as the integer which corresponds to the diffulty String
        //from the button that was selected in the gameSetupScreen
        difficulty = gameDifficulty.get(difficultySetting);
        folder = new gameLogFolder();

        //formates the date and time for the summary log at the end
        formatDateTime();

        //sets the pile number
        piles = pileNum;
        //initializes the game state arraylist
        game = new ArrayList<Integer>();
        for(int i = 0; i<piles;i++){
            int chips = (int)(Math.random()*maxInPile+1);
            game.add(chips);
            initialChips++;
        }

        //declares initial turn randomly, true if computer starts, false if user starts
        ArrayList<Boolean> turn = new ArrayList<Boolean>();
        turn.add(true);
        turn.add(false);
        isTurn = turn.get(randomizer.nextInt(turn.size()));

        System.out.println("initial "+game.toString());
        System.out.println("is turn "+isTurn);

    }

    private void formatDateTime(){
        //formats the date and time for the moment when the specific game was created
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateFormat timeFormat = new SimpleDateFormat("hh.mm aa");
        datePlayed = dtf.format(date);
        timePlayed = timeFormat.format(new Date()).toString();
    }

    //called from the gamePlayScreen every time it is the engine's turn to make the move
    public void makeMove(){
        int rand = randomizer.nextInt(100)+1;
        if(isTurn){
            if(strategyExists()&&rand<=difficulty){
                optimalMove();
            }else{
                arbitraryMove();
            }
            isTurn = false;
        }
       
    }

    //checks if a strategy exists by checking if the nim sum of the pile is not 0 (so it can be balaned)
    private boolean strategyExists(){
        int nimSum = calcNimSum();
        System.out.println("nim sum is "+nimSum);
        if(nimSum>0){
            return true;
        }
        return false;
    }

    //if no strategy exists, then the move doesn't matter so computer randomly picks chips off the pile
    private void arbitraryMove() {
        System.out.println("arbitrary");
        System.out.println(game.toString());
        int pile = randomizer.nextInt(game.size());
        int takeOff = (int)(Math.random()*game.get(pile)+1);
        if(game.get(pile)-takeOff==0){
            game.remove(pile);
        }else{
            game.set(pile,game.get(pile)-takeOff);
        }
        
        System.out.println(game.toString());
    }

    private void optimalMove() {
        //this is the method that is called when a winning strategy exists
        System.out.println("optimal");
        System.out.println(game.toString());
        int sum = calcNimSum();
        System.out.println("nim sum is "+sum);

        /*
         * The following code is the algorithm that solves the regular game of nim
         * It is based on the fact that the XOR (^) operation is commutative, meaning that
         * x^y = y^x
         * 
         * It works by calculating the XOR sum (Nim Sum) of all the chips in all the piles
         * Assuming that the nim sum is not 0 (which is the winning case),
         * the algorithm finds the right pile k such that by removing n chips from pile k,
         * the nim sum of the game is set back to 0. It does this every time so that it always
         * ends up taking the last chip and winning the game. The difficulty mechanic comes in
         * by randomizing the chance that the algorithm makes the optimal move.
         * If difficulty is set to "Hard" (for example), then the algorithm has a 75% chance of making the right
         * move since it randomizes a number between 1-100 and only makes the move if the
         * number is under 75.
         * 
         * Let x = nim sum of game before move
         * The algorithm finds the pile k with n chips such that (n^x)<n
         * It checks if (n^x)<n because that is the only way it can set the 
         * nim sum to 0 by REMOVING chips, otherwise it will have to add chips
         * which is against the rules of the game. It then replaces n with (n^x)
         * since this is the value that will make the game have a nim sum of 0:
         * (n^x)^a^b^c... = 0 since n^a^b^c=x where a,b,c are the rest of the piles
         * Thus, (n^x)^a^b^c = x^(n^a^b^c...) = x^x = 0
         */

        for(int i = 0; i< game.size();i++){
            if ((sum^game.get(i))<game.get(i)){
                game.set(i,game.get(i)^sum);
                if(game.get(i)==0){
                    game.remove(i);
                }
                break;
            }
        }

        System.out.println(game.toString());
        System.out.println("new nim sum is "+calcNimSum());

    }

    private int calcNimSum(){
        //calculated the nimsum by XORing all the chips in all the piles together
        int nimSum = 0;
        for(int i = 0; i<game.size();i++){
            nimSum^=game.get(i);
        }

        return nimSum;
    }

    public void setTurn(boolean turn){
        isTurn = turn;
    }

    public boolean isEngineTurn(){
        return isTurn;
    }

    public ArrayList<Integer> getState(){
        //returns the state of the game
        return game;
    }

    public void evaluateState(ArrayList<Integer> changedState){
        //called everytime the user removes a certain number of chips from their stack
        //the state of the game is evaluated and the turn of the computer is set to true
        this.game = changedState;
        if (game.size()==1){
            isLost=true;
        }
        System.out.println(game.toString());
        isTurn = true;
    }

    public boolean isLost(){
        //returns whether or not the computer has lost the game
        return isLost;
    }

    public Stopwatch getStopwatch(){
        //returns the stopwatch so it can be displayed on the main frame
        return stopWatch;
    }

    public void endGame(){
        //called everytime the engine detects the game has ended
        //it formats a summary and detects whether or not a summary of the same date already exists
        //if it does, then the time is added to the date as well to be more specific
        stopWatch.stopTime();
        String summary = """
                Number of initial chips: %d
                Number of initial piles: %d
                Date played: %s
                Time Played: %s 
                Total Elapsed Time: %s
                """;
        summary = String.format(summary,initialChips,piles,datePlayed,timePlayed,stopWatch.getElapsedTime());
        if(folder.logAlreadyExists(datePlayed)){
            folder.newLog(datePlayed.substring(0,10)+" "+timePlayed, summary);
        }else{
            folder.newLog(datePlayed.substring(0,10), summary);
        }
        
    }
    
}
