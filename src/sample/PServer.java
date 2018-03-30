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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

public class PServer extends Application
{
    // Main Launch
    public static void main(String[] args) {
        launch(args);
    }

    // Start
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Setup All Scene information
        PData data = PData.getInstance();
        data.Setup("Pong Server", primaryStage);

        // Create Update Function
        Thread PServer_Update = new Thread(update);
        PServer_Update.setDaemon(true);
        //PServer_Update.start();

        // Player Input
        data.getGameScene().addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode()== KeyCode.ENTER) {
                System.out.println("You pressed enter");
            }
        });
    }

    // Update Task
    Task update = new Task<Void>() {
        @Override
        public Void call() throws Exception {
            while (true) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run()
                    {
                        //System.out.println("TEST");
                        PongGame.getInstance().update();
                    }
                });
                Thread.sleep(200);
            }
        }
    };
}
