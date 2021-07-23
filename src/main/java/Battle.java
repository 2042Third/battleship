
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.Box;
import java.io.File;
import java.util.ArrayList;
import java.nio.file.*;
import java.util.*;
import java.io.*;
import javax.swing.filechooser.FileFilter;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
* Homework 3
* 
* Battle class handles displays GUI, handles network, and call graphics.
* @author Yi Yang
* @version rev. 1 17/7/2021
 */

public class Battle extends JFrame
{
  private static String sp = File.separator;

  /**
   * Contains the absolute working director
   * */
  private static String curDir = System.getProperty("user.dir")+sp+".."+sp;

  /**
   * Contains the resources file path
   * */ 
  private static String config_dir = "src"+sp+"main"+sp+"resources";

  /**
   * Contains the source code files path
   * */
  private static String src_dir_sub = "src"+sp+"main"+sp+"java";
  /**
   * Graphics object
   * */
  public Render graphPane;

  /**
   * Good looking icon
   * */
  ImageIcon icon_img;

  /**
   * Reads a file on a given path
   * @param dir
   * @throws IOException when the given file is not correct
   * */
  public BufferedReader rd_this (String dir)throws IOException, FileNotFoundException
  {
    String[] line;
    File seed_file = new File(dir);
    FileReader seed_rd = new FileReader(seed_file);
    BufferedReader seed_buff = new BufferedReader(seed_rd);
    return seed_buff;
  }

  /**
   * Reads and sets the configuration file "config.battle"
   * @return true if configuration is set, false otherwise
   * */
  private void rd_config()
  {
    String rd_line;
    BufferedReader cf;
    // cf = rd_this(curDir+config_dir);
    try{
      cf = rd_this(curDir+config_dir+sp+"config.battle");
    }
    catch (Exception e){
      System.out.println("ERR:A1, falling back to default.");
      return;
    }
    for (int i=0 ; i<config_enum;i++){
      try{
        rd_line=cf.readLine();
      }
      catch (Exception e){
        System.out.println("ERR:A2, falling back to default.");
        return;
      }
      switch (i){
        case 0:
          if (rd_line.length() != 0){
            try{
              String res[];
              res = rd_line.split(",");
              screenWidth = Integer.parseInt(res[0]);
              screenHeight= Integer.parseInt(res[1]);
            }
            catch (Exception e){
              System.out.println("ERR:B1, Setting back to using half usr screen res");
              Toolkit kit = Toolkit.getDefaultToolkit();
              Dimension screenSize = kit.getScreenSize();
              screenHeight = screenSize.height/2;
              screenWidth = screenSize.width/2;
            }
          }
          break;
      }
    }

  }

