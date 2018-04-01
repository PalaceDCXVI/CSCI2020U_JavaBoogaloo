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
    public Paddle enemyPaddle = null;
    public void setPlayerSide(PSide s)
    {
        if(s == PSide.LEFT)
        {
            playerPaddle = leftPaddle;
            enemyPaddle = rightPaddle;
        }
        else
        {
            playerPaddle = rightPaddle;
            enemyPaddle = leftPaddle;
        }

    }

    public Paddle leftPaddle = new Paddle();
    public Paddle rightPaddle = new Paddle();
    private Ball ball = new Ball();

    // Score
    public int player1score = 0;
    public int player2score = 0;

    // Win
    public boolean isGameOver = false;
    public final int winningScore = 1;

    // Init
    private boolean isInit = false;

    // SpaceBar Press
    private boolean actionButton = false;

    // Reference To Graphics Context
    private PData data;
    private GraphicsContext gc;

    // Setup
    public void setup()
    {
        setPlayerSide(PSide.LEFT);
        data = PData.getInstance();
        gc = data.game.gc;

        reset();

        isInit = true;
    }

    // Reset Game
    public void reset()
    {
        isGameOver = false;
        leftPaddle.setup(leftPaddle.width(), data.AppHeight/2);
        rightPaddle.setup(data.AppWidth - rightPaddle.width(), data.AppHeight/2);
        ball.reset();
        player1score = 0;
        player2score = 0;
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
    public void updateInputSpacebar(boolean spacebar)
    {
        actionButton = spacebar;
    }

    // Update
    public void update(double delta)
    {
        if(isGameOver)
        {
            if(actionButton)
                reset();
            return;
        }

        if(!isInit)
        {
            System.err.println("Pong Game Not Init");
            return;
        }

        leftPaddle.update(delta);
        rightPaddle.update(delta);

        // Check if ball is waiting
        if(ball.ResetWait > 0.0f)
        {
            ball.attachToPaddle();
            ball.ResetWait -= delta;

            if(actionButton)
                ball.ResetWait = 0.0f;
        }
        else{
            ball.update(delta);

            // Check collision
            if (leftPaddle.checkCollision(ball))
                ball.resolveCollisionWithPaddle(leftPaddle);

            if (rightPaddle.checkCollision(ball))
                ball.resolveCollisionWithPaddle(rightPaddle);

        }

        // Check end state
        if(player1score >= winningScore || player2score >= winningScore)
        {
            isGameOver = true;
        }
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

        // Draw Middle Line
        gc.setFill(Color.GRAY);
        gc.fillRect(data.AppWidth*0.5f - 2.5f, 0.0f, 5.0f, data.AppHeight);

        // Draw Score
        gc.setFill(Color.WHITE);
        gc.setFont(Utility.getFont(Utility.FontType.COURIER, FontPosture.REGULAR, 80));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(Utility.ToString(player1score), data.AppWidth * 0.2f, data.AppHeight * 0.2f);
        gc.fillText(Utility.ToString(player2score), data.AppWidth * 0.8f, data.AppHeight * 0.2f);

        if(!isGameOver)
        {
            // Draw Both Paddles
            leftPaddle.draw(gc);
            rightPaddle.draw(gc);

            // Draw Ball
            ball.draw(gc);

        }
        else if(isGameOver)
        {
            gc.setFont(Utility.getFont(Utility.FontType.COURIER, FontPosture.REGULAR, 100));

            String WinMessage = "PLAYER 2 WINS";
            if(player1score > player2score)
                WinMessage ="PLAYER 1 WINS";

            gc.fillText(WinMessage, data.AppWidth * 0.5f ,data.AppHeight * 0.5f);
        }


    }
}
