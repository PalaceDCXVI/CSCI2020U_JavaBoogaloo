package Scenes;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import sample.PData;

public class Scene_Game extends Scene_Base
{
    public BorderPane       layout   = new BorderPane();
    public Group            root     = new Group();
    public StackPane        holder   = new StackPane();
    public Canvas           canvas;
    public GraphicsContext  gc;

    public Scene_Game()
    {
        PData data = PData.getInstance();

        canvas = new Canvas(data.AppWidth, data.AppHeight);
        gc = canvas.getGraphicsContext2D();
        holder.getChildren().add(canvas);
        holder.setStyle("-fx-background-color: black");
        root.getChildren().add(holder);
        layout.setCenter(root);

        m_scene = new Scene(root, data.AppWidth, data.AppHeight);
    }
}