  /**
   * Initializes all required components of the game
   * */
  public void initComponents(){
    ///CABINET///
    cabinet = new Fridge();
    cabinet.parent = this;
    ///LOGIC///
    logic = new Logic(this);
    // add(new InteractLogic());
    logicThread = new Thread(logic);
    ///GRAPHICS///
    graphPane=new Render(this);
    graphPane.set_res(screenWidth,screenHeight);
    graphPane.set_parent(this);
    graphPane.repaint();
    graphPane.display_debug_info();
    graphThread = new Thread(graphPane);
    // graphThread.start();
    graphPane.button_scl();
    ///PANEL///
    panel = new JPanel();

    panel.setOpaque(false);
    ///LISTENER///
    int_logic = new InteractLogic();
    key_listen = new InteractKeyboard(); 
    graphPane.addMouseListener(int_logic);
    graphPane.addMouseMotionListener(int_logic);
    
    this.addKeyListener(key_listen); 

    ///ADDING THEM TOGETHER///
    this.add(graphPane);

    ///LAYER STITCHING///
    cabinet.all_layers.add(new ArrayList<Cdnt>()); // 0, main menu
    cabinet.all_labels.add(new ArrayList<Dslb>()); // 0, main menu
    cabinet.all_layers.add(new ArrayList<Cdnt>()); // 1, Settings
    cabinet.all_labels.add(new ArrayList<Dslb>()); // 0, main menu
    cabinet.all_layers.add(new ArrayList<Cdnt>()); // 2, Network
    cabinet.all_labels.add(new ArrayList<Dslb>()); // 0, main menu
    cabinet.all_layers.add(new ArrayList<Cdnt>()); // 3, Game
    cabinet.all_labels.add(new ArrayList<Dslb>()); // 0, main menu
    cabinet.all_layers.add(new ArrayList<Cdnt>()); // 4, Input Screen
    cabinet.all_labels.add(new ArrayList<Dslb>()); // 0, input


    ///CABINET INIT///

    cabinet.layer_buttons = cabinet.all_layers.get(0);
    cabinet.layer_label = cabinet.all_labels.get(0);
    cabinet.init();
    cabinet.layer_buttons = cabinet.all_layers.get(1);
    cabinet.layer_label = cabinet.all_labels.get(1);
    cabinet.init_drop_down();
    cabinet.layer_buttons = cabinet.all_layers.get(2);
    cabinet.layer_label = cabinet.all_labels.get(2);
    cabinet.init_net();
    cabinet.layer_buttons = cabinet.all_layers.get(3);
    cabinet.layer_label = cabinet.all_labels.get(3);
    cabinet.init_game();
    cabinet.layer_buttons = cabinet.all_layers.get(4);
    cabinet.layer_label = cabinet.all_labels.get(4);
    cabinet.init_input();

    ///DEFAULT MENU///
    cabinet.layer_buttons = cabinet.all_layers.get(0);
    cabinet.layer_label = cabinet.all_labels.get(0);

    ///NETWORK///
    
    // graphThread.start();
    // net_thread.start();
    logic.set_parent(this);
    // logicThread.start();
    ///STARTING THREADS///
    // Runnable r = new Runnable() {
    //      public void run() {
    //          runYourBackgroundTaskHere();
    //      }
    //  };
    synchronized (ls){
      for (int i=0 ;i< cabinet.p1.gd.size;i++){
          ls.add(new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)));
          // ls.add(Collections.synchronizedList(new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0))));

        }
      }

    //  ExecutorService executor = Executors.newCachedThreadPool();
     executor.submit(graphThread);
     // executor.submit(net_thread);
     executor.submit(logicThread);
     
  }

  /**
   * Current layer is copied to the container, and switch to another layer.
   * */
  public void switch_layer (int a){
    // cabinet.all_layers.set(last_layer, cabinet.layer_buttons);
    // System.out.println("Switch: "+cabinet.layer_label.get(0).pred + " to "+);
    cabinet.layer_buttons = cabinet.all_layers.get(a);
    cabinet.layer_label =cabinet.all_labels.get(a);
    if(a==1) dropdown_from = last_layer;
    // cabinet.layer_buttons.get(0).pred = last_layer;
    last_layer = a;
  }

  /**
  * Starts the gui application
  */
  public void Battle()
  {
    
    
    rd_config();
    setTitle("Battleship");
    setSize(screenWidth, screenHeight);
    setLocationByPlatform(true);
    String currentDir = System.getProperty("user.dir");
    initComponents();
    try{
      icon_img = new Battle().getImage("icon.png");
    }
    catch(Exception e){
      e.printStackTrace();
    }
    setIconImage(icon_img.getImage()); 
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
    } catch (Exception e) {
      System.out.println("No good window start! theme ");
    }
  }


  /**
   * Gets the Fridge
   * 
   * */
  public Fridge get_info (){
    return cabinet;
  }

  /**
   * Set fps
   * */
  public void set_fps(int a){
    cabinet.fps = a;
  }

   /**
    * Credit: https://coderanch.com/t/569491/java/images-jar-file
    * Takes a relative path to get a ImageIcon
    * @param path - file path of the system to a image
    * 
    * */
  private  ImageIcon getImage(String path){
 
  // System.out.println(path);
  URL  imgURL = getClass().getResource(path);
  System.out.println(imgURL);
      if (imgURL != null) {
          return new ImageIcon(imgURL);
      } 
      else {
          System.err.println("Couldn't find file: " + path+"\n");
          return null;
      }
  }
  /**
   * 
   * */
  public ExecutorService executor = Executors.newCachedThreadPool();

  /**
   * 
   * */
  public P2P net_manager;
  public  Thread net_thread;
  /**
   * 
   * */
  public InteractLogic int_logic;
  /**
   * 
   * */
  Thread logicThread;
  /**
   * 
   * */
  P2P net_serv;
  /**
   * 
   * */
  public boolean taking_str=false;
  /**
   * 
   * */
  public String t_str="";
  /**
   * 
   * */
  public boolean inip=false;
  /**
   * 
   * */
  public String info="";
  /**
   * 
   * */
  Thread net_serv_thread;

  /**
   * 
   * */
  public Logic logic;
  public List<ArrayList<Integer>> ls = Collections.synchronizedList(new ArrayList<ArrayList<Integer>>());

  /**
   * 
   * */
  Thread graphThread;
    /**
   * Screen height
   * */
  public int screenHeight = 1080;
  /**
   * Screen width
   * */
  public int screenWidth = 1920;

  public int dropdown_from=0;
  public Battle parent = this;

  public Fridge cabinet;

  public String key_input = "";
  public boolean conso = false;
  public InteractKeyboard key_listen;
  /**
   * 
   * */
  Set<String> first_grid = new HashSet<String> (); 
  
  class InteractKeyboard implements KeyListener
  {
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
      String key_input = e.getKeyText(e.getKeyCode());
      // System.out.println(key_input);
      if(key_input.equals("Enter")){
        parent.taking_str=false;
        // parent.cabinet.
        // System.out.println("Input addr");
        if(conso){
          t_str=t_str.toLowerCase(); 
          String[] tmp_=t_str.split("=");

          switch (tmp_[0]){
            case "antialias":
              switch (tmp_[1]){
                case "on":
                  parent.graphPane.antialias_view = true;
                case "off":
                  parent.graphPane.antialias_view = false;
              }
            case "resx":
              parent.graphPane.resx = Integer.parseInt(String.valueOf(tmp_[1]));
              parent.graphPane.adjust_scl ();
            case "resy":
              parent.graphPane.resy = Integer.parseInt(String.valueOf(tmp_[1]));
              parent.graphPane.adjust_scl ();
            case "grid_size":
              int tp_int = Integer.parseInt(String.valueOf(tmp_[1]));
              parent.graphPane.grid_size=tp_int;
              parent.cabinet.p1.gd=new Grid(tp_int);
              parent.cabinet.p2.gd=new Grid(tp_int);
            case "reset_battle_p1":
              parent.cabinet.p1.shps=new ArrayList<ArrayList<Ele>>();
            case "reset_battle_p2":
              parent.cabinet.p2.shps=new ArrayList<ArrayList<Ele>>();
            case "loser_message":
              parent.graphPane.ending=tmp_[1];
            case "winnner_message":
              parent.graphPane.ending=tmp_[1];
            case "get_stuck":
              parent.cabinet.playing=false;
            case "cheat_infinit_ammo_on":
              parent.logic.shots=99999999;
            case "cheat_infinit_ammo_off":
              parent.logic.shots=0;
            case "cheat_i_win":
              parent.cabinet.s1=15;
            case "cheat_win_cond":
              int tp_int111 = Integer.parseInt(String.valueOf(tmp_[1]));
              parent.graphPane.win_cond=tp_int111;
            case "cheat_respawn":
              String[] tmp_2=t_str.split(",");
              parent.cabinet.p1.shps.get(Integer.parseInt(String.valueOf(tmp_2[0]))).get(Integer.parseInt(String.valueOf(tmp_2[1]))).tp=1;
            case "replay_again":
              parent.cabinet.s1=0;
              parent.cabinet.s2=0;

          }
          parent.conso=false;
          parent.switch_layer(0);
        }
        else if(!parent.inip)parent.cabinet.ip =parent.t_str;
        else parent.cabinet.sock = Integer.parseInt(String.valueOf(parent.t_str));
        parent.t_str="\"Change Saved! You may exit this screen\"";
        parent.switch_layer(2);
        // parent.graphPane.clutch_switch(1,4,15);
      }
      else if(key_input.equals("Period")){
        key_input=".";
      }
      else if(key_input.equals("Equals")){
        key_input="=";
      }
      else if(key_input.equals("Comma")){
        key_input=",";
      }
      else if(key_input.equals("Space")){
        key_input=" ";
      }
      else if(key_input.equals("Shift")){
        key_input="";
      }
      else if(key_input.equals("Minus")){
        key_input="_";
      }
      else if(key_input.equals("Backspace")){
        // parent.t_str=parent.t_str+".";
        key_input="";
        String tmp="";
        int asd=t_str.length();
        int ads=1;
        for(char i : t_str.toCharArray()){
          if(ads==asd)break;
          tmp+=i;
          ads++;
        }
        t_str=tmp;
      }
      if(parent.taking_str)parent.t_str=parent.t_str+key_input;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
  }
  class InteractLogic implements MouseListener, MouseMotionListener {

    BufferedImage bi;
    Graphics2D big;
    // Holds the coordinates of the user's last mousePressed event.
    int last_x, last_y;
    boolean firstTime = true;
    TexturePaint fillPolka, strokePolka;
    // True if the user pressed, dragged or released the mouse outside of the rectangle; false otherwise.
    boolean pressOut = false;
    
   

    // Handles the event of the user pressing down the mouse button.
    public void mousePressed(MouseEvent e) {

      buttons_check(e.getX(),e.getY(),MouseMove.PRESS);
      if(last_layer==3){
        if(parent.graphPane.this_view )battle_check(e.getX(),e.getY(),MouseMove.PRESS);

        else if(parent.cabinet.player2 && !parent.graphPane.this_view){
          battle_check_p2(e.getX(),e.getY(),MouseMove.PRESS);
        }
      }
    }

    // Handles the event of a user dragging the mouse while holding down the
    // mouse button.
    public void mouseDragged(MouseEvent e) {
      // if(last_layer==3)battle_check(e.getX(),e.getY(),MouseMove.DRAG);
      buttons_check(e.getX(),e.getY(),MouseMove.DRAG);
    }

    // Handles the event of a user releasing the mouse button.
    public void mouseReleased(MouseEvent e) {

      // if(last_layer==3)battle_check(e.getX(),e.getY(),MouseMove.RELEASE);
      buttons_check(e.getX(),e.getY(),MouseMove.RELEASE);

    }
    // This method required by MouseListener.

    public void mouseMoved(MouseEvent e) {
      if(last_layer==3){

        if(parent.graphPane.this_view)battle_check(e.getX(),e.getY(),MouseMove.HOVER);
        else if(parent.cabinet.player2 && !parent.graphPane.this_view){
          battle_check_p2(e.getX(),e.getY(),MouseMove.HOVER);
        }
      }
      if(cabinet.mouse_in_frame)buttons_check(e.getX(),e.getY(),MouseMove.HOVER);
    }

    // These methods are required by MouseMotionListener.
    public void mouseClicked(MouseEvent e) {
      // cabinet.bColor += 50 ;
      buttons_check(e.getX(),e.getY(),MouseMove.CLICK);
    }

    public void mouseExited(MouseEvent e) {
      cabinet.mouse_in_frame = false;
    }

    public void mouseEntered(MouseEvent e) {
      cabinet.mouse_in_frame = true;
    }

    // Updates the coordinates representing the location of the current rectangle.
    public void updateLocation(MouseEvent e) {
      
    }

    public void buttons_check (int mx , int my,MouseMove mm){
      int sclx = mx/graphPane.bscl;
      int scly = my/graphPane.bscl;
      int scl = graphPane.bscl;
      switch (mm){
        case PRESS:
          for (Cdnt i:cabinet.layer_buttons){
            if(check_button_click(sclx,scly,i,scl)){
              i.state=true;
              break;
              
            }
          }
          break;
        case CLICK:
          break;
        case DRAG:
          break;
        case HOVER:
          for (Cdnt i:cabinet.layer_buttons){
            if(check_button_click(sclx,scly,i,scl)){
              i.hover=true;
              break;
            }
            else{
              i.hover=false;
              // break;
            }
          }
          break;
        case RELEASE:
          for (Cdnt i:cabinet.layer_buttons){
            if(check_button_click(sclx,scly,i,scl)){
              i.state=false;
              cabinet.post = i.index;
              Thread post_thread = new Thread(new WhatButtonAction(parent,i.index));
              // post_thread.start();
              executor.submit(post_thread);
              break;
            }
          }
          break;
      }
    }

  }
    /**
     * 
     * */
    public Thread battle_watch;

  public void battle_check (int mx , int my,MouseMove mm){
    battle_max = parent.graphPane.gd_scl*parent.graphPane.grid_size+10+parent.graphPane.cur_offset_x;
    battle_may = parent.graphPane.gd_scl*parent.graphPane.grid_size+10+parent.graphPane.cur_offset_y;
    if (mx > battle_max || my > battle_may) return;
    gd_offy = parent.graphPane.gd_scl+10+parent.graphPane.cur_offset_y;
    gd_offx = parent.graphPane.gd_scl+10+parent.graphPane.cur_offset_x;
    if (my- gd_offy<0 || mx- gd_offx<0) return;
    gdx = (int)Math.floor((mx- gd_offx)/parent.graphPane.gd_scl);
    gdy = (int)Math.floor((my- gd_offy)/parent.graphPane.gd_scl);
    if(parent.graphPane.grid_size==0)return;
    if(gdx>=parent.graphPane.grid_size-1 || gdy>=parent.graphPane.grid_size-1){
      return;
    }
    switch (mm){
      case PRESS:
        if ( parent.logic.ship_count<5 && parent.cabinet.p1.shps.get(gdx).get(gdy).tp==0){
          if(parent.logic.ship_track_choosen == parent.logic.ship_track){
            if(first_grid.contains(""+gdx+""+gdy)&& (parent.logic.ship_track==2 || parent.logic.ship_track==1)){
            first_grid.add(""+(gdx+1)+""+(gdy));
            first_grid.add(""+(gdx)+""+(gdy-1));
            first_grid.add(""+(gdx-1)+""+(gdy));
            first_grid.add(""+(gdx)+""+(gdy+1));
              parent.cabinet.p1.shps.get(gdx).get(gdy).show = true;
              parent.cabinet.p1.shps.get(gdx).get(gdy).tp = 1;
              parent.logic.ship_track_choosen++;
            }
            parent.logic.ship_track--;
            parent.logic.ship_track_choosen=0;
            parent.logic.ship_count++;
            first_grid = new HashSet<String> (); 
          }
          else if(parent.logic.ship_track_choosen==0 || first_grid.contains(""+gdx+""+gdy)){
            first_grid.add(""+(gdx+1)+""+(gdy));
            first_grid.add(""+(gdx)+""+(gdy-1));
            first_grid.add(""+(gdx-1)+""+(gdy));
            first_grid.add(""+(gdx)+""+(gdy+1));
            parent.cabinet.p1.shps.get(gdx).get(gdy).show = true;
            parent.cabinet.p1.shps.get(gdx).get(gdy).tp = 1;
            parent.logic.ship_track_choosen++;
          }
        }
        else if(parent.logic.ship_count==5){

          if (parent.cabinet.my_turn)
            synchronized(parent.info){parent.info=parent.info+",MYTURN";
                    parent.logic.p1_r=true;
                  }
          else
            synchronized(parent.info){parent.info=parent.info+",YOURTURN";
                    parent.logic.p1_r=true;}
        }

        break;
      case CLICK:
        break;
      case DRAG:
        break;
      case HOVER:
        // System.out.println("x"+gdx+" y"+gdy);
        if ((nowx != gdx ||nowy != gdy  )){
          parent.cabinet.p1.shps.get(nowx).get(nowy).show = false;
        }
        else if(parent.logic.ship_count<5){
          if(parent.logic.ship_track_choosen==0){
              parent.cabinet.p1.shps.get(gdx).get(gdy).show = true;
          }
          else if (first_grid.contains(""+gdx+gdy)){

              parent.cabinet.p1.shps.get(gdx).get(gdy).show = true;
          }
        }
        break;
      case RELEASE:
        break;

      }

        nowx = gdx;
        nowy = gdy;
    }
  public void battle_check_p2 (int mx , int my,MouseMove mm){
    battle_max = parent.graphPane.gd_scl*parent.graphPane.grid_size+10+parent.graphPane.cur_offset_x;
    battle_may = parent.graphPane.gd_scl*parent.graphPane.grid_size+10;

    if (mx > battle_max || my > battle_may) return;
    gd_offy = parent.graphPane.gd_scl+10;
    gd_offx = parent.graphPane.gd_scl+10+parent.graphPane.cur_offset_x;
    if (my- gd_offy<0 || mx- gd_offx<0) return;
    gdx = (int)Math.floor((mx- gd_offx)/parent.graphPane.gd_scl);
    gdy = (int)Math.floor((my- gd_offy)/parent.graphPane.gd_scl);
    if(parent.graphPane.grid_size==0)return;
    if(gdx>=parent.graphPane.grid_size-1 || gdy>=parent.graphPane.grid_size-1){
      return;
    }
    // gdy+=battle_may;
    switch (mm){
      case PRESS:
        if(parent.logic.shots>=0 && parent.logic.p1_r && parent.logic.p2_r && parent.cabinet.p2.shps.get(gdx).get(gdy).tp <2){
          parent.cabinet.p2.shps.get(gdx).get(gdy).show = true;
          parent.cabinet.p2.shps.get(gdx).get(gdy).tp = 3;
          synchronized (ls){ls.get(gdx).set( gdy,1);}
          parent.logic.shots--;
        }
        else if (parent.logic.shots ==-1){
          // parent.logic.p1_r = false;
          parent.cabinet.my_turn=false;
          synchronized(parent.info){parent.info=parent.info+",YOURTURN";}
        }
        break;
      case CLICK:
        break;
      case DRAG:
        break;
      case HOVER:
        // System.out.println("x"+gdx+" y"+gdy);
        if ((nowx != gdx ||nowy != gdy  )){
          parent.cabinet.p2.shps.get(nowx).get(nowy).show = false;
        }
        else if (parent.cabinet.my_turn){
          parent.cabinet.p2.shps.get(gdx).get(gdy).show = true;
        }
        break;
      case RELEASE:
        break;

      }

        nowx = gdx;
        nowy = gdy;
    }

    public boolean check_button_click(int mx, int my, Cdnt i,int scl){
      if (show_telemetry){
        System.out.printf("mx %d my %d i.index %d scl %d\n",mx,my,i.index,scl);
        System.out.printf("xx %d xx %d xxxxxxx %d xxx %d\n",i.a,(i.x+i.a) ,i.b,(i.y+i.b));
      }
      if(mx>i.a&& mx<(i.x+i.a) &&my>i.b&& my<(i.y+i.b)){
        return true;
      }  
      else {
        return false;
      }
    }

  int battle_max ;
  int battle_may ;
  int gd_offy ;
  int gd_offx ;
  int gdx ;
  int gdy ;
  int nowx=0;
  int nowy=0;
  int nowx2=0;
  int nowy2=0;
  public boolean show_telemetry = false;
  private JPanel panel;
  public int last_layer = 0;
  private static int config_enum = 1;
}


