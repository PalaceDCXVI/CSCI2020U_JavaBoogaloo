package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

public class Main extends Application
{

    public static void main(String[] args) {
        launch(args);
    }

    //private Stage primaryStage;
    private BorderPane layout = new BorderPane();
    private BorderPane settings_layout = new BorderPane();
    private GridPane top = new GridPane();
    private Button b = new Button("TEST");

    private GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Group root = new Group();
        Canvas canvas = new Canvas(640, 640);
        gc = canvas.getGraphicsContext2D();
        drawShapes();


        StackPane holder = new StackPane();
        holder.getChildren().add(canvas);
        holder.setStyle("-fx-background-color: black");
        root.getChildren().add(holder);

        layout.setCenter(root);

        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        editMenu.getItems().add(new MenuItem("Cut"));

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(editMenu);

        //layout.setTop(b);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(layout, 640, 640));
        primaryStage.show();

        // Task Test
        Thread myTask = new Thread(task);
        myTask.setDaemon(true);
        myTask.start();
    }

    Task task = new Task<Void>() {
        @Override
        public Void call() throws Exception {
            while (true) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run()
                    {
                        System.out.println("TEST");
                        test++;
                        drawShapes();
                    }
                });
                Thread.sleep(100);
            }
        }
    };

    private int test = 150;

    private void drawShapes()
    {
        // Clear Screen
        gc.clearRect(0, 0, 640, 640);

        gc.setFill(Color.WHITE);
        gc.fillText("TEST", test, 200);

        gc.fillRect(0, 0, 15, 15);
        gc.fillRect(625, 625, 15, 15);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                new double[]{210, 210, 240, 240}, 4);
    }
}
