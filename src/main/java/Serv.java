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
class Serv extends P2P implements Runnable {

  @Override
  public void run() {
    // serv=-1;
    parent.cabinet.serv = 1;
    try {
      ServerSocket s = new ServerSocket(parent.cabinet.sock);
      System.out.println("Waiting for client to connect...");

      Socket incoming = s.accept();
      try {
        InputStreamReader inStream = new InputStreamReader(incoming.getInputStream());
        OutputStreamWriter outStream = new OutputStreamWriter(incoming.getOutputStream());
        System.out.println("Client connected, writing");

        BufferedReader in = new BufferedReader(inStream);
        BufferedWriter out = new BufferedWriter(outStream);
        System.out.println("Client connected, writing");
        parent.cabinet.all_labels.get(2).get(0).str = "Client Handshake Established";
        parent.cabinet.try_connect();
        String line = "";
        out.write("Hello! Enter BYE to exit.");
        out.newLine();
        out.flush();
        boolean done = false;
        while (!done )
        {
          //################################################################
          
          try {
            Thread.sleep(60);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          line = in.readLine();
          // System.out.println("client:" + line);
          // read_packet(line);
          out_str = "";
          super.read_packet(line);
          synchronized (parent.info){
              out_str=parent.info+";";
              parent.info = "";
          }
          synchronized(parent.ls) {
            for (int i = 0; i < parent.graphPane.grid_size - 1; i++) {
              for (int z = 0; z < parent.graphPane.grid_size - 1; z++) {
                out_str = out_str + parent.ls.get(i).get(z);
              }
              out_str = out_str + ",";
            }
          }
          parent.cabinet.peer = true;
          
          // System.out.println(out_str);
          // out.println(out_str);
          out.write(out_str);
          out.newLine();
          out.flush();
          if (line.trim().equals("BYE")) done = true;
          //################################################################

        }

      } catch (Exception e) {
        parent.cabinet.peer = false;
        System.out.println("exit");
        e.printStackTrace();
      } finally {
        parent.cabinet.peer = false;
        incoming.close();
      }
    } catch (IOException e) {
      parent.cabinet.peer = false;
      e.printStackTrace();
    }
    System.out.println("exited!");
    parent.cabinet.peer = false;
  }



}