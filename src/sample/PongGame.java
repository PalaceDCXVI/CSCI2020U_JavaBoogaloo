package sample;

import javafx.scene.canvas.GraphicsContext;

public class PongGame
{
    // Instancing
    static private PongGame instance = null;
    static public PongGame getInstance()
    {
        if(instance == null) instance = new PongGame();
        return instance;
    }

    // Data
    public enum PSide
    {
        LEFT,
        RIGHT
    }
    public Paddle playerPaddle = null;
    public void setPlayerSide(PSide s)
    {
        if(s == PSide.LEFT)
            playerPaddle = leftPaddle;
        else
            playerPaddle = rightPaddle;
    }

    private Paddle leftPaddle = new Paddle();
    private Paddle rightPaddle = new Paddle();
    private Ball ball = new Ball();

    // Init
    private boolean isInit = false;

    // Reference To Graphics Context
    private PData data;
    private GraphicsContext gc;

    // Setup
    public void setup(PSide sideOfField)
    {
        setPlayerSide(sideOfField);
        data = PData.getInstance();
        gc = data.game_gc;

        leftPaddle.setup(0, 0);
        rightPaddle.setup(100, 100);
        ball.setup(200, 200);

        isInit = true;
    }

    // Update Player Input
    public void updateInputUp(boolean UpKey)
    {
        if(playerPaddle == null)
        {
            System.err.println("No Player Paddle Selected");
            return;
        }

        playerPaddle.MoveUp = UpKey;
    }
    public void updateInputDown(boolean DownKey)
    {
        if(playerPaddle == null)
        {
            System.err.println("No Player Paddle Selected");
            return;
        }

        playerPaddle.MoveDown = DownKey;
    }

    // Update
    public void update()
    {
        if(!isInit)
        {
            System.err.println("Pong Game Not Init");
            return;
        }

        leftPaddle.update();
        rightPaddle.update();
        ball.update();
    }

    // Draw Scene
    public void draw()
    {
        if(!isInit)
        {
            System.err.println("Pong Game Not Init");
            return;
        }

        // Clear Screen
        gc.clearRect(0, 0, data.AppWidth, data.AppHeight);

        // Draw Both Paddles
        leftPaddle.draw(gc);
        rightPaddle.draw(gc);

        // Draw Ball
        ball.draw(gc);
    }
}
