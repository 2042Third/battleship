import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.*;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import javax.swing.event.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
* Homework 3
* 
* Graphics class draws and animates on the given data.
* @author Yi Yang
* @version rev. 1 17/7/2021
 */

public class Render extends JPanel implements Runnable {

    /**
    * default graphics display size for x
    * */
    protected int resx=1280; 
    /**
    * default graphics display size for y
    * */
    protected int resy=720; 
    /**
     * Increases the size of the current display  
     * 
     * */
    public void inc_size(){
      if (resx==0) return;
      scl+=2;
      dot_scl = scl;
      font_scl = scl*3;
      gd_scl = scl;
      parent.cabinet.scl=scl;
    }
        /**
     * Increases the size of the current display  
     * 
     * */
    public void inc_size_scl(){
      if (resx==0) return;
      scl+=1;
      // System.out.printf("scl changed + %d\n",scl);
      dot_scl = scl;
      font_scl = scl*3;
      gd_scl = scl;
      parent.cabinet.scl=scl;
    }
    public void adjust_scl (){
      resx = this.getWidth();
      resy = this.getHeight();
      // inc_size_scl();
      // dec_size_scl();
      button_scl();
      // rescl();
    }
    /**
     * 
     * */
    public int gd_scl=3;
    /**
     * Decrease the size of the current display
     * 
     * */
    public void dec_size(){
      if (resx==0) return;
      scl-=2;
      dot_scl = scl;
      font_scl = scl*3;
      gd_scl = scl;
      parent.cabinet.scl=scl;
    }    
    /**
     * Decrease the size of the current display
     * 
     * */
    public void dec_size_scl(){
      if (resx==0) return;
      scl=scl-1;
      // System.out.printf("scl changed - %d\n",scl);
      dot_scl = scl;
      font_scl = scl*3;
      gd_scl = scl;
      parent.cabinet.scl=scl;
    }
    /**
     *  Set res
     * @param x x screen res
     * @param y y screen res
     * 
     * */
    public void set_res(int x, int y){
      resx= x;
      resy = y;
      area=new Rectangle(resx,resy);
      rescl();
    }
     /**
     * Reused from hw2
     * Draw a dot on the display area at position (x,y) 
     * @param x - x axis position 
     * @param y - y axis position
     * @param g - Graphics instance, also extends Graphics2D 
     * 
     * */
    private void draw_shade(int x, int y, Graphics g){
      g.setColor(DARK_GREEN);
      g.fillRect(x,y, dot_scl, dot_scl);
      g.setColor(bgd);
    }
    /**
     * Reused from hw2
     * Draw a dot on the display area at position (x,y) 
     * @param x - x axis position 
     * @param y - y axis position
     * @param g - Graphics instance, also extends Graphics2D 
     * 
     * */
    private void draw_dot(int x, int y, Graphics g){
      g.setColor(GREEN);
      g.fillOval(x,y, dot_scl, dot_scl);
      g.setColor(bgd);
    }
    /**
     * Reused from hw2
     * Draw a dot on the display area at position (x,y) 
     * @param x - x axis position 
     * @param y - y axis position
     * @param g - Graphics instance, also extends Graphics2D 
     * 
     * */
    private void draw_dot_hit(int x, int y, Graphics g){
      g.setColor(YELLOW);
      g.fillOval(x,y, dot_scl, dot_scl);
      g.setColor(bgd);
    }
    private void draw_line(int x, int y, Graphics g){
      g.setColor(WHITE);
      g.drawLine(x,y, x+gd_scl, y+gd_scl);
      g.setColor(bgd);
    }
    /**
     * Draw a circle on the display area at position (x,y) 
     * @param x - x axis position 
     * @param y - y axis position
     * @param g - Graphics instance, also extends Graphics2D 
     * 
     * */
    private void draw_point_light(int x, int y, Graphics g){
      g.setColor(fgd);
      g.fillOval(x,y, dot_scl*2, dot_scl);
      g.setColor(bgd);
    }
    /**
     * Change fps
     * @param a the target fps
     * */
    public void ch_fps(int a){
      fps = a;
    }

    /**
     * Re-reads the scl and displays accordingly 
     * */
    public void rescl(){
      scl = resy/100;
      dot_scl = scl*10;
      font_scl = scl*3;
      gd_scl = scl*3;
      parent.cabinet.scl=scl;
      // font1 = new Font("Serif", Font.PLAIN, font_scl);
    }

