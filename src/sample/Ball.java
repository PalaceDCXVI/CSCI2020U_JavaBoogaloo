package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball
{
    // Position
    public float x = 0.0f;
    public float y = 0.0f;
    // Size
    public float w = 5.0f;
    public float h = 5.0f;
    // Speed
    public float speed = 3.0f;
    // Direction Vector
    public float aim_x = 0.0f;
    public float aim_y = 0.0f;

    public void setup(float _x, float _y)
    {
        x = _x;
        y = _y;
    }

    public void update()
    {

    }

    public void draw(GraphicsContext gc)
    {
        gc.setFill(Color.RED);
        gc.fillRect(x, y, w, h);
    }

}
