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
 * CPServer - stub of a TCP Server which goes with TCP Client (connect only)
 * @author  D. Patric
 * @version 2205
 */

public class TCPServer extends Application implements EventHandler<ActionEvent> 
{
   // Window attributes
   private Stage stage;
   private Scene scene;
   private VBox root;
   
   // GUI Components
   public TextArea taLog = new TextArea();
   private Button btnFormat = new Button("Format");
   

   // Socket stuff
   private ServerSocket sSocket = null;
   public static final int SERVER_PORT = 32001;
   
   //takes input from client and inputs it into an array list
   private ArrayList<Patient> newList = new ArrayList<Patient>();
   
   //tree for organizing the patients by last name 
   private TreeMap<String, Patient> tree_map = new TreeMap<String, Patient>();

   // Patient recieved list
   private List<Patient> patientList = new ArrayList<>();
   
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
      
      btnFormat.setOnAction(this);
      
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
   
   public void handle(ActionEvent ae) {
      String label = ((Button)ae.getSource()).getText();
      switch(label) {
 
         case "Format":
            doFormat();
            break;
      }
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
         patientList = (ArrayList<Patient>)objectInputStream.readObject();
      
         taLog.appendText("Info received!\n");
         
         for(Patient p: patientList)
         {       
            tree_map.put(p.getLastName(), p);
            taLog.appendText("Patient Received: " + p.toString());
         }    
        
      }
      
      catch(Exception e)
      {
      
      }
   }  

   public void doFormat()
   {
      String str = "";
      if(!taLog.getText().isBlank() && !patientList.isEmpty())
         str+="Info received!\n\n";

      Collections.sort(patientList);
      for(Patient p: patientList)
      {
         str+="Patient Received: "+p.toString()+"\n";
      }
      taLog.setText(str);
      storeInFile(str);
      encriptToFile();
   
   }
   private void storeInFile(String str){
      System.out.println("Storing method");
      System.out.println(str);
      try {
         FileWriter fileWriter = new FileWriter(new File("src/main/resources/store.txt"));
         fileWriter.write(str);
         System.out.println("Writing");
         fileWriter.close();
      } catch (IOException e) {
         System.out.println("Store file not found: "+e.getMessage());
      }
   }
   private void encriptToFile(){
      FileOutputStream fileOut = null;
      try {
         fileOut = new FileOutputStream("src/main/resources/enStore.txt");

         ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
         for (Patient p: patientList){
            objectOut.writeObject(p);
         }
         objectOut.close();

      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}