import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;
import java.util.*;
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
   
   //send button
   private Button btnSend = new Button("Send Patient Info");
   private Button btnAdd = new Button("Add Patient");
   
   // Components - BOTTOM
   private Label lblLog = new Label("Log:");
   private TextArea taLog = new TextArea();
   
   //first name
   private Label lblFname = new Label("First name:");
   private TextField tfFname = new TextField();
   //last name
   private Label lblLname = new Label("Last name:");
   private TextField tfLname = new TextField();
   //age
   private Label lblAge = new Label("Age:");
   private TextField tfAge = new TextField();
   //date of birth info
   private Label lblDOBinfo = new Label("Date of birth mm/dd/yyyy:");
   private TextField tfDOBmonth = new TextField();
   private Label lblDOBday = new Label("/");
   private TextField tfDOBday = new TextField();
   private Label lblDOByear = new Label("/");
   private TextField tfDOByear = new TextField();
   //height
   private Label lblHeight = new Label("Height:");
   private TextField tfHeight = new TextField();
   //weight
   private Label lblWeight = new Label("Weight:");
   private TextField tfWeight = new TextField();
   //days stayed
   private Label lblDays = new Label("Days Stayed:");
   private TextField tfDays = new TextField();

   
   
   //reason for stay
   private Label lblReason = new Label("Reason for stay:");
   private RadioButton reason1 = new RadioButton("Reason 1");
   private RadioButton reason2 = new RadioButton("Reason 2");
   private RadioButton reason3 = new RadioButton("Reason 3");
   private RadioButton reason4 = new RadioButton("Reason 4");
   private RadioButton reason5 = new RadioButton("Reason 5");
   private RadioButton reason6 = new RadioButton("Reason 6");
   private RadioButton reason7 = new RadioButton("Reason 7");
   private RadioButton reason8 = new RadioButton("Reason 8");
   private RadioButton reason9 = new RadioButton("Reason 9");
   private RadioButton reason10 = new RadioButton("Reason 10");
   
   //insurance y/n?
   private Label lblInsurance = new Label("Does patient have insurance?");
   private RadioButton radioY = new RadioButton("Yes");
   private RadioButton radioN = new RadioButton("No");
   
   // networking attributes
   public static final int SERVER_PORT = 32001;
   private Socket socket = null;
   
   // arraylist for patients
   private ArrayList<Patient> list = new ArrayList<Patient>();
   
   // cost attributes
   private double total;
   private String reason;
    
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
      stage.setOnCloseRequest(
         new EventHandler<WindowEvent>() {
            public void handle(WindowEvent evt) { System.exit(0); }
         });
      stage.setResizable(false);
      root = new VBox(8);
      
      // TOP - label, text field, button
      FlowPane fpTop = new FlowPane(8,8);
      fpTop.setAlignment(Pos.CENTER);
      fpTop.getChildren().addAll(new Label("Server Name or IP: "),
         tfServerIP, btnConnect,btnAdd,btnSend);
      root.getChildren().add(fpTop);
      
      // BOTTOM - Label + text area
      FlowPane fpBot = new FlowPane(8,8);
      fpBot.setAlignment(Pos.CENTER);
      taLog.setPrefRowCount(10);
      taLog.setPrefColumnCount(35);
      fpBot.getChildren().addAll(lblLog, taLog);
      root.getChildren().add(fpBot);
      
      //Patient Info section
      GridPane infoSection = new GridPane();
      //patient name
      HBox nameBox = new HBox(8);
      nameBox.getChildren().addAll(lblFname, tfFname, lblLname, tfLname);
      infoSection.addRow(1, nameBox);
      //patient age
      HBox ageBox = new HBox(8);
      ageBox.getChildren().addAll(lblAge, tfAge);
      infoSection.addRow(2, ageBox);
      //patient DOB
      HBox dobBox = new HBox(8);
      dobBox.getChildren().addAll(lblDOBinfo, tfDOBmonth, lblDOBday, tfDOBday, lblDOByear, tfDOByear);
      infoSection.addRow(3,dobBox);
      //patient height and weight
      HBox hwBox = new HBox(8);
      hwBox.getChildren().addAll(lblHeight, tfHeight, lblWeight, tfWeight, lblDays, tfDays);
      infoSection.addRow(4, hwBox);
      //patient reason for stay
      VBox reasonBox = new VBox(8);
      reasonBox.getChildren().addAll(lblReason, reason1, reason2, reason3, reason4, reason5, reason6, reason7, reason8, reason9, reason10);
      infoSection.addRow(5, reasonBox);
      //patient has insurance y/n?
      VBox insuranceBox = new VBox(8);
      insuranceBox.getChildren().addAll(lblInsurance, radioY, radioN);
      infoSection.addRow(6, insuranceBox);
      
      
      root.getChildren().add(infoSection);
      
      // Listen for the button
      btnConnect.setOnAction(this);
      btnSend.setOnAction(this);
      btnAdd.setOnAction(this);
   
      // Show window
      scene = new Scene(root, 650, 700);
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
         case "Send Patient Info":
            sendPatient();
            break;
         case "Add Patient":
            addPatient();
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
   

   private void sendPatient()
   {      
          
      try 
      {
         ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());        
      
         ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());        
      
         outputStream.writeObject(list);
         taLog.appendText("\nPatient data sent.");
         outputStream.flush();
      
         socket.close();
      
      }
      catch(IOException ioe) {
         taLog.appendText("IO Exception: " + ioe + "\n");
         
      }
      
   }
   
   public void addPatient()
   {
      // attribute formatting
      int days = Integer.parseInt(tfDays.getText());
      String dob = tfDOBmonth.getText() + "/" + tfDOBday.getText() + "/" + tfDOByear.getText();
      
      Patient p = new Patient(tfFname.getText(), tfLname.getText(), dob, Integer.parseInt(tfAge.getText()), 
         Double.parseDouble(tfHeight.getText()),Double.parseDouble(tfWeight.getText()), reason, days, total, false);
   
      p.setCost(calcCosts());
      p.setReason(reason);
      if(radioY.isSelected())
      {
         total = 0.0;
         p.setInsurance(true);
      }
      
      list.add(p);
      
      tfFname.setText("");
      tfLname.setText("");
      tfAge.setText("");
      tfDOBmonth.setText("");
      tfDOBday.setText("");
      tfDOByear.setText("");
      tfHeight.setText("");
      tfWeight.setText("");
      tfDays.setText("");
      total = 0.0;
   
   }
   
   
   public double calcCosts()
   {
      // Ankle Sprain
      if(reason1.isSelected())
      {
         total += 500.0;
         reason = "Sprained Ankle";
      }
      
      // Broken Leg
      if(reason2.isSelected())
      {
         total += 2500.0;
         reason = "Broken Leg";
      
      }
      
      // Broken Arm
      if(reason3.isSelected())
      {
         total += 2500.0;
         reason = "Broken Arm";
      }
      
      // Hip Fracture
      if(reason4.isSelected())
      {
         total += 25000.0;
         reason = "Fractured Hip";
      }
      
      // Blood Test
      if(reason5.isSelected())
      {
         total += 1500.0;
         reason = "Blood Test";
      }
      
      // X-Ray
      if(reason6.isSelected())
      {
         total += 300.0;
         reason = "X-Ray";
      }
      
      // CT-Scan
      if(reason7.isSelected())
      {
         total += 500.0;
         reason = "CT-Scan";
      }
      
      // Brain Surgery
      if(reason8.isSelected())
      {
         total += 100000.0;
         reason = "Brain Surgery";
      }
      
      // Back Surgery
      if(reason9.isSelected())
      {
         total += 70000.0;
         reason = "Back Surgery";
      }
      
      // Stitches
      if(reason10.isSelected())
      {
         total += 1500.0;
         reason = "Stitches";
      }
   
      return total;
   
   }

}
