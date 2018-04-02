package Scenes;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import sample.PClient;
import sample.PData;
import sample.PServer;
import sample.PongGame;

public class Scene_Game extends Scene_Base
{
    public BorderPane       layout   = new BorderPane();
    public Group            root     = new Group();
    public StackPane        holder   = new StackPane();
    public Canvas           canvas;
    public GraphicsContext  gc;

    public Scene_Game()
    {
        SetupScene();
        SetupHandlers();
    }

    public void SetupScene()
    {
        PData data = PData.getInstance();

        canvas = new Canvas(data.AppWidth, data.AppHeight);
        gc = canvas.getGraphicsContext2D();
        holder.getChildren().add(canvas);
        root.getChildren().add(holder);
        layout.setCenter(root);
        layout.setStyle("-fx-background-color: black");

        //m_scene = new Scene(layout, data.AppWidth, data.AppHeight);
        // Create Scene
        CreateScene(layout);

    }

    public void SetupHandlers()
    {
        // Player Input (KEY PRESS)
        m_scene.setOnKeyPressed((key) ->
        {
            boolean UpKey = key.getCode()== KeyCode.UP;
            boolean DownKey = key.getCode()== KeyCode.DOWN;

            if(UpKey)
                PongGame.getInstance().updateInputUp(true);
            if(DownKey)
                PongGame.getInstance().updateInputDown(true);

            // Space Bar
            if(key.getCode() == KeyCode.SPACE)
                PongGame.getInstance().updateInputSpacebar(true);

            // Escape
            if(key.getCode() == KeyCode.ESCAPE)
            {
                if (PData.getInstance().AppType == PData.ApplicationType.SERVER)
                {
                    PServer.GetInstance().CloseServer();
                }
                else if (PData.getInstance().AppType == PData.ApplicationType.CLIENT)
                {
                    PClient.GetInstance().CloseClient();
                }
                PData.getInstance().AddHighscore(PongGame.getInstance().player1name, PongGame.getInstance().player2name,
                                                PongGame.getInstance().player1score, PongGame.getInstance().player2score);
                PData.getInstance().changeScene(PData.PSceneState.MENU);
            }
        });
        // Player Input (KEY RELEASE)
        m_scene.setOnKeyReleased((key) ->
        {
            boolean UpKey = key.getCode()== KeyCode.UP;
            boolean DownKey = key.getCode()== KeyCode.DOWN;

            if(UpKey)
                PongGame.getInstance().updateInputUp(false);
            if(DownKey)
                PongGame.getInstance().updateInputDown(false);

            // Space Bar
            if(key.getCode() == KeyCode.SPACE)
                PongGame.getInstance().updateInputSpacebar(false);
        });
    }
}