/**
 * DIY action listener 
 * */
class WhatButtonAction implements Runnable{
  public WhatButtonAction(Battle a,int b){
    parent = a;
    run(b);
  }
  public void run(){

  }
  // Doing this way isn't the best, but I don't time to change it anymore
  public void run (int b){
    Cdnt button = parent.cabinet.layer_buttons.get(b);
    // System.out.printf("WhatButtonAction for %s\n",button.str);
    switch (button.func){
      case START:
        synchronized(parent.info){parent.info=parent.info+",START";}
        parent.graphPane.clutch_switch(3,parent.last_layer,15);
        parent.graphPane.match_start=true;
        parent.switch_layer(3);
        break;
      case CONSOLE:
        parent.t_str="";
        parent.graphPane.menu = false;
        // parent.graphPane.clutch_switch(4,1,15);
        parent.taking_str=true;
        parent.conso=true;
        // parent.cabinet.layer_label.get(3).str = "console_mode";
        // parent.inip=true;
        parent.switch_layer(4);

        break;
      case CONSOLE1:
        parent.t_str="";
        parent.graphPane.menu = false;
        // parent.graphPane.clutch_switch(4,3,15);
        parent.taking_str=true;
        parent.conso=true;
        parent.switch_layer(4);

        // parent.cabinet.layer_label.get(3).str = "console_mode";
        // parent.inip=true;
        break;
      case NETWORK:
        parent.graphPane.clutch_switch(2,parent.last_layer,15);
        parent.graphPane.menu = parent.graphPane.menu ? false:true;
        parent.graphPane.blurry = parent.graphPane.menu;
        // parent.graphPane.match_start=true;
        parent.switch_layer(2);
        break;
      case EXIT:
        System.exit(0);
        break;
      case ZOOMIN:
        parent.graphPane.menu = false;
        parent.graphPane.inc_size();
        parent.graphPane.inc_size();
        break;
      case ZOOMOUT:
        parent.graphPane.menu = false;
        parent.graphPane.dec_size();
        parent.graphPane.dec_size();
        // parent.graphPane.gearing;
        break;
      case VIEW:
        parent.graphPane.this_view = parent.graphPane.this_view ? false:true;
        // parent.graphPane.gearing;
        break;
      case AUTOZOOM:
        button.str = (button.str =="Auto Zoom: On")? "Auto Zoom: off":"Auto Zoom: On";
        parent.graphPane.autozoom = (button.str =="Auto Zoom: On")? true:false;
        // parent.graphPane.gearing;
        break;

      case SELSERVER:
        parent.net_serv = new Serv();
        parent.net_serv.set_parent(parent);
        parent.net_serv_thread = new Thread(parent.net_serv);
         parent.executor.submit(parent.net_serv_thread);
         parent.cabinet.layer_label.get(0).str="Waiting for client to join...";
        // parent.net_manager.start_listen();
        break;
      case SELCLI:
        parent.t_str="";
        parent.cabinet.my_turn = false;
        parent.net_manager = new P2P();
        parent.net_manager.set_parent(parent);
        parent.net_thread = new Thread(parent.net_manager);
         parent.cabinet.layer_label.get(0).str="Waiting for Server...";
         parent.executor.submit(parent.net_thread);
        // parent.net_manager.start_sending();
        break;
      case SELSOC:
        
        // System.out.println("In back");
        parent.t_str="";
        parent.graphPane.menu = false;
        parent.graphPane.clutch_switch(4,1,15);
        parent.taking_str=true;
        parent.inip=false;
        parent.switch_layer(4);


        break;
      case SELCUS:
        parent.t_str="";
        parent.graphPane.menu = false;
        parent.graphPane.clutch_switch(4,1,15);
        parent.taking_str=true;
        parent.inip=true;
        parent.switch_layer(4);


        break;
      case READY_P:
        // parent.net_manager.start
        parent.cabinet.p1_ready=true;
        synchronized(parent.info){parent.info=parent.info+",READY";}
        break;
      case BACK:
        // System.out.println("In back");
        parent.graphPane.menu = false;
        parent.graphPane.clutch_switch(parent.dropdown_from,3,15);
        parent.switch_layer(parent.dropdown_from);
        break;
      case MAINMENU:
        parent.cabinet.p1_ready=false;
        // System.out.println("In main menu");
        synchronized(parent.info){parent.info=parent.info+",BACK";}
        parent.graphPane.menu =false;
        parent.graphPane.match_start=false;
        parent.graphPane.clutch_switch(0,3,15);
        parent.switch_layer(0);
        break;

      case DROPDOWN:
        // drop_tmp = ;
        // System.out.println("In dropdown");
        parent.graphPane.menu = parent.graphPane.menu ? false:true;
        parent.graphPane.blurry = parent.graphPane.menu;

        parent.switch_layer(1);

        break;
    }
  }
  public Thread drop_down_thread;
  public Runnable drop_tmp;
  public Battle parent; 
}
/**
 * DIY action listener 
 * */
