package sample;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class PongGame
{
    public enum ObjectNetId
    {
        BALL,
        LPADDLE,
        RPADDLE,
        SCORE,
        RESET
    }

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
    public Ball ball = new Ball();

    // Score
    public int player1score = 0;
    public int player2score = 0;

    // Bouncer Emitters
    private ArrayList<PEmitter> emitters = new ArrayList<PEmitter>();
    public void AddEmitter(Vec2 position, int amount, Vec2 direction, Color tint)
    {
        PEmitter e = new PEmitter(gc, position, amount, direction);
        e.colorTint = tint;
        emitters.add(e);
    }

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
        leftPaddle.resetColor();
        rightPaddle.resetColor();
        ball.reset();
        player1score = 0;
        player2score = 0;
        ball.clearTrail();
        PData.getInstance().startTime = System.currentTimeMillis();
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
            {
                if (PData.getInstance().AppType == PData.ApplicationType.SERVER)
                {
                    PServer.GetInstance().SendMessage(ObjectNetId.RESET, new Vec2());
                }
                else if (PData.getInstance().AppType == PData.ApplicationType.CLIENT)
                {
                    PClient.GetInstance().SendMessage(ObjectNetId.RESET, new Vec2());
                }
                reset();
            }
            if (PData.getInstance().AppType == PData.ApplicationType.SERVER)
            {
                PServer.GetInstance().ReceiveUpdate();
            }
            else if (PData.getInstance().AppType == PData.ApplicationType.CLIENT)
            {
                PClient.GetInstance().ReceiveUpdate();
            }
            return;
        }

        if(!isInit)
        {
            System.err.println("Pong Game Not Init");
            return;
        }

        for(int i = 0; i < emitters.size(); i++)
        {
            // Update time and update self at the same time
            emitters.get(i).update().timeAlive -= delta;

            if(emitters.get(i).timeAlive <= 0.0)
                {
                    emitters.remove(emitters.get(i));
                    break;
                }
        }

        leftPaddle.update(delta);
        if (PData.getInstance().AppType == PData.ApplicationType.SERVER)
        {
            PServer.GetInstance().SendMessage(ObjectNetId.LPADDLE, leftPaddle.position);
        }
        else
        {
            PClient.GetInstance().ReceiveUpdate();
        }
        
        rightPaddle.update(delta);
        if (PData.getInstance().AppType == PData.ApplicationType.CLIENT)
        {
            PClient.GetInstance().SendMessage(ObjectNetId.RPADDLE, rightPaddle.position);
        }
        else
        {
            PServer.GetInstance().ReceiveUpdate();
        }

        // Check if ball is waiting
        if(ball.ResetWait > 0.0f)
        {
            ball.attachToPaddle();
            ball.ResetWait -= delta;

            if(actionButton)
                ball.ResetWait = 0.0f;
        }
        else{
            //Ball only updated on the server.
            if (PData.getInstance().AppType == PData.ApplicationType.SERVER)
            {
                ball.update(delta);

                // Check collision
                if (leftPaddle.checkCollision(ball)) {
                    ball.resolveCollisionWithPaddle(leftPaddle);
                    leftPaddle.color = Color.RED;
                }


                if (rightPaddle.checkCollision(ball)) {
                    ball.resolveCollisionWithPaddle(rightPaddle);
                    rightPaddle.color = Color.RED;
                }

                PServer.GetInstance().SendMessage(ObjectNetId.BALL, ball.position);
            }
            else
            {
                PClient.GetInstance().ReceiveUpdate();
            }


        }

        // Check end state
        if((player1score >= winningScore || player2score >= winningScore) && winningScore > 0)
        {
            isGameOver = true;
            PData.getInstance().endTime = System.currentTimeMillis();
        }


        //Bonus Network Update
        {
            if (PData.getInstance().AppType == PData.ApplicationType.SERVER)
            {
                PServer.GetInstance().ReceiveUpdate();
            }
            else if (PData.getInstance().AppType == PData.ApplicationType.CLIENT)
            {
                PClient.GetInstance().ReceiveUpdate();

            }
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
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(data.AppWidth*0.5f - 2.5f, 0.0f, 5.0f, data.AppHeight);

        // Draw Server Data on top left
        gc.setFill(Color.GRAY);
        gc.setFont(Utility.getFont(Utility.FontType.COURIER, FontPosture.REGULAR, 16));
        gc.setTextAlign(TextAlignment.LEFT);

        String ServerInfo1 = "Server: ";
        if(data.AppType == PData.ApplicationType.CLIENT)
            ServerInfo1 = "Client: ";

        ServerInfo1 += data.IpAddress + "\nPort: " + Utility.ToString(data.Port);

        gc.fillText(ServerInfo1, 2, 14);


        gc.setTextAlign(TextAlignment.RIGHT);
        String ServerInfo2 = "Client Connection: " + ((data.ClientConnected) ? "TRUE" : "FALSE");
        ServerInfo2 += "\nClient Ping: 0ms";
        gc.fillText(ServerInfo2, data.AppWidth, 14);

        // Draw Score
        gc.setFill(Color.WHITE);
        gc.setFont(Utility.getFont(Utility.FontType.COURIER, FontPosture.REGULAR, 80));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(Utility.ToString(player1score), data.AppWidth * 0.2f, data.AppHeight * 0.2f);
        gc.fillText(Utility.ToString(player2score), data.AppWidth * 0.8f, data.AppHeight * 0.2f);

        if(!isGameOver)
        {
            // Draw Emitters
            for(int i = 0; i < emitters.size(); i++)
            {
                emitters.get(i).draw();
            }

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