    /**
     * Reused from hw2
     * Draw a dot on the display area at position (x,y) from offset ox and oy
     * @param x - x axis position 
     * @param y - y axis position
     * @param g - Graphics instance, also extends Graphics2D 
     * @param ox - offset of the x axis position
     * @param oy - offset of the y axis position
     * 
     * */
    private void draw_dot(int x, int y, Graphics g, int ox,int oy){
      g.fillOval(x+ox,y+oy, dot_scl, dot_scl);
    }

    /**
     * Button scale reset, only called when resize
     * */
    public int button_tmp_value;
    public void button_scl(){
      button_tmp_value = bscl;
      bscl = (resx+resy)/300;

    }
     /**
     * 
     * */
    public void draw_active_labels (Graphics2D g2){


      // g2.setColor(GREEN);
      g2.setFont(font2);
      for (Dslb i:parent.cabinet.layer_label){
        // g2.drawString(get_str(i.str).getIterator(),i.a*scl+5,i.b*scl+i.y*scl/2);
        
        switch (i.func){
          case NETWORK:
            g2.setColor(parent.logic.net_status);
            g2.drawString(i.str,i.a*bscl,i.b*bscl);
            break;
          case AFK_NOTICE:
            if(!parent.cabinet.peer)return;
            synchronized(parent.cabinet){
              if(parent.cabinet.p2_menu){
                g2.setColor(parent.logic.flash_yellow);
                g2.drawString(i.str,i.a*bscl,i.b*bscl);
              }
            }
            break;
          case PLAYER1:
            g2.setColor(parent.logic.p1_status);
            g2.drawString(i.str,i.a*bscl,i.b*bscl);
            break;
          case GAME1:
            g2.setColor(parent.logic.p1_status);
            g2.drawString(i.str,i.a*bscl,i.b*bscl);
            break;
          case GAME2:
            if(!parent.cabinet.peer)return;
            g2.setColor(parent.logic.p2_status);
            g2.drawString(i.str,i.a*bscl,i.b*bscl);
            break;
          case PLAYER2:
            if(!parent.cabinet.peer)return;
            g2.setColor(parent.logic.p2_status);
            g2.drawString(i.str,i.a*bscl,i.b*bscl);
            break;
          case SHOTS:
            if(!parent.cabinet.my_turn)return;
            g2.setColor(parent.logic.p1_status);
            g2.drawString((1+parent.logic.shots)+" avaliable shots",i.a*bscl,i.b*bscl);
            break;
          case SHIPCHOOSER:
            if(!parent.cabinet.peer || !parent.cabinet.p2_ready)return;
            g2.setColor(WHITE);
            switch(parent.logic.ship_count){
              case 0:
                g2.drawString("Carrier: Choose 5 connected grid",i.a*bscl,i.b*bscl);
                break;
              case 1:
                g2.drawString("Battleship: Choose 4 connected grid",i.a*bscl,i.b*bscl);
                break;
              case 2:
                g2.drawString("Destroyer: Choose 3 connected grid",i.a*bscl,i.b*bscl);
                break;
              case 3:
                g2.drawString("Submarine: Choose 3 connected grid",i.a*bscl,i.b*bscl);
                break;
              case 4:
                g2.drawString("Patrol Boat: Choose 2 connected grid",i.a*bscl,i.b*bscl);
                break;
              case 5:
                if(!parent.logic.p2_r)g2.drawString("Waiting for your enemy",i.a*bscl,i.b*bscl);
                else if(parent.cabinet.my_turn)g2.drawString("Your Turn, go to enemy's grid and fight!",i.a*bscl,i.b*bscl);
                else g2.drawString("Enemy's Turn, wait a bit",i.a*bscl,i.b*bscl);
                break;
            }
            break;

          case SCORE2:
            if(!parent.cabinet.peer)return;
            g2.setColor(parent.logic.p2_status);
            if(parent.cabinet.s1==win_cond)g2.drawString("Score "+parent.cabinet.s2+ending,i.a*bscl,i.b*bscl);
            else if(parent.cabinet.s2==win_cond)g2.drawString("Score "+parent.cabinet.s2+winner_message,i.a*bscl,i.b*bscl);
            else g2.drawString("Score "+parent.cabinet.s2+"",i.a*bscl,i.b*bscl);
            break;
          case SCORE1:
            g2.setColor(parent.logic.p1_status);
            if(parent.cabinet.s2==win_cond)g2.drawString("Score "+parent.cabinet.s2+ending,i.a*bscl,i.b*bscl);
            else if(parent.cabinet.s1==win_cond)g2.drawString("Score "+parent.cabinet.s2+winner_message,i.a*bscl,i.b*bscl);
            else g2.drawString("Score "+parent.cabinet.s2+"",i.a*bscl,i.b*bscl);
            break;
          case ROUND:
            g2.setColor(WHITE);
            g2.drawString("ROUND "+parent.cabinet.round+"",i.a*bscl,i.b*bscl);
            break;
          case STATUS:
            g2.setColor(WHITE);
            g2.drawString("Status: "+i.str,i.a*bscl,i.b*bscl);
            break;
          case IP:
            g2.setColor(WHITE);
            g2.drawString("IP    : "+parent.cabinet.ip,i.a*bscl,i.b*bscl);
            break;
          case SOCK:
            g2.setColor(WHITE);
            g2.drawString("Socket: "+parent.cabinet.sock,i.a*bscl,i.b*bscl);
            break;
          // case SELCUS:
          //   g2.setColor(WHITE);
          //   // g2.drawString("Input the ip address you want to use, then press enter(you might need to turn off you fire wall to connect to outside internet)",i.a*bscl,i.b*bscl);
          //   break;
          // case SELSOC:
          //   g2.setColor(WHITE);
          //   // g2.drawString("Input the ip address you want to use, then press enter",i.a*bscl,i.b*bscl);
          //   break;
          case INPUT:
            if(parent.conso){
              g2.setColor(parent.logic.flash_green);
              g2.setFont(font2);
            g2.drawString(parent.t_str.toLowerCase()+"|",i.a*bscl,i.b*bscl);
            }
            else{
            g2.setColor(parent.logic.flash_white);
            g2.drawString(parent.t_str+"|",i.a*bscl,i.b*bscl);
          }
            break;
          case BATTLESHIP:
            g2.setFont(font3);
            g2.setColor(WHITE);
            g2.drawString("BATTLESHIP",i.a*bscl,i.b*bscl);

            g2.setFont(font2);
            g2.drawString("by Mike Yang",i.a*bscl,i.b*bscl+17);
            break;
        }
        g2.setColor(WHITE);
        g2.setFont(font1);

      }

      g2.setColor(bgd);

    }
    public void button_al (int a, int b){
      
    }
    /**
     * 
     * */
    public void draw_active_button (Graphics2D g2){
      // g2.setColor(fgd);
      if(gearing_step<=0){
        for (Cdnt i:parent.cabinet.layer_buttons){
          if(i.state){
            g2.setColor(i.cl2);
          }else {g2.setColor(i.hover? i.cl1:i.cl4);}
          switch (i.func){
            case ZOOMIN:
              if(true){
                if(i.a<resx/bscl-20){
                  i.a+=1;
                }
                else if (i.a>resx/bscl-20){
                  i.a-=1;
                }
              }
              break;
            case ZOOMOUT:              
              if(true){
                if(i.a<resx/bscl-10){
                  i.a+=1;
                }
                else if (i.a>resx/bscl-10){
                  i.a-=1;
                }
              }
              break;
            case DROPDOWN:
              if(i.index==3 || i.index==1){
                if(i.a<resx/bscl-10){
                  i.a+=1;
                }
                else if (i.a>resx/bscl-10){
                  i.a-=1;
                }
              }
              break;
            case VIEW:
              if(this_view)  
                i.str="Enemy grid";
              else
                i.str="My grid";
              if(i.a<resx/bscl-50){
                i.a+=1;
              }
              else if (i.a>resx/bscl-50){
                i.a-=1;
              }
              break;
            case READY_P:
              if(!parent.cabinet.peer){}
              else if(!parent.cabinet.p1_ready)g2.setColor(parent.logic.flash_yellow);
              else {g2.setColor(GREEN);}

              break;


          }

          g2.fillRect(i.a*bscl, i.b*bscl, i.x*bscl,i.y*bscl);

        }

        g2.setColor(BLACK);
        for (Cdnt i:parent.cabinet.layer_buttons){
          // g2.drawString(get_str(i.str).getIterator(),i.a*scl+5,i.b*scl+i.y*scl/2);
          g2.drawString(i.str,i.a*bscl+5,i.b*bscl+i.y*bscl/2);
        }

        g2.setColor(bgd);
      }
      // else if (gearing_set){
      //       button_offset(g2, parent.cabinet.layer_buttons,1);
      //       button_offset(g2, parent.cabinet.all_layers.get(gear_out),1);
      //   gearing_step -=1;
      // }
      else {
        switch (gear_in){
          case 0:
            button_offset(g2, parent.cabinet.layer_buttons,-1,0);
            break;
          case 3:
            button_offset(g2, parent.cabinet.layer_buttons,0,1);
            break;
        }
        switch (gear_out){
          case 0:
            button_offset(g2, parent.cabinet.all_layers.get(gear_out),1,0);
            break;
          case 3:
            button_offset(g2, parent.cabinet.all_layers.get(gear_out),0,-1);
            break;
        }
        // button_offset(g2, parent.cabinet.all_layers.get(gear_out));
        gearing_set = true;
        gearing_step -=1;
      }

    }

