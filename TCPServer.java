import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;

import java.net.*;
import java.io.*;

/**
 * CPServer - stub of a TCP Server which goes with TCP Client (connect only)
 * @author  D. Patric
 * @version 2205
 */

public class TCPServer extends Application {
   // Window attributes
   private Stage stage;
   private Scene scene;
   private VBox root;
   
   // GUI Components
   public TextArea taLog = new TextArea();
   private Button btnFormat = new Button("PDF Download");
   

   // Socket stuff
   private ServerSocket sSocket = null;
   public static final int SERVER_PORT = 32001;
   
   /**
    * main program
    */
   public static void main(String[] args) {
      launch(args);
   }
   
   /**
    * Launch, draw and set up GUI
    * Do server stuff
    */
   public void start(Stage _stage) {
      // Window setup
      stage = _stage;
      stage.setTitle("Electronic Medical Records");
      stage.setOnCloseRequest(
         new EventHandler<WindowEvent>() {
            public void handle(WindowEvent evt) { System.exit(0); }
         });
      stage.setResizable(false);
      root = new VBox(8);
   
      // TOP components
      FlowPane fpBot = new FlowPane(8,8);
      fpBot.setAlignment(Pos.CENTER);
      taLog.setPrefRowCount(10);
      taLog.setPrefColumnCount(35);
      fpBot.getChildren().addAll(new Label("Patients"), taLog);
      root.getChildren().add(fpBot);
      root.getChildren().add(btnFormat);
      
      // Show window
      scene = new Scene(root, 500, 400);
      stage.setScene(scene);
      stage.show();      
      
      // Do server work in a thread
      Thread t = 
         new Thread() {
            public void run() { doServerWork(); }
         };
      t.start();
   }
   
   /** 
    * doServerWork
    * does the basic non-GUI work of the server 
    */
   public void doServerWork() {
      // Claim the port and start listening for new connections
      try {
         sSocket = new ServerSocket(SERVER_PORT);
      }
      catch(IOException ioe) {
         taLog.appendText("IO Exception (1): " + ioe);
         return;
      }
   
      // Socket for communication with client      
      Socket cSocket = null;
      try {
         // Wait for a connection
         cSocket = sSocket.accept();
      }
      catch(IOException ioe) {
         taLog.appendText("IO Exception (2): " + ioe);
         return;
      }
      
      // No real processing yet
      taLog.appendText("Client connected!\n");
      
      
      try
      {
         OutputStream outputStream = cSocket.getOutputStream();
         ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

         InputStream inputStream = cSocket.getInputStream();
         ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
         Patient newPatient = (Patient)objectInputStream.readObject();     
         
         if(newPatient instanceof Patient)
         {
            taLog.appendText("Info received: " + newPatient.toString());
         }

        
      }
      
      catch(Exception e)
      {
      
      }
   }   
}
