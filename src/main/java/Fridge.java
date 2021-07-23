import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
/**
* Homework 3
* 
* Fridge class ensures producer thread can store things in it and 
* the consumer thread can take the data when its ready. Makes sure
* the program is thread-safe. Thus, the analogy...
* @author Yi Yang
* @version rev. 1 17/7/2021
 */


public class Fridge {
  public void init(){
    resx=parent.screenWidth;
    resy=parent.screenHeight;
    // scl=resy/10;
    add_button(10,50,"Start"     ,0   ,ButtonAction.START);
    add_button(10,60,"Network"   ,1   ,ButtonAction.NETWORK);
    add_button(10,70,"Settings"  ,2   ,ButtonAction.DROPDOWN);
    add_button(10,80,"Console"     ,3   ,ButtonAction.CONSOLE);
    add_button(10,90,"Exit"      ,4   ,ButtonAction.EXIT);
    add_label(40,90,"NETWORK"      ,0 , LabelType.NETWORK );
    add_label(160,80,""      ,2, LabelType.BATTLESHIP );
  }

  /**
   * 
   * */
  public void init_game(){
    add_button_game(140,85,"+",   0,  ButtonAction.ZOOMIN);
    add_button_game(150,85,"-",   1,  ButtonAction.ZOOMOUT);
    add_button_game(10,85,"<",   2,  ButtonAction.MAINMENU);
    add_button_game(150,5,"S",   3,  ButtonAction.DROPDOWN);
    // add_button(0,0,"Console"     ,6   ,ButtonAction.CONSOLE1);
    // add_button(0,0,"Console",   6,  ButtonAction.CONSOLE);
    add_button(110,85,"Enemy view",4   ,ButtonAction.VIEW);
    add_button(160,76,"READY",5   ,ButtonAction.READY_P);

    add_label(160,70,"NETWORK"      ,0 , LabelType.NETWORK );
    add_label(10,10,"Player 1"      ,1 , LabelType.GAME1 );
    add_label(10,20,""      ,2 , LabelType.SCORE1 );
    add_label(10,30,"Server"      ,3 , LabelType.TYPE );

    add_label(10,50,""      ,4 , LabelType.ROUND );

    add_label(10,70,"Player 2"      ,5 , LabelType.GAME2 );
    add_label(10,80,""      ,6 , LabelType.SCORE2 );
    add_label(10,90,"Client"      ,7 , LabelType.TYPE );
    add_label(160,60,"Player 2 is back in menu"      ,8 , LabelType.AFK_NOTICE );
    add_label(160,50,"shipchooser"      ,9 , LabelType.SHIPCHOOSER );
    add_label(160,40,"shots_count"      ,10 , LabelType.SHOTS );
    p1 = new Player(0, parent.graphPane.scl);

    
    ///MOCK BATTLE///
    // try_connect();
  }
  /**
   * 
   * */
  public void init_input(){
    add_button_game(10,85,"<",   0,  ButtonAction.MAINMENU);
    // add_label(60,30,"Type the ip address you would like to use (you might need to turn off you fire wall to connect to outside internet)"      ,0 , LabelType.SELCUS );
    add_label(80,40,""      ,0 , LabelType.INPUT );

  }
  /**
   * 
   * */
  public void init_net(){
    add_label( 70,10,"NO NETWORK SELECTED"      ,0 , LabelType.STATUS );
    add_label( 100,75,""      ,1 , LabelType.IP );
    add_label( 100,85,""      ,2 , LabelType.SOCK );

    add_label(40,90,"NETWORK"      ,0 , LabelType.NETWORK );
    add_button_net(70,20,"Host Server"  ,0   ,ButtonAction.SELSERVER);
    add_button_net(70,30,"Connect Server "  ,1   ,ButtonAction.SELCLI);
    add_button_net(70,40,"Change Socket"  ,2   ,ButtonAction.SELCUS);
    add_button_net(70,50,"Change Server IP"  ,3   ,ButtonAction.SELSOC);
    add_button_net(70,60,"Main Menu"  ,4   ,ButtonAction.MAINMENU);
    
  }
  public void try_connect (){
    p2 = new Player(1,parent.graphPane.scl);
    // if p2p agrees
    player2=true;
  }
  /**
   * 
   * */
  public void init_drop_down(){
    add_button(120,10,"Auto Zoom: On",   0,  ButtonAction.AUTOZOOM);
    add_button_game(150,5,"S",   1,  ButtonAction.BACK);
    add_label(10,15,"Ready Player 1"      ,0 , LabelType.PLAYER1 );
    add_label(10,25,"Ready Player 2"      ,1 , LabelType.PLAYER2 );
  }
  // public int serv=-1; // -1:not connected, 0:client, 1:server
  public int sock = 5019;
  public String ip = "localhost";
  public Player p1;
  public Player p2;
  public boolean p2_ready=false;
  public boolean p1_ready=false;
  public boolean p2_menu=true;
  public int s1=0;
  public int s2=0;

