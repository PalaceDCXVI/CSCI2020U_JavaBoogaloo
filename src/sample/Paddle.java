package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle
{
    // Movement
    public boolean MoveUp = false;
    public boolean MoveDown = false;

    // Position
    public Vec2 position = new Vec2();
    // Size
    public Vec2 size = new Vec2(15.0f, 80.0f);
    // Speed
    public float speed = 1.0f;

    // Shortcut positions
    public float getLeft()  {return position.x - size.x/2;}
    public float getRight() {return position.x + size.x/2;}
    public float getTop()   {return position.y + size.x/2;}
    public float getBottom(){return position.y - size.y/2;}

    public float width(){return size.x;}
    public float height(){return size.y;}

    // Movement
    private void moveUp(double delta)
    {
        position.y -= speed * delta;

        // Check screen limit
        if(position.y < 0){ position.y = 0.0f;}
    }
    private void moveDown(double delta)
    {
        position.y += speed * delta;

        // Check Screen limit
        float screen_height = PData.getInstance().AppHeight;
        if(position.y + size.y > screen_height)
            position.y = screen_height - size.y;
    }

    public boolean checkCollision(Ball b)
    {
        /*
        // Check for X position
        boolean X_check =
                (b.getLeft() < getRight()  && b.getLeft() > getLeft()) ||
                (b.getRight() < getRight() && b.getRight() > getLeft());

        boolean Y_check =
                (b.getTop() > getBottom()    && b.getTop() < getTop()) ||
                (b.getBottom() > getBottom() && b.getBottom() < getTop());

        return X_check && Y_check;
        */
        // Create line from previous ball position to current one
        return false;
    }


    public void setup(float _x, float _y)
    {
        position.set(_x, _y);
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
        gc.fillRect(position.x - size.x/2, position.y - size.y/2, size.x, size.y);
    }
}
