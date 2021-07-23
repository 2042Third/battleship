import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.io.*;
/**
* Homework 3
* @author Yi Yang
* @version rev. 1 17/7/2021
 */

public class hw3{
  /**
    * Main method that starts the gol gui application thread 
    * @param args - command line arguments
    */
    public static void main(String[] args)throws FileNotFoundException, IOException
    {
      EventQueue.invokeLater(() ->
         {
          var frame = new Battle();
          frame.Battle();
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);
        });
    }
}

