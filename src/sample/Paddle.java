package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle
{
    // Movement
    public boolean MoveUp = false;
    public boolean MoveDown = false;

    // Position
    public float x = 0.0f;
    public float y = 0.0f;
    // Size
    public float w = 20.0f;
    public float h = 70.0f;
    // Speed
    public float speed = 1.0f;

    // Movement
    private void moveUp()
    {
        y -= speed;

        // Check screen limit
        if(y < 0){ y = 0.0f;}
    }
    private void moveDown()
    {
        y += speed;

        // Check Screen limit
        float screen_height = PData.getInstance().AppHeight;
        if(y + h > screen_height)
            y = screen_height - h;
    }

    public void setup(float _x, float _y)
    {
        x = _x;
        y = _y;
    }

    public void update()
    {
        if(MoveUp && !MoveDown)
            moveUp();
        else if(!MoveUp && MoveDown)
            moveDown();
    }

    public void draw(GraphicsContext gc)
    {
        gc.setFill(Color.WHITE);
        gc.fillRect(x, y, w, h);
    }
}
