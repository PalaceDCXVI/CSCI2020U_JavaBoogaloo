package sample;

import Scenes.Scene_Game;
import Scenes.Scene_MainMenu;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PData
{
    // Instancing
    static private PData instance = null;
    static public PData getInstance()
    {
        if(instance == null) instance = new PData();
        return instance;
    }

    // Force Quit
    public boolean ForceQuit = false;

    // Status
    public enum ApplicationType
    {
        NONE,
        SERVER,
        CLIENT
    }
    public ApplicationType AppType = ApplicationType.NONE;
    public String IpAddress = "";
    public int Port = 0;
    public static boolean ClientConnected = false;
    public static void SetAppData(ApplicationType a, String ipaddr, int port)
    {
        PData data = PData.getInstance();
        data.AppType = a;
        data.IpAddress = ipaddr;
        data.Port = port;
    }

    // Application Data
    private String      AppName = "";
    public final int    AppWidth = 720;
    public final int    AppHeight = 640;

    // Main Menu
    public Scene_MainMenu   mainmenu = null;
    // Game
    public Scene_Game       game = null;

    // Primary Stage
    public Stage primaryStage;
    public void setAppTitle(String str)
    {
        if(primaryStage == null)
            return;

        primaryStage.setTitle(str);
    }

    // Set Scene
    public enum PSceneState
    {
        NULL,
        MENU,
        GAME
    }
    private PSceneState current_Pscene = PSceneState.NULL;
    public PSceneState getCurrent_Pscene() {
        return current_Pscene;
    }

    public void changeScene(PSceneState p)
    {
        if(current_Pscene == p)
            return;

        current_Pscene = p;

        switch(p)
        {
            case MENU:
                primaryStage.setScene(mainmenu.GetScene());
                break;

            case GAME:
                primaryStage.setScene(game.GetScene());
                break;
        }
        this.primaryStage.show();
    }

    // Setup
    public void Setup(String _Appname, Stage _primaryStage)
    {
        // Create Scenes
        mainmenu    = new Scene_MainMenu();
        game        = new Scene_Game();

        // Setup Stage
        this.primaryStage = _primaryStage;
        this.AppName = _Appname;
        setAppTitle(this.AppName);

        // Show initial Scene
        changeScene(PSceneState.MENU);

        // Setup Pong (must be last call here since Graphics Context needs to be setup first)
        PongGame.getInstance().setup();
        PongGame.getInstance().setPlayerSide(PongGame.PSide.LEFT);
    }
}
