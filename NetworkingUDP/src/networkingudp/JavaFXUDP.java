package networkingudp;
/**
  _____                    _  _       _     ___  _
 |_   _|___  __ _  _ __   | \| | ___ | |_  / __|| |_   ___  _ _  ___
   | | / -_)/ _` || '  \  | .` |/ _ \|  _| \__ \| ' \ / _ \| '_|/ -_)
   |_| \___|\__,_||_|_|_| |_|\_|\___/ \__| |___/|_||_|\___/|_|  \___|

 Kyle Loveless, Gerardo Paleo, Matthew OBzera

 */
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
public class JavaFXUDP extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        // Menu bar, game menu, and playagain menu
        MenuBar menuBar = new MenuBar();
        Menu chatMenu = new Menu("Chat");
        Menu imageMenu = new Menu("Upload Image");
        menuBar.getMenus().addAll(chatMenu, imageMenu);
        //playAgainMenu.setDisable(true);
        
        MenuItem quitMenuItem = new MenuItem();
        quitMenuItem.setText("Quit");
        quitMenuItem.setOnAction(new QuitMenuItemHandler());
        
        MenuItem ImageMenuItem = new MenuItem();
        ImageMenuItem.setText("Upload");
        imageMenu.getItems().addAll(ImageMenuItem);
        ImageMenuItem.setOnAction(new ImageMenuItemHandler());
        
        // Create Menu Items and add to Game Menu
        MenuItem clientMenuItem = new MenuItem();
        clientMenuItem.setText("Client: Connect to another client");
        chatMenu.getItems().addAll(clientMenuItem, quitMenuItem);
        
         // Attach the event handlers to menu item
        clientMenuItem.setOnAction(new ClientMenuItemHandler());        
        
        BorderPane root = new BorderPane();       
        root.setTop(menuBar);
        Scene scene = new Scene(root);       
        
        HBox imageHBox = new HBox(20);
        imageHBox.setAlignment(Pos.CENTER);
        VBox image1 = new VBox(20);
        VBox image2 = new VBox(20);
        Label image1Label = new Label("Last image uploaded by you");
        Label image2Label = new Label("Last image uploaded by your friend");
        Globals.imageLocalView = new ImageView();
        Globals.imageRemoteView = new ImageView();
        image1.getChildren().addAll(image1Label, Globals.imageLocalView);
        image2.getChildren().addAll(image2Label, Globals.imageRemoteView);
        imageHBox.getChildren().addAll(image1, image2);
        root.setCenter(imageHBox);
        
        
        
        // ChatMessage VBox and its contents
        VBox chatMessageVBox = new VBox(10);
        chatMessageVBox.setAlignment(Pos.TOP_LEFT);
        Label chatLabel = new Label("Type a message to send:");
        // ChatHistory TextArea
        TextArea chatHistoryTextArea = new TextArea();
        Globals.chatHistoryTextArea = chatHistoryTextArea;  // make globally accessible
        chatHistoryTextArea.setPrefColumnCount(50);
        chatHistoryTextArea.setPrefRowCount(5); 
        TextField chatMessageTextField = new TextField();
        Globals.chatMessageTextField = chatMessageTextField;
        Button sendMessageButton = new Button("Send Message");
        sendMessageButton.setOnAction(new SendChatMessageHandler());
        chatMessageVBox.getChildren().addAll(chatHistoryTextArea, chatLabel, 
                                             chatMessageTextField, 
                                             sendMessageButton);
        root.setBottom(chatMessageVBox);        
        
        // Set margin to 10 for all children of a BorderLayout
        for (Node node : root.getChildren())
        {
           BorderPane.setMargin(node, new Insets(10));
        }        
        primaryStage.setHeight(600);
        primaryStage.setWidth(600);
        primaryStage.setTitle("CSC 469-569 UDP Chat");
        primaryStage.setScene(scene);
        primaryStage.show();
        
            
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

