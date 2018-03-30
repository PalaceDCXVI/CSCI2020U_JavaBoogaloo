package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball
{
    // Position
    public float x = 0.0f;
    public float y = 0.0f;
    // Size
    public float w = 15.0f;
    public float h = 15.0f;
    // Speed
    public float speed = 0.3f;
    // Direction Vector
    public float aim_x = -1.0f;
    public float aim_y = 0.0f;

    public void setup(float _x, float _y)
    {
        x = _x;
        y = _y;
    }

    public void update(double delta)
    {
        x += aim_x * speed * delta;
        y += aim_y * speed * delta;
    }

    public void draw(GraphicsContext gc)
    {
        gc.setFill(Color.RED);
        gc.fillRect(x - w/2, y - h/2, w, h);
    }

}
