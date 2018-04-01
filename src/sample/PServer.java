package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
        //Thread PServer_Update = new Thread(update);
        //PServer_Update.setDaemon(true);
        //PServer_Update.start();

        // ADD EVENT HANDLERS AND LISTENERS HERE (temporary placement)

        /*
         //Menu Button
          data.button_test1.setOnAction(new EventHandler<ActionEvent>() {
              @Override public void handle(ActionEvent e)
              {
                  PData.getInstance().changeScene(PData.PSceneState.GAME);

                  PongGame.getInstance().enemyPaddle.size.y = 80.0f;
              }
          });
          data.button_test2.setOnAction(new EventHandler<ActionEvent>() {
              @Override public void handle(ActionEvent e)
              {
                  PData.getInstance().changeScene(PData.PSceneState.GAME);

                  PongGame.getInstance().enemyPaddle.size.y = 1000.0f;
              }
          });

        // Player Input (KEY PRESS)
        data.game_scene.setOnKeyPressed((key) ->
        {
            boolean UpKey = key.getCode()== KeyCode.UP;
            boolean DownKey = key.getCode()== KeyCode.DOWN;

            if(UpKey)
                PongGame.getInstance().updateInputUp(true);
            if(DownKey)
                PongGame.getInstance().updateInputDown(true);
        });
        // Player Input (KEY RELEASE)
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
            if(key.getCode()== KeyCode.ESCAPE)
            {
                PData.getInstance().changeScene(PData.PSceneState.MENU);
            }
        });
        data.menu_scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) ->
        {
            if(key.getCode()== KeyCode.ESCAPE)
            {
                System.exit(0);
            }
        });
        */

        // End of START
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

        // Framerate Data
        private final long[] frameTimes = new long[100];
        private int frameTimeIndex = 0 ;
        private boolean arrayFilled = false ;
        // Starts 1 until framerate updates correct
        private double deltaTime = 1.0;

        @Override
        public Void call() throws Exception
        {
            // Implementation of frame rate was mostly taken from this forum post on stack overflow
            // https://stackoverflow.com/questions/28287398/what-is-the-preferred-way-of-getting-the-frame-rate-of-a-javafx-application
            // START OF FRAME RATE STUFF
            AnimationTimer frameRateMeter = new AnimationTimer() {

                @Override
                public void handle(long now) {
                    long oldFrameTime = frameTimes[frameTimeIndex] ;
                    frameTimes[frameTimeIndex] = now ;
                    frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length ;
                    if (frameTimeIndex == 0) {
                        arrayFilled = true ;
                    }
                    if (arrayFilled) {
                        long elapsedNanos = now - oldFrameTime ;
                        long elapsedNanosPerFrame = elapsedNanos / frameTimes.length ;
                        double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame ;
                        //label.setText(String.format("Current frame rate: %.3f", frameRate));
                        //System.out.println(frameRate);
                        deltaTime = 60.0 / frameRate;
                        // Fix (glitch occurs a bit during startup
                        if(deltaTime > 100)
                            deltaTime = 1.0;
                    }
                }
            };
            frameRateMeter.start();
            // END OF FRAME RATE STUFF

            // Treat this for loop as the update function for the server
            while (true)
            {
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        PongGame.getInstance().update(deltaTime);
                        PongGame.getInstance().draw();

                        if(PData.getInstance().ForceQuit)
                            return;
                    }

                });
                Thread.sleep(1);
            }
            // End of Update Function
        }
    };
}
