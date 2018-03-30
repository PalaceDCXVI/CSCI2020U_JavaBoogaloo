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
        PServer_Update.start();

        // Player Input
        data.game_scene.setOnKeyPressed((key) ->
        {
            boolean UpKey = key.getCode()== KeyCode.UP;
            boolean DownKey = key.getCode()== KeyCode.DOWN;

            if(UpKey)
                PongGame.getInstance().updateInputUp(true);
            if(DownKey)
                PongGame.getInstance().updateInputDown(true);
        });
        data.game_scene.setOnKeyReleased((key) ->
        {
            boolean UpKey = key.getCode()== KeyCode.UP;
            boolean DownKey = key.getCode()== KeyCode.DOWN;

            if(UpKey)
                PongGame.getInstance().updateInputUp(false);
            if(DownKey)
                PongGame.getInstance().updateInputDown(false);
        });

        // Basic Input [Slow]
        data.game_scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) ->
        {
            if(key.getCode()== KeyCode.ESCAPE) {
                //Force Quit
                System.exit(0);
            }
        });
    }

    @Override
    public void stop()
    {
        // On Application Close
        PData.getInstance().ForceQuit = true;
        System.exit(0);
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
                        PongGame.getInstance().draw();

                        if(PData.getInstance().ForceQuit)
                            return;
                    }

                });
                Thread.sleep(1);
            }
            //return null;
        }
    };
}
