package sample;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

    // Application Data
    private String      AppName = "";
    public final int    AppWidth = 720;
    public final int    AppHeight = 640;

    // Main Menu
    public Scene       menu_scene;
    public BorderPane  menu_layout = new BorderPane();

    // Game
    public Scene           game_scene;
    public BorderPane      game_layout = new BorderPane();
    public Group           game_root = new Group();
    public StackPane       game_holder = new StackPane();
    public Canvas          game_canvas;
    public GraphicsContext game_gc;

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
    public void changeScene(PSceneState p)
    {
        if(current_Pscene == p)
            return;

        current_Pscene = p;

        switch(p)
        {
            case MENU:
                primaryStage.setScene(menu_scene);
                break;

            case GAME:
                primaryStage.setScene(game_scene);
                break;
        }
        this.primaryStage.show();
    }

    // Setup
    public void Setup(String _Appname, Stage _primaryStage)
    {
        // Setup Stage
        this.primaryStage = _primaryStage;
        this.AppName = _Appname;
        setAppTitle(this.AppName);
        // Setup Menu
        menu_scene = new Scene(menu_layout, AppWidth, AppHeight);

        // Setup Game
        game_scene = new Scene(game_layout, AppWidth, AppHeight);
        game_canvas = new Canvas(AppWidth, AppHeight);
        game_gc = game_canvas.getGraphicsContext2D();
        game_holder.getChildren().add(game_canvas);
        game_holder.setStyle("-fx-background-color: black");
        game_root.getChildren().add(game_holder);
        game_layout.setCenter(game_root);

        //  // Clear Screen
        //  game_gc.clearRect(0, 0, AppWidth, AppHeight);
        //
        //  // Testing
        //  game_gc.setFill(Color.WHITE);
        //  game_gc.fillRect(0, 0, 15, 15);
        //  game_gc.fillRect(AppWidth - 15, AppHeight - 15, 15, 15);

        // Show initial Scene
        changeScene(PSceneState.GAME);

        // Setup Pong (must be last call here since Graphics Context needs to be setup first)
        PongGame.getInstance().setup(PongGame.PSide.LEFT);
    }
}