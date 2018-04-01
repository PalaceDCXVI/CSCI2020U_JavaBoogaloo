package sample;

import Scenes.Scene_MainMenu;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    //public static Stage primaryStage;

    public void start(Stage primaryStage) throws Exception
    {
        // Load Image
        ImageLoader.CreateImage("logo", "src/Images/game_title.png");

        // Setup Information
        PData data = PData.getInstance();
        data.Setup("Pong with Friends", primaryStage);

        //Create Update Function
        Thread PServer_Update = new Thread(update);
        PServer_Update.setDaemon(true);
        PServer_Update.start();
    }

    // End of Application
    @Override
    public void stop()
    {
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
                        // GAME
                        if(PData.getInstance().getCurrent_Pscene() == PData.PSceneState.GAME)
                        {
                            PongGame.getInstance().update(deltaTime);
                            PongGame.getInstance().draw();
                        }

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
