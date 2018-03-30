package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball
{
    // Previous Position
    public Vec2 position_prev = new Vec2();
    // Position
    public Vec2 position = new Vec2();
    // Size
    public Vec2 size = new Vec2(15.0f, 15.0f);
    // Speed
    public float speed = 0.3f;
    // Direction Vector
    public Vec2 direction = new Vec2(-1.0f, 0.0f);

    // Shortcut positions
    public float getLeft()  {return position.x - size.x/2;}
    public float getRight() {return position.x + size.x/2;}
    public float getTop()   {return position.y + size.x/2;}
    public float getBottom(){return position.y - size.y/2;}

    public float width(){return size.x;}
    public float height(){return size.y;}

    public void setup(float _x, float _y)
    {
        position.set(_x, _y);
    }

    public void resolveCollisionWithPaddle(Paddle p)
    {
        // Fix position
        //x = p.getRight() + w/2;
    }

    public void update(double delta)
    {
        position_prev.set(position);

        // Calculate New Position for Ball
        Vec2 Offset_Speed = new Vec2((float)(delta) * speed);
        Vec2 Offset_Vector = direction.mult(Offset_Speed);

        // Move Ball
        position = position.add(Offset_Vector);
    }

    public void draw(GraphicsContext gc)
    {
        gc.setFill(Color.RED);
        gc.fillRect(position.x - size.x/2, position.y - size.y/2, size.x, size.y);
    }

}
