import java.util.List;
import java.util.ArrayList;
public class Player {
  public Player (int a, int sl){
    id = a; 
    gd = new Grid (gd_size);
    gd.scl=sl;
    for (int i=0;i<gd_size;i++){
      shps.add(new ArrayList<Ele>());
      for (int z =0 ;z<gd_size;z++){
        shps.get(i).add(new Ele(i,z,0,false));
      }
    }
  }
  public int gd_size =10;
  // public ArrayList<Ele> shps=new ArrayList<Ele>();
  public ArrayList<ArrayList<Ele>> shps=new ArrayList<ArrayList<Ele>>();
  public Grid gd;
  int id;
  public int score=0;

  public int grid_size (){
    return gd.size;
  }
  public int gd_scl(){
    return gd.scl;
  }
}