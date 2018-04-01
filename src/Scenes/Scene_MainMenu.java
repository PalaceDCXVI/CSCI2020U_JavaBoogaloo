package Scenes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import sample.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scene_MainMenu extends Scene_Base {
    private BorderPane root = new BorderPane();

    private GridPane left_menu = new GridPane();
    private GridPane right_menu = new GridPane();

    // Left Menu
    private Button button_host = new Button("Host Game");
    private Button button_join = new Button("Join");
    private Button button_highscore = new Button("HIGH_SCORE");
    private Button button_exit = new Button("EXIT");

    // Right Menu
    private Label       label_username          = new Label("Username:");
    private Label       label_ipaddress         = new Label("IP ADDRESS:");
    private Label       label_status            = new Label("Status: [...]");
    private TextField   text_username           = new TextField("Randy Mandy");
    private TextField   text_ipaddress          = new TextField("127.0.0.1");
    private Button      button_randomusername   = new Button("Randomize");
    private Button      button_localhost        = new Button("LocalHost");

    private Socket socket;

    int clientPort = 20500;

    public Scene_MainMenu() {
        SetupScene();
        SetupHandlers();
    }

    public void SetupScene() {
        // Create Scene
        CreateScene(root);

        // General GUI
        ImageView logo = ImageLoader.GetImage("logo");

        root.setTop(logo);
        root.setLeft(left_menu);
        root.setCenter(right_menu);

        left_menu.setPadding(new Insets(40, 20, 0, 60));
        right_menu.setPadding(new Insets(40, 20, 0, 60));

        left_menu.setVgap(20);
        left_menu.setHgap(20);

        right_menu.setVgap(20);
        right_menu.setHgap(20);

        left_menu.add(button_host, 0, 0);
        left_menu.add(button_join, 0, 1);
        left_menu.add(button_highscore, 0, 2);
        left_menu.add(button_exit, 0, 3);

        right_menu.add(label_username, 0, 0);
        right_menu.add(text_username, 1, 0);
        right_menu.add(button_randomusername, 2, 0);
        right_menu.add(label_ipaddress, 0, 1);
        right_menu.add(text_ipaddress, 1, 1);
        right_menu.add(button_localhost, 2, 1);
        right_menu.add(label_status, 0, 2);
        //

        // Specific GUI
        button_host.setAlignment(Pos.CENTER);
        button_join.setAlignment(Pos.CENTER);
        button_localhost.setAlignment(Pos.CENTER);
        button_highscore.setAlignment(Pos.CENTER);
        button_exit.setAlignment(Pos.CENTER);
        button_randomusername.setAlignment(Pos.CENTER);

        button_host.setOnAction(event -> OnAction_Create());
        button_join.setOnAction(event -> OnAction_Join());
        button_localhost.setOnAction(event -> OnAction_LocalHost());
        button_highscore.setOnAction(event -> OnAction_HighScores());
        button_exit.setOnAction(event -> OnAction_Exit());
        button_randomusername.setOnAction(event -> RandomizeUsername());

        // Listener for Username
        text_username.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                UpdateButtonLockRules();
            }
        });
        // Listener for IP Address
        text_ipaddress.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                // Check if string doesn't contain letters
                if (newValue.matches(".*\\d+.*") == false && newValue.length() > 0) {
                    text_ipaddress.setText(oldValue);
                }

                UpdateButtonLockRules();
            }
        });
    }

    public void RandomizeUsername() {
        text_username.setText(RandomNames.GetName());
    }

    public void UpdateButtonLockRules() {
        boolean EmptyUsername = text_username.getText().length() == 0;
        boolean InvalidID = !Utility.validIP(text_ipaddress.getText());

        button_host.setDisable(EmptyUsername);
        button_join.setDisable(EmptyUsername || InvalidID);

    }


    public void SetupHandlers() {
        // Player Input (KEY PRESS)
        m_scene.setOnKeyPressed((key) ->
        {
            // Escape
            if (key.getCode() == KeyCode.ESCAPE)
                OnAction_Exit();
        });
    }

    public void OnAction_Create() {
        // Change Type
        PData.SetAppData(PData.ApplicationType.SERVER, text_ipaddress.getText(), clientPort);
        PongGame.getInstance().setPlayerSide(PongGame.PSide.LEFT);

        // DEBUG //
        PData.getInstance().changeScene(PData.PSceneState.GAME);
        if (1 == 1) return;
        // DEBUG //

        //Spawn server socket
        if (PServer.GetInstance().StartServer()) {
            label_status.setText("");
        } else {
            label_status.setText("Failed to create server");
            return;
        }

        //Create the server socket for the game
        //Create thread for the server socket. Wait for response
        label_status.setText("Waiting for response...");
    }

    public void OnAction_LocalHost() {
        text_ipaddress.setText("127.0.0.1");
    }

    public void OnAction_Join() {
        // Change Type
        PData.SetAppData(PData.ApplicationType.CLIENT, text_ipaddress.getText(), clientPort);
        PongGame.getInstance().setPlayerSide(PongGame.PSide.RIGHT);

        // DEBUG //
        PData.getInstance().changeScene(PData.PSceneState.GAME);
        if (1 == 1) return;
        // DEBUG //

        //Fetch input for IP address to play against
        // Validate ip
        if (!validateIP(text_ipaddress.getText())) {
            label_status.setText("IP address validation failed.");
            return;
        }
        label_status.setText("");

        if (PClient.GetInstance().Connect(text_ipaddress.getText())) {
            label_status.setText("");
        } else {
            label_status.setText("Failed to connect to IP Address.");
            return;
        }
    }

    public void OnAction_HighScores() {
        //Create high score scene.
        PData.getInstance().changeScene(PData.PSceneState.SCORES);
        //Remove main menu from stage.
    }

    public void OnAction_Exit() {
        System.exit(0);
    }

    //Copied this from here: http://javafxforbeginners.blogspot.ca/2015/08/javafx-validating-text-field-with-ip.html
    public boolean validateIP(final String ip) {
        Pattern pattern;
        Matcher matcher;
        String IPADDRESS_PATTERN
                = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