    /**
     * 
     * */
    public void button_offset(Graphics g2,ArrayList<Cdnt> inp,int multix, int multiy){
      gearing_offset_x = gearing_step*multix;
      gearing_offset_y = gearing_step*multiy;
      for (Cdnt i:inp){
        if(i.state){
          g2.setColor(i.cl2);
        }else {g2.setColor(i.hover? i.cl1:i.cl4);}
        g2.fillRect(i.a*bscl+gearing_offset_x, i.b*bscl+gearing_offset_y, i.x*bscl,i.y*bscl);
        // g2.draw(i);
      }

      g2.setColor(BLACK);
      for (Cdnt i:inp){
        // g2.drawString(get_str(i.str).getIterator(),i.a*scl+5,i.b*scl+i.y*scl/2);
        g2.drawString(i.str,i.a*bscl+5+gearing_offset_x,i.b*bscl+i.y*bscl/2+gearing_offset_y);
      }
      // gearing_offset_x =gearing_offset_x/2;
      // gearing_offset_y =gearing_offset_y/2;
      // gearing_offset_x +=multi*1;
      // gearing_offset_y +=multi*1;
      g2.setColor(bgd);
    }

    public Render (Battle a){
      parent = a;

    }

    public void draw_active_ele (Graphics2D g2, Player p, Grid gd){
      g2.setColor(color1);
      // if(match_start && autozoom){
      //   check_grid(gd);
      // }
      int gd_offy = gd_scl+10;
      int gd_offx = gd_scl+10+cur_offset_x;
      int max_y = gd_scl*grid_size+10;
      int max_x = gd_scl*grid_size+10+cur_offset_x;
      // for (int i=0 ; i< grid_size;i++){
      int i=-1;
      int z =-1;
      int x,y;
      for (ArrayList<Ele> lst:p.shps){
        z+=1;
        for (Ele ele : lst){
          i+=1;
          if (i>=grid_size-1 || z>=grid_size-1)break;
          x=(z+1)*gd_scl+10+cur_offset_x;
          y=(i+1)*gd_scl+10+cur_offset_y;
          
            switch (ele.tp){
              case 0:
                if (!ele.show) continue;
                draw_shade(x,y,g2);
                break;
              case 1:
                draw_dot(x,y,g2);
                break;
              case 2:
                // draw_line(x,y,g2);
                draw_dot_hit(x,y,g2);
                // draw_line(x,y,g2);
                break;
              case 3:
                // draw_dot(x,y,g2);
                draw_line(x,y,g2);
                break;
            }
        }
        i=-1;
      }
      if (parent.cabinet.player2){ 
        Player p2=parent.cabinet.p2;
        i=-1;
        z =-1;
        for (ArrayList<Ele> lst:p2.shps){
          z+=1;
          for (Ele ele : lst){
            i+=1;
            if (i>=grid_size-1 || z>=grid_size-1)break;
            x=(z+1)*gd_scl+10+cur_offset_x;
            y=(i+1)*gd_scl+20+cur_offset_y+max_y;
            
            switch (ele.tp){
              case 0:
                if (!ele.show) continue;
                draw_shade(x,y,g2);
                break;
              case 1:
                draw_dot(x,y,g2);
                break;
              case 2:
                // draw_line(x,y,g2);
                draw_dot_hit(x,y,g2);
                // draw_line(x,y,g2);
                break;
              case 3:
                // draw_dot(x,y,g2);
                draw_line(x,y,g2);
                break;
            }
          }
          i=-1;
        }
      }
    }
    /**
     * Draw the grid of the game
     * 
     * */
    public void draw_grid(Graphics2D g2, Grid gd){
      g2.setColor(color1);
      if(match_start && autozoom){
        check_grid(gd);
      }
      view_changer();
      int max_y = gd_scl*grid_size+10;
      int max_x = gd_scl*grid_size+10+cur_offset_x;
      for (int i=0 ; i< grid_size;i++){
        g2.drawLine(gd_scl+10+cur_offset_x,
          (i+1)*gd_scl+10+cur_offset_y,
          gd_scl*grid_size+10+cur_offset_x,
          (i+1)*gd_scl+10+cur_offset_y);
        g2.drawLine((i+1)*gd_scl+10+cur_offset_x,
          gd_scl+10+cur_offset_y,
          (i+1)*gd_scl+10+cur_offset_x,
          gd_scl*grid_size+10+cur_offset_y);

      }
      if(parent.cabinet.player2){
        for (int i=0 ; i< grid_size;i++){
          g2.drawLine(gd_scl+10+cur_offset_x,
            (i+1)*gd_scl+20+cur_offset_y+max_y,
            gd_scl*grid_size+10+cur_offset_x,
            (i+1)*gd_scl+20+cur_offset_y+max_y);
          g2.drawLine((i+1)*gd_scl+10+cur_offset_x,
            gd_scl+20+cur_offset_y+max_y,
            (i+1)*gd_scl+10+cur_offset_x,
            gd_scl*grid_size+20+cur_offset_y+max_y);
            

        }
      }
    }