  public int round=0; 
  public boolean my_turn=true;
  boolean player2 = false;
  public void add_label(int a,int b, String str ,int indx,LabelType lbfunc){
    layer_label.add(new Dslb(a,b,20,6,str,indx,lbfunc));
  }
  /**
   * 
   * */
  public void add_button(int a,int b, String str ,int indx, ButtonAction btnact){
    
    layer_buttons.add(new Cdnt(a,b,20,6,str,indx,btnact));
  }
  /**
   * 
   * */
  public void add_button_net(int a,int b, String str ,int indx, ButtonAction btnact){
    
    layer_buttons.add(new Cdnt(a,b,30,6,str,indx,btnact));
  }
  /**
   * 
   * */
  public void add_button_game(int a,int b, String str ,int indx, ButtonAction btnact){
    
    layer_buttons.add(new Cdnt(a,b,6,6,str,indx,btnact));
  }
  /**
   * 
   * */
  public int serv=-1; // -1:not connected, 0:client, 1:server
  /**
   * 
   * */
  public boolean peer =false;
  /**
   * 
   * */
  public ArrayList<Dslb> layer_label;
  public ArrayList<ArrayList<Dslb>> all_labels = new ArrayList<ArrayList<Dslb>>();
  public boolean mouse_in_frame = false;
  /**
   * 
   * */
  public int bColor=0;
  /**
   * 
   * */
  public int resx;
  /**
   * 
   * */
  public int resy;
  public int scl ;
    /**
   * 
   * */
  public int fps = 33;
  /**
   * 
   * */
  public int ticker=0;
    /**
   * 
   * */
  public boolean playing = true;

  /**
   * 
   * */
  public Battle parent;
    /**
   * 
   * */
  public int active_layer = 0;
    /**
   * 
   * */
  // public Rectangle[] ;
  public ArrayList<ArrayList<Cdnt>> all_layers = new ArrayList<ArrayList<Cdnt>>();
  public ArrayList<Cdnt> layer_buttons;
   /**
   * 
   * */
  public int post=0;
}
/**
 * Class container that keeps all information needed for a label
 * */
class Dslb{
  int index;
  int a;
  int b;
  int x;
  int y;
  String str;
  int alpha = 120;
  int blink_factor = 1;
  LabelType func;
  Color cl1 = new Color(255,255,255);     // Deactivate color
  Color cl2 = new Color(0,128,0);         // Give blue when selected 
  Color cl3 = new Color(0,0,0);           // Idle color
  Color cl4 = new Color(128,128,128);     // Hover color
  boolean state = false;
  int pred;
  public Dslb(int inta, int intb, int intx, int inty, String instr, int ind, LabelType fun){
    index = ind;
    a = inta;
    b = intb;
    x = intx;
    y = inty;
    str = instr;
    func=fun;
  }
}

/**
 * Coordinates container class,
 * contains label
 * 
 * */
class Cdnt{
  int index;
  int a;
  int b;
  int x;
  int y;
  ButtonAction func;
  String str;
  Color cl1 = new Color(255,255,255);     // Deactivate color
  Color cl2 = new Color(0,0,128);         // Give blue when selected 
  Color cl3 = new Color(0,0,0);           // Idle color
  Color cl4 = new Color(128,128,128);     // Hover color
  boolean state = false;
  boolean hover = false;
  int pred = 1;
  public Cdnt (int inta, int intb, int intx, int inty, String instr, int ind, ButtonAction fun){
    index = ind;
    a = inta;
    b = intb;
    x = intx;
    y = inty;
    str = instr;
    func = fun;
  }
}