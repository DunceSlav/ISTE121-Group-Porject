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
 * TCPClient - simple tcp client program
 * together with TCPServer only does connect - not even complete disconnect.
 * @author  D. Patric
 * @version 2205
 */

public class TCPClient extends Application implements EventHandler<ActionEvent> {
   // Window attributes
   private Stage stage;
   private Scene scene;
   private VBox root;
   
   // Components - TOP
   private TextField tfServerIP = new TextField();
   private Button btnConnect = new Button("Connect");
   
   // Components - BOTTOM
   private Label lblLog = new Label("Log:");
   private TextArea taLog = new TextArea();
   
   // networking attributes
   public static final int SERVER_PORT = 32001;
   private Socket socket = null;
    
   /**
    * main program 
    */
   public static void main(String[] args) {
      launch(args);
   }
   
   /**
    * launch - draw and set up GUI
    */
   public void start(Stage _stage) {
      stage = _stage;
      stage.setTitle("TCP Client");
      stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
         public void handle(WindowEvent evt) { System.exit(0); }
      });
      stage.setResizable(false);
      root = new VBox(8);
      
      // ina row 
      
      private Label lblSentence
      
      // TOP - label, text field, button
      FlowPane fpTop = new FlowPane(8,8);
      fpTop.setAlignment(Pos.CENTER);
      fpTop.getChildren().addAll(new Label("Server Name or IP: "),
         tfServerIP, btnConnect);
      root.getChildren().add(fpTop);
      
      // BOTTOM - Label + text area
      FlowPane fpBot = new FlowPane(8,8);
      fpBot.setAlignment(Pos.CENTER);
      taLog.setPrefRowCount(10);
      taLog.setPrefColumnCount(35);
      fpBot.getChildren().addAll(lblLog, taLog);
      root.getChildren().add(fpBot);
      
      // Listen for the button
      btnConnect.setOnAction(this);

      // Show window
      scene = new Scene(root, 500, 250);
      stage.setScene(scene);
      stage.show();      
   }
   
   /**
    * Button dispatcher
    */
   public void handle(ActionEvent ae) {
      String label = ((Button)ae.getSource()).getText();
      switch(label) {
         case "Connect":
            doConnect();
            break;
         case "Disconnect":
            doDisconnect();
            break;
      }
   }
   
   /**
    * doConnect - Connect button
    */
   private void doConnect() {
      try {
         socket = new Socket(tfServerIP.getText(), SERVER_PORT);
      }
      catch(IOException ioe) {
         taLog.appendText("IO Exception: " + ioe + "\n");
         return;
      }
      taLog.appendText("Connected!\n");
      btnConnect.setText("Disconnect");
   }
   
   /**
    * doDisconnect - Disconnect button
    */
   private void doDisconnect() {
      try {
         socket.close();
      }
      catch(IOException ioe) {
         taLog.appendText("IO Exception: " + ioe + "\n");
         return;
      }
      taLog.appendText("Disconnected!\n");
      btnConnect.setText("Connect");
   }
}