    /**
     * Draw the grid of the game
     * 
     * */
    public void draw_grid(Graphics2D g2){
      if(match_start) return;
      for (int i=0 ; i< grid_size;i++){
        g2.drawLine(gd_scl+10,(i+1)*gd_scl+10,gd_scl*grid_size+10,(i+1)*gd_scl+10);
        g2.drawLine((i+1)*gd_scl+10,gd_scl+10,(i+1)*gd_scl+10,gd_scl*grid_size+10);
      }

    }
    /**
     * Draw the grid of the game at off set ox, oy
     * 
     * */
    public void draw_grid(Graphics2D g2,int ox , int oy){
      if(match_start) return;
      g2.setColor(color1);
      for (int i=0 ; i< grid_size;i++){
        g2.drawLine(gd_scl+10+ox,(i+1)*gd_scl+10+oy,gd_scl*grid_size+10+ox,(i+1)*gd_scl+10+oy);
        g2.drawLine((i+1)*gd_scl+10+ox,gd_scl+10+oy,(i+1)*gd_scl+10+ox,gd_scl*grid_size+10+oy);
      }
    }
        /**
     * Draw the grid of the game at off set ox, oy
     * 
     * */
    public void draw_bad_grid(Graphics2D g2,int ox , int oy){
      g2.setColor(color1);
      for (int i=0 ; i< grid_size;i++){
         g2.drawLine((gd_scl+10+ox)*(spinx+50),(i+1)*gd_scl+10+oy,(gd_scl*grid_size+10+ox)*(spinx+50),(i+1)*gd_scl+10+oy);
        g2.drawLine(((i+1)*gd_scl+10+ox)*(spinx+50),gd_scl+10+oy,((i+1)*gd_scl+10+ox)*(spinx+50),gd_scl*grid_size+10+oy);
      }

    }
    /**
     * Checks if the grid is displaying at proper zoom and offset
     * 
     * */
    public void check_grid(Grid gd){
      double offset_check = ((gd_scl*grid_size+10)/2 + cur_offset_x);

      double scl_check = 10*((double)(gd_scl*grid_size+10)/(double)resx);
      // System.out.printf("%s %s %s\n",""+offset_check,""+scl_check,""+resx);
      if((int)scl_check<=4){
        // cur_offset_x+=3;
        inc_size_scl();
      }
      else if((int)scl_check>5){
        // cur_offset_x-=3;
        dec_size_scl();
      }
      if(offset_check<=resx/2-10 ){ // Need to move to the East and/or increa size
          cur_offset_x+=4;
      }else if(offset_check>resx/2+10 ){
          cur_offset_x-=4;
      }

    }
    public void view_changer(){
      if(this_view && cur_offset_y <=0){
        cur_offset_y+=10;
      }
      else if(!this_view && cur_offset_y > -(gd_scl*grid_size+10)){
        cur_offset_y-=10;
      }
    }
    /**
     * Blur the current image
     * */
    public BufferedImage blur(BufferedImage image){
      // Double scaleFactor = 2 * 5;
      RescaleOp op1 = new RescaleOp((float)0.3, 0, null);
      op.filter(image, null);
      BufferedImage bufferedImage = op.filter(image, null);
      bufferedImage = op1.filter(bufferedImage,null);
      return bufferedImage;
    }
    /**
     * Return a attributed string
     * */
    public AttributedString get_str(String str){
      AttributedString attr_str=new AttributedString(str); 
      // attr_str.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0,str.length());
      attr_str.addAttribute(TextAttribute.FONT,font1);
      return attr_str;
    }
    @Override
    /**
     * Calculates and displays rasterized (pixiel by pixiel) image of every frame in the simulation
     * @param g - graphics instance for display
     * */
    public void paintComponent(Graphics g) {
      adjust_scl ();
      if (menu&&blurry){
        // blurry = true;
        Graphics2D g2 = image.createGraphics();     
        g2.setFont(font1);
        g2.setColor(bgd);
        g2.fillRect(0, 0, resx,resy);
        draw_active_button(g2);
        draw_grid(g2,parent.cabinet.p1.gd);
        if(blurry){
          image =blur(image);
        }
        draw_active_labels(g2);

        g.drawImage(image,0,0,null);
        blurry=false;
      }
      else if (menu  ){
        g.drawImage(image,0,0,resx,resy,0,0,image.getWidth(),image.getHeight(),null);
        Graphics2D g2 = (Graphics2D)g;
        g2.setFont(font1);
        draw_active_button(g2);
        draw_active_labels(g2);
        // blurry = false;
      }
      else {

        Graphics2D g2 = (Graphics2D)g;
        if(antialias_view) 
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); // The simulation cells look bad without antialiasing on
        g2.setFont(font1);
        // draw_active_labels(g2);
        g2.setColor(bgd);
        g2.fillRect(0, 0, resx,resy);
        // draw_curv();

        draw_active_button(g2);
        draw_active_labels(g2);
        draw_active_ele(g2,parent.cabinet.p1,parent.cabinet.p1.gd);
        draw_grid(g2,parent.cabinet.p1.gd);
        increamental_check();
      }
    }


    public void spin_check(){
      if(spinx>5 || spinx<(0)){
        increamentalx = increamentalx * (-1);
      }
      // if(spiny>gd_scl || spiny<(0)){
      //   increamentalx = increamentalx * (-1);
      // }
      spinx = spinx+increamentalx;
      // offy = offy+increamentaly;
    }

    public void increamental_check(){
      if(offy>resy/2 || offy<(0)){
        increamentaly = increamentaly * (-1);
      }
      if(offx>resx/2 || offx<(0)){
        increamentalx = increamentalx * (-1);
      }
      offx = offx+increamentalx;
      offy = offy+increamentaly;

    }

    /**
     * DEBUG ONLY
     * */
    public void display_debug_info(){
      System.out.println(scl);
    }

    public void run () {
      start_refresh();
    }
    /**
     * Switches from one button lay out to another
     * */
    public void clutch_switch(int a , int b, int c){
      gear_in = a;
      gear_out = b;
      gearing_step = c;
      gearing_set=false;
    }
    /**
     * Refreshes the graphics at fps time delay
     * 
     * */
    public void start_refresh(){
      int ticker=0;
      System.out.println("Starting refresh");
      
      SwingWorker<Boolean, int[][]> display_thread = new SwingWorker<Boolean, int[][]>() {
       
       protected Boolean doInBackground() throws Exception {
        while ( parent.cabinet.playing){
          repaint();
          try{
            Thread.sleep((int)fps);
            fgd=new Color(255,parent.cabinet.bColor%255,parent.cabinet.ticker%255);
            // parent.cabinet.ticker+=1;
          }
          catch(InterruptedException e){
            System.out.println("Tick sleep interapted.");
          }
        }
        return true;
       }

       protected void done() {
     
       }

       
       protected void process() {

       }
      
    };
    executor.submit(display_thread);
    // display_thread.execute();
  }

    public Rectangle get_area (){
      return area;
    }
    /**
    * 
    * Sets the parent  
    * 
    * */
    public void set_parent (Battle a){
      parent = a;
      
    }

    /**
    * Sets the color of the color pallete #1; if you are looking to change the color pallete #1 (green), it cannot be changed.
    * 
    * */
    public void set_color1(Color c){
      color1 = c;
    }

    /**
    * 
    * 
    * */
    public void set_color2(Color c){
      color2 = c;
    }

    /**
     * 
     * Sets the color of the color pallete #3; if you are looking to change the color pallete #1 (green), it cannot be changed.
     * 
     * */
    public void set_color3(Color c){
      color3 = c;
    }

    public int need_offset_x=0;
    public int need_offset_y=0;
    public int cur_offset_x=0;
    public int cur_offset_y=0;
    /**
     * 
     * */
    public int offx = 0 ;
    public int offy = 0;
    public int increamentalx = 2;
    public int increamentaly = 2;
    public int gd_increamental = 1;
    public int spinx =2;
    public int spiny =2;
    /**
     * Antialias view
     * 
     * */
    public boolean antialias_view = true;
    /**
     * Antialias view
     * 
     * */
    public boolean this_view = true;
    /**
     * 
     * */
    public boolean blurry = false;
    /**
     * 
     * */
    public boolean menu = false;
    /**
     * 
     * */
    public ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * 
     * */
    public boolean match_start = false;
    /**
    * color palette one
    * */
    protected Color RED = new Color(255,0,0); 
    /**
    * color palette one
    * */
    protected Color GREEN = new Color(0,255,0); 
    protected Color DARK_GREEN = new Color(0,255,0,70); 
    /**
    * color palette one
    * */
    protected Color BLUE = new Color(0,0,255); 
    /**
    * color palette one
    * */
    protected Color WHITE = new Color(255,255,255); 
    protected Color YELLOW = new Color(255,255,0); 
    /**
    * color palette one
    * */
    protected Color BLACK = new Color(0,0,0); 

    /**
    * color palette one
    * */
    protected Color color1 = new Color(255,255,255); 
    /**
    * color palette one
    * */
    protected Color color2 = new Color(0,255,0); 
    /**
    * color palette one
    * */
    protected Color color3 = new Color(0,0,0); 

    protected Color fgd = WHITE;
    public boolean autozoom = true;
    public Color bgd = BLACK;
    /**
     * 
     * */
    public int bscl =1;
    Kernel kernel = new Kernel(5,5, new float[] { 
        1f/25f ,1f/25f ,1f/25f ,1f/25f ,1f/25f ,1f/25f ,1f/25f ,1f/25f ,1f/25f , 
        1f/25f ,1f/25f ,1f/25f ,1f/25f ,1f/25f ,1f/25f ,1f/25f ,1f/25f ,1f/25f , 
        1f/25f ,1f/25f ,1f/25f ,1f/25f ,1f/25f ,1f/25f ,1f/25f });

    /**
     * 
     * */
    BufferedImage image = new BufferedImage(resx,resy,BufferedImage.TYPE_INT_BGR);
    /**
     * 
     * */
    ConvolveOp op = new ConvolveOp(kernel);

    public boolean gearing_set=false;
    public int gearing_offset_x = 5;
    public int gearing_offset_y = 5;
    /**
     * The current switching layer, if it exsist
     * */
    protected int gear_in = 0; 
    /**
     * The current switching layer, if it exsist
     * */
    protected int gear_out = 0; 
    /**
     * The number of frames the switching animation plays
     * */
    protected int gearing_step = 0; 

    /**
     * fram time of 33.33 ms is roughly 30 fps,
     * This is being used as frame time instead of frame per second.
     * */
    protected int fps = 16; // 
    /**
    * 
    * 
    * */
    public boolean run = true;
    /**
    * 
    * 
    * */
    public int scl = resy/10;
    /**
    * 
    * 
    * */
    protected int pov_cdnt = resx/2;
    /**
    * 
    * 
    * */
    protected Battle parent;
    /**
    * 
    * 
    * */
    protected int dot_scl;
    /**
    * 
    * 
    * */
    protected int font_scl;
    /**
    * 
    * 
    * */
    protected Rectangle area;
    /**
    * 
    * 
    * */
    Font font1 = new Font("Serif", Font.BOLD, 15);
    Font font2 = new Font(Font.MONOSPACED, Font.BOLD, 20);
    Font font3 = new Font(Font.MONOSPACED, Font.BOLD, 24);
    public String winner_message="THIS IS THE WINNER!!!!!!!!!!!!";
    public String ending = "this is the loser";
    /**
     * Grid width
     * */
    public int grid_size=11;
    public int win_cond = 15;
}


 
