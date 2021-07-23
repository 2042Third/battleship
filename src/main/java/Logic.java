import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.image.*;
public class Logic implements Runnable {
  public Logic (Battle a){
    parent = a;
  }
  public void run(){
    while (parent.cabinet.playing){
      pulse_value2=pulse(pulser);
      pulse_value1=pulse(pulser);
      flash_red = new Color(255,0,0,pulse_value1);
      flash_yellow = new Color(255,255,0,pulse_value1);
      flash_white = new Color(255,255,255,pulse_value2);
      flash_green = new Color(0,255,0,pulse_value2);
      flash_hit = new Color(255,255,0,pulse_value1>>2<<2);
      parent.cabinet.ticker+=1;     // Plan to use this for other functions to keep track of time.
      if(parent.cabinet.peer){
        net_status=parent.graphPane.GREEN;
      }
      else{
        net_status=flash_red;
      }
      synchronized(this){
        if(parent.cabinet.p2_ready){
          p2_status=parent.graphPane.GREEN;
        }else{
          p2_status=flash_red;
        }
      }
      if(parent.cabinet.s1==15){
        // parent.graphPane.winner_message="THIS IS THE WINNER!!!!!!!!!!!!";
      }
      try{
          Thread.sleep((int)16);    // This is fixed for keeping the game logic consistent at all fps. 
          }
      catch(InterruptedException e){
        System.out.println("Tick sleep interapted.");
      }
        
    }
  }

  /**
   * This forces the program to only register one button click at a time.
   * This would only matter if I make this a touch screen game...
   * To enable multi-touch support for this game, I would put a post_queue
   * instead of just a post variable in the Fridge class.
   * 
   * */
  public void post_action(int a){
    parent.cabinet.post = a;
  }
  public void update_ticker(){
    
  }
  public int pulse(int a){
    if(pulser>120 || pulser<3){
      // pulser=0;
      factor*=-1;
    }
    // System.out.println(pulser);
    pulser+=factor;
    return pulser;
  }
  public int slow_pulse(int a){
    if(slow_pulser>120 || slow_pulser<50){
      // pulser=0;
      slow_factor*=-1;
    }
    // System.out.println(pulser);
    slow_pulser+=slow_factor;
    return slow_pulser;
  }
  /**
   * Deprecated zoom function, currently.
   * Problem: made me dizzy.
   * May need fixing.
   * */
  public void zoom_view (){ 

    if(parent.graphPane.gd_scl!=0 && !parent.graphPane.menu)
        parent.graphPane.gd_scl = (parent.graphPane.gd_scl+1)%(parent.graphPane.resy/4)+1;
  }
  public void set_parent (Battle a){
    parent=a;
    net_status=parent.graphPane.GREEN;
    p1_status=parent.graphPane.GREEN;
    p2_status=parent.graphPane.GREEN;
  }
  public int pulse_value1;
  public int pulse_value2;
  public int pulser=4;
  public int slow_pulser=100;
  public int factor=2;
  public int slow_factor=1;
  public int shots= 3;
  public int ship_count = 0;
  public int ship_track = 5;
  public int ship_track_choosen = 0;
  private Battle parent;
  public Color flash_red;
  public Color flash_yellow;
  public Color flash_hit;
  public Color flash_white;
  public Color flash_green;
  public Color net_status;
  public Color p1_status;
  public Color p2_status;
  public boolean p1_r = false;
  public boolean p2_r = false;

}

