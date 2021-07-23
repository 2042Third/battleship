import java.util.List;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;

public class P2P implements Runnable {

  public void set_parent(Battle a){
    parent=a;
  }
  public void run (){
    // synchronized (parent)
     parent.cabinet.serv = 0;
      try
      {
          
         String server, input, two_input;
         int port;
         // Scanner cons = new Scanner(System.in);

        System.out.println("Waiting for server...");
         Socket incoming = new Socket(parent.cabinet.ip, parent.cabinet.sock); // Hard coded to local network
         try                                       // but it was very cool...
         {
            InputStreamReader inStream = new InputStreamReader(incoming.getInputStream());
            OutputStreamWriter outStream = new OutputStreamWriter(incoming.getOutputStream());

            BufferedReader in = new BufferedReader(inStream);
            BufferedWriter out = new BufferedWriter(outStream);
            System.out.println("Server connected, reading");
            System.out.println("Server:"+in.readLine());

             out_str="hello";
            System.out.println(out_str);
            out.write(out_str);
            out.newLine();
            out.flush();
            parent.cabinet.all_labels.get(2).get(0).str = "Server Handshake Established";
            parent.cabinet.try_connect();
            input = "";
                
           while (true)
          {

            parent.cabinet.peer = true;
            synchronized (parent.info){
              out_str=parent.info+";";
              parent.info = "";
            }
            synchronized (parent.ls){
               for (int i=0;i<parent.graphPane.grid_size-1;i++){
                for(int z=0 ; z<parent.graphPane.grid_size-1;z++){
                  out_str=out_str+parent.ls.get(i).get(z);
                }
                out_str=out_str+",";
               }
             }
            // out.println(out_str);
            // System.out.println(out_str);
            out.write(out_str);
            out.newLine();
            out.flush();
            
            
            input = in.readLine();
            // System.out.println("Server:"+input);
            read_packet(input);
          }
            
         }
         catch (NoSuchElementException e) {
            e.printStackTrace();
            parent.cabinet.peer = false;
           System.out.println("The remote server appears to have closed the connection");
         }
         catch (InterruptedIOException exception) {
            exception.printStackTrace();
           System.out.println("Connection timeout");
         }
         catch (Exception e) {
            e.printStackTrace();
           System.out.println("Connection timeout");
         }
         finally
         {
            parent.cabinet.peer = false;
            System.out.println("Server closed");
            incoming.close();
         }
      }
      catch (IOException e)
      {
      parent.cabinet.peer = false;
         e.printStackTrace();
      }
      System.out.println("exited");
      
      parent.cabinet.peer = false;

  }
  public void read_packet(String str){
    String[] arry = str.split(";");
    if(arry.length == 2){
      read_grid_packet(arry[1]);
      arry=arry[0].split(",");
    }
    else {

      read_grid_packet(arry[0]);
      return;
    }

    for(String i:arry){
      if(i.length() !=0)System.out.println("P2_status_update: "+i);
      switch (i){
        case "BACK":
          synchronized(parent.cabinet){
            parent.cabinet.p2_menu=true;
            parent.cabinet.p2_ready=false;
          }
          continue;
          // break;
        case "START":
          synchronized(parent.cabinet){parent.cabinet.p2_menu=false;}
          // parent.cabinet.p2_=false;
          continue;
          // break;
        case "READY":
          synchronized(parent.logic){parent.cabinet.p2_ready=true;}
          continue;
          // break;
        case "MYTURN":
          parent.cabinet.round++;
          // parent.logic.p2_r=false;
          parent.cabinet.my_turn=false;
          parent.logic.p2_r=true;
          continue;
          // break;
        case "YOURTURN":
          if(!parent.cabinet.my_turn)parent.logic.shots=3;
          parent.cabinet.round++;
          parent.logic.p2_r=true;
          parent.cabinet.my_turn=true;
          continue;
      }

      String str_tmp ="";
      int cou = 0;
      for(char st:i.toCharArray()){
        if (cou==4) break;
        str_tmp=str_tmp+st;
        cou++;
      }
      if(!str_tmp.equals("SCOR") )continue;
      parent.cabinet.s1++;
      String[] tmp = i.split("_");
      parent.cabinet.p2.shps.get(Integer.parseInt(String.valueOf(tmp[1]))).get(Math.abs(parent.graphPane.grid_size-2-Integer.parseInt(String.valueOf(tmp[2])))).tp=2;
    }

  }
  
  public void read_grid_packet(String str) {
    Integer a;
    String[] arry = str.split(",");
    int ind_x=0,ind_y=parent.graphPane.grid_size-2;
    for (String i:arry){
      for (char z:i.toCharArray()){
        a = Character.getNumericValue(z);
        if(a==1){
          // System.out.printf("x: %d y: %d value: %d\n",ind_x,ind_y,a);
          if(parent.cabinet.p1.shps.get(ind_x).get(ind_y).tp == 1){

            synchronized(parent.info){parent.info=parent.info+",SCORE_"+ind_x+"_"+ind_y;}
            parent.cabinet.p1.shps.get(ind_x).get(ind_y).tp=2;
            parent.cabinet.s2++;
          }
          else if (parent.cabinet.p1.shps.get(ind_x).get(ind_y).tp == 0){

            parent.cabinet.p1.shps.get(ind_x).get(ind_y).tp=3;
          }
        }
        ind_y--;
      }
      ind_x++;
      ind_y=parent.graphPane.grid_size-2;
    }
  }
  public void reader (){
    out_str="";
    synchronized (parent.ls){
     for (int i=0;i<parent.graphPane.grid_size;i++){
      for(int z=0 ; z<parent.graphPane.grid_size;z++){
        out_str=out_str+parent.ls.get(i).get(z);
      }
      out_str=out_str+",";
     }
   }
  }
  public boolean str_ready = false;
  public String out_str="";

  public Battle parent;

}