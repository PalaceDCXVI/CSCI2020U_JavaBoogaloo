package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;

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

    // Score
    public int player1score = 0;
    public int player2score = 0;

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

        leftPaddle.setup(leftPaddle.width(), data.AppHeight/2);
        rightPaddle.setup(data.AppWidth - rightPaddle.width(), data.AppHeight/2);
        ball.reset();

        // TESTING
        rightPaddle.size.y = 1000.0f;
        //

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
    public void update(double delta)
    {
        if(!isInit)
        {
            System.err.println("Pong Game Not Init");
            return;
        }

        // Check collision
        if(leftPaddle.checkCollision(ball))
            ball.resolveCollisionWithPaddle(leftPaddle);

        if(rightPaddle.checkCollision(ball))
            ball.resolveCollisionWithPaddle(rightPaddle);

        leftPaddle.update(delta);
        rightPaddle.update(delta);
        ball.update(delta);
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

        // Draw Score
        gc.setFill(Color.WHITE);
        gc.setFont(Utility.getFont(Utility.FontType.COURIER, FontPosture.REGULAR, 80));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(Utility.ToString(player1score), data.AppWidth * 0.2f ,data.AppHeight * 0.2f);
        gc.fillText(Utility.ToString(player2score), data.AppWidth * 0.8f ,data.AppHeight * 0.2f);
    }
}
