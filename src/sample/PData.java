package sample;

import Scenes.Scene_Game;
import Scenes.Scene_Highscore;
import Scenes.Scene_MainMenu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.*;

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
    public long startTime, endTime;


    // Main Menu
    public Scene_MainMenu   mainmenu = null;
    // Game
    public Scene_Game       game = null;
    //Highscores
    public Scene_Highscore  highscore = null;

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
        GAME,
        SCORES
    }
    private PSceneState current_Pscene = PSceneState.NULL;
    public PSceneState getCurrent_Pscene() {
        return current_Pscene;
    }

    private ObservableList<HighScore> highscores;
    public ObservableList<HighScore> getHighscores() {
        return highscores;
    }
    private ObservableList<HighScore> LoadHighscores()
    {
        ObservableList<HighScore> tScores = FXCollections.observableArrayList();

        File file = new File("highscores.csv");
        try {
            if (file.exists()) {
                BufferedReader inFile = new BufferedReader(new FileReader(file));
                String line = inFile.readLine();
                while((line = inFile.readLine()) != null)
                {
                    String[] splitLine = line.split(",");
                    tScores.add(new HighScore(splitLine[0], Integer.parseInt(splitLine[1]), splitLine[2], Integer.parseInt(splitLine[3]), Float.parseFloat(splitLine[4])));
                }
            }
            else
            {
                file.createNewFile();
            }
        }
        catch (IOException e)
        {
            System.err.println("Error loading highscores file");
            e.printStackTrace();
        }

        return tScores;
    }
    public void SaveHighscores()
    {
        try
        {
            File file = new File("highscores.csv");
            PrintWriter outFile = new PrintWriter(file);
            outFile.println("Player1,P1Score,Player2,P2Score,GameTime");
            for(HighScore score : highscores)
            {
                outFile.println(score.toString());
            }
            outFile.close();
        }
        catch(IOException e)
        {
            System.err.println("Error saving highscores file");
            e.printStackTrace();
        }
    }
    public void AddHighscore(String p1name, String p2name, int p1score, int p2score)
    {
        highscores.add(new HighScore(p1name, p1score,
                        p2name, p2score,
                        endTime - startTime));
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
                mainmenu.UpdateButtonLockRules();
                break;

            case GAME:
                PData.getInstance().startTime = System.currentTimeMillis();
                primaryStage.setScene(game.GetScene());
                break;

            case SCORES:
                primaryStage.setScene(highscore.GetScene());
        }
        this.primaryStage.show();
    }

    // Setup
    public void Setup(String _Appname, Stage _primaryStage)
    {
        //load highscores from "highscores.csv", needs to happen before score scene is initialized
        highscores = LoadHighscores();

        // Create Scenes
        mainmenu    = new Scene_MainMenu();
        game        = new Scene_Game();
        highscore        = new Scene_Highscore();

        // Setup Stage
        this.primaryStage = _primaryStage;
        this.AppName = _Appname;
        setAppTitle(this.AppName);


        // Show initial Scene
        changeScene(PSceneState.MENU);

        // Setup Pong (must be last call here since Graphics Context needs to be setup first)
        PongGame.getInstance().setup();

        if (AppType == ApplicationType.SERVER)
        {
            PongGame.getInstance().setPlayerSide(PongGame.PSide.LEFT);
        }
        else
        {
            PongGame.getInstance().setPlayerSide(PongGame.PSide.RIGHT);
        }
    }
}
