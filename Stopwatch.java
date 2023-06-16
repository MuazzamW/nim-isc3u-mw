import java.awt.Font;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;

//this class is the custom Stopwatch class that creates a JLabel that automatically increments the time every second and updates the label
public class Stopwatch extends JLabel{

    private Timer stopWatch;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    
    public Stopwatch(int freqInMilliseconds){
        //initialize the base text as 0 time elapsed
        setText("00:00:00");
        setHorizontalAlignment(JLabel.CENTER);
        setFont(new Font("Times New Roman", Font.PLAIN,36));
        
        //set the new timer
        stopWatch = new Timer();
        //the timer task is to update the stopwatch every x milliseconds where x is the freqInMilliseconds denoted by the constructor
        stopWatch.schedule(new TimerTask() {
        
            @Override
            public void run() {
                seconds++;
                if(seconds==60){
                    minutes++;
                    seconds=0;
                    if(minutes==60){
                        hours++;
                        minutes=0;
                    }
                }
                String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                setText(time);
            }
            
        },0,freqInMilliseconds);
    }

    //method to stop the timer when the game is done
    public void stopTime(){
        stopWatch.cancel();
    }

    //returns the elapsed time at any given moment
    public String getElapsedTime(){
        return getText();
    }
    
}

