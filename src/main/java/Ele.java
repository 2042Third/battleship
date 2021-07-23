import java.util.ArrayList;
import java.io.*;
import java.util.*;

public class Ele {
  public int X;
  public int Y;
  public int cur_hlth;
  public int hlth_lim=6;
  public int tp;
  public boolean show = false;
  /**
   * Change the position of the ship to coord (inx,iny)
   * */
  public void to(int inx, int iny){
    X = inx;
    Y = iny;
  }
  public Ele (int inx, int iny, int type,boolean s){
    to(inx,iny);
    tp = type;
    show = s;
  }

  /**
   * return the current heallth of the ship
   * */
  public int hlth (){
    return cur_hlth;
  }
}