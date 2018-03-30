package sample;

public class Paddle
{
    // Position
    public float x = 0.0f;
    public float y = 0.0f;
    // Speed
    public float speed = 2.0f;

    // Movement
    public void moveUp()
    {
        y -= 2.0f;
    }
    public void moveDown()
    {
        y += 2.0f;
    }

    public void setup(float _x, float _y)
    {
        x = _x;
        y = _y;
    }

    public void update()
    {

    }
}
