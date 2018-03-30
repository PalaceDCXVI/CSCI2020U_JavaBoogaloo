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
    public float w = 15.0f;
    public float h = 80.0f;
    // Speed
    public float speed = 1.0f;

    // Movement
    private void moveUp(double delta)
    {
        y -= speed * delta;

        // Check screen limit
        if(y < 0){ y = 0.0f;}
    }
    private void moveDown(double delta)
    {
        y += speed * delta;

        // Check Screen limit
        float screen_height = PData.getInstance().AppHeight;
        if(y + h > screen_height)
            y = screen_height - h;
    }

    public boolean checkCollision()
    {
        
    }


    public void setup(float _x, float _y)
    {
        x = _x;
        y = _y;
    }

    public void update(double delta)
    {
        if(MoveUp && !MoveDown)
            moveUp(delta);
        else if(!MoveUp && MoveDown)
            moveDown(delta);
    }

    public void draw(GraphicsContext gc)
    {
        gc.setFill(Color.WHITE);
        gc.fillRect(x - w/2, y - h/2, w, h);
    }
}
