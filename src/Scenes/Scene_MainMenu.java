package Scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import sample.*;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scene_MainMenu extends Scene_Base
{
    private BorderPane root;

    private Label Label_Title;
    private Button Button_Join; private TextField TextField_IPAddress; private Label Label_Error;
    private Button Button_Create;
    private Button Button_Scores;
    private Button Button_Exit;

    Socket socket;

    int clientPort = 20500;

    public Scene_MainMenu()
    {
        SetupScene();
        SetupHandlers();
    }
    public void SetupScene()
    {
        root = new BorderPane();
        m_scene = new Scene(root, PData.getInstance().AppWidth, PData.getInstance().AppHeight, Color.BLACK);;

        GridPane center = new GridPane();
        center.setAlignment(Pos.CENTER);

        VBox vBox_Text = new VBox();
        vBox_Text.setAlignment(Pos.CENTER);

        Label_Title = new Label("PONG");
        Label_Title.setTextAlignment(TextAlignment.CENTER);
        Label_Title.setFont(Font.font("Verdana, 48"));

        vBox_Text.getChildren().addAll(Label_Title);
        center.add(vBox_Text, 0, 0);

        TextField_IPAddress = new TextField("IpAddress");
        center.add(TextField_IPAddress, 1, 0);

        VBox vBox_Buttons = new VBox();
        vBox_Buttons.setAlignment(Pos.CENTER);

        Label_Error = new Label("asd");

        Button_Join = new Button("Join");
        Button_Join.setAlignment(Pos.CENTER);
        Button_Join.setOnAction(event -> OnAction_Join());

        Button_Create = new Button("Create");
        Button_Create.setAlignment(Pos.CENTER);
        Button_Create.setOnAction(event -> OnAction_Create());

        Button_Scores = new Button("HighScores");
        Button_Scores.setAlignment(Pos.CENTER);
        Button_Scores.setOnAction(event -> OnAction_HighScores());

        Button_Exit = new Button("Exit");
        Button_Exit.setAlignment(Pos.CENTER);
        Button_Exit.setOnAction(event -> OnAction_Exit());

        vBox_Buttons.getChildren().addAll(Label_Error, Button_Join, Button_Create, Button_Scores, Button_Exit);
        center.add(vBox_Buttons, 0, 2);

        root.setCenter(center);
    }
    public void SetupHandlers()
    {
        // Player Input (KEY PRESS)
        m_scene.setOnKeyPressed((key) ->
        {
            // Escape
            if(key.getCode() == KeyCode.ESCAPE)
                OnAction_Exit();
        });
    }


    public void OnAction_Join()
    {
        // Change Type
        PData.getInstance().AppType = PData.ApplicationType.CLIENT;

        // DEBUG //////////////////////////////////////////
        //PData.getInstance().changeScene(PData.PSceneState.GAME);

        //if(1 == 1)
        //    return;
        ////////////////!!!!!!!!!!!!!!1//

        //Fetch input for IP address to play against
        // Validate ip
        if (!validateIP(TextField_IPAddress.getText()))
        {
            Label_Error.setText("IP address validation failed.");
            return;
        }
        Label_Error.setText("");


        //Ensure connection.
        // Chances are the sockets should be in the Pong game. Should do this over there and check for validation.
        if(PClient.GetInstance().Connect(TextField_IPAddress.getText()))
        {
            Label_Error.setText("");

        }
        else
        {
            Label_Error.setText("Failed to connect to IP Address.");
            return;
        }


        //Create pong game. We don't have to do more verification than that for a second year final
        PData.getInstance().changeScene(PData.PSceneState.GAME);
    }

    public void OnAction_Create()
    {
        // Change Type
        PData.getInstance().AppType = PData.ApplicationType.SERVER;

        //Fetch input for IP address to play against

        //Spawn server socket
        if (PServer.GetInstance().StartServer())
        {
            Label_Error.setText("");
        }
        else
        {
            Label_Error.setText("Failed to create server");
            return;
        }

        //Create the server socket for the game
        //Create thread for the server socket. Wait for response
        Label_Error.setText("Waiting for response...");
    }

    public void OnAction_HighScores()
    {
        //Create high score scene.

        //Remove main menu from stage.
    }

    public void OnAction_Exit()
    {
        System.exit(0);
    }

    //Copied this from here: http://javafxforbeginners.blogspot.ca/2015/08/javafx-validating-text-field-with-ip.html
    public boolean validateIP(final String ip)
    {
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