class WhatBattleAction implements Runnable{
  public WhatBattleAction(Battle a,int bd){
    parent = a;
    b =bd;
  }

  // Doing this way isn't the best, but I don't time to change it anymore
  public void run (){
    Cdnt button = parent.cabinet.layer_buttons.get(b);
    // System.out.printf("WhatButtonAction for %s\n",button.str);
    switch (button.func){
      case START:
        parent.graphPane.clutch_switch(3,parent.last_layer,15);
        parent.graphPane.match_start=true;
        parent.switch_layer(3);
        break;

    }
  }
  public int b=0;
  public Thread drop_down_thread;
  public Runnable drop_tmp;
  public Battle parent; 
}
// A seperate thread for keeping the animation of the drop down menu consistent
class AnimatedDrop implements Runnable {
  

  public AnimatedDrop(Battle b,int a){
    parent = b;
    // run(a);

  }
  public void run(){
    
      System.out.println(current_tick);
  } 
  public void run(int a){
    // parent = b;
    int start_tick = parent.cabinet.ticker;
    for (Cdnt i: parent.cabinet.layer_buttons){
      i.b = basic_offset;
    }
    while (total_tick>parent.cabinet.ticker-start_tick){
      current_tick +=1;
      System.out.println(current_tick);
      for (Cdnt i: parent.cabinet.layer_buttons){
        i.b = i.b + per_tick_inc;
        pos +=per_tick_inc;
      }
      // parent.graphPane.repaint();
      try{
          Thread.sleep((int)16);    // This is faster than the Logic class because the above operation take time
          }
      catch(InterruptedException e){
        System.out.println("Tick sleep interapted.");
      }
    }
    // if(pos!=basic_offset){
    //   for (Cdnt i: parent.cabinet.all_layers.get(a)){
    //     i.b += basic_offset-pos;
    //   }
    // }
  }

  public Battle parent;
  public int pos=0;
  public int total_tick = 15;
  private int current_tick = 0;
  public int basic_offset = -50;
  public int per_tick_inc = 3;
}

enum MouseMove {
  CLICK,DRAG,PRESS,RELEASE,HOVER
}

