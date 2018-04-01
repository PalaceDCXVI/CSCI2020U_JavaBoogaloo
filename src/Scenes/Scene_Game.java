package Scenes;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import sample.PData;
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
        holder.setStyle("-fx-background-color: black");
        root.getChildren().add(holder);
        layout.setCenter(root);

        m_scene = new Scene(layout, data.AppWidth, data.AppHeight);

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
                PData.getInstance().changeScene(PData.PSceneState.MENU);
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
