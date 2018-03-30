package sample;

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
    public Paddle leftPaddle = new Paddle();
    public Paddle rightPaddle = new Paddle();
    public Ball ball = new Ball();

    // Setup
    public void setup(PSide sideOfField)
    {
        setPlayerSide(sideOfField);
    }

    // Update Player Input
    public void updateInput(int direction)
    {
        switch(direction)
        {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    // Update
    public void update()
    {

    }
}
