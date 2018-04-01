package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle
{
    // Movement
    public boolean MoveUp = false;
    public boolean MoveDown = false;

    // Color
    public Color color = Color.color(1,1,1,1);

    // Position
    public Vec2 position = new Vec2();
    // Size
    public Vec2 size = new Vec2(15.0f, 80.0f);
    // Speed
    public float speed = 0.7f;

    // Shortcut positions
    public float getLeft()  {return position.x - size.x/2;}
    public float getRight() {return position.x + size.x/2;}
    public float getTop()   {return position.y - size.y/2;}
    public float getBottom(){return position.y + size.y/2;}

    public float width(){return size.x;}
    public float height(){return size.y;}
    public float widthHalf(){return size.x*0.5f;}
    public float heightHalf(){return size.y*0.5f;}

    // Movement
    private void moveUp(double delta)
    {
        position.y -= speed * delta;

        // Check screen limit
        if(position.y - heightHalf() < 0){ position.y = heightHalf();}
    }
    private void moveDown(double delta)
    {
        position.y += speed * delta;

        // Check Screen limit
        float screen_height = PData.getInstance().AppHeight;
        if(position.y + heightHalf() > screen_height)
            position.y = screen_height - heightHalf();
    }

    public boolean checkCollision(Ball b)
    {
        // Update Slope of ball
        b.updateSlope();

        // Create line from previous ball position to current one
        Vec2 BallTraversal_Local = b.position.subt(b.position_prev);
        Vec2 BallTraversal_World = BallTraversal_Local.add(b.position_prev);

        // y-intecept solving
        // y = slope * x + b;
        // y - slope * x = b;
        float y_intercept = BallTraversal_World.y - b.slope * BallTraversal_World.x;

        //System.out.println(y_intercept);

        // Check y positions at left and right of box
        float y1 = b.slope * getLeft() + y_intercept;
        float y2 = b.slope * getRight() + y_intercept;

        // It will cross the y threshold
        boolean Y_check =
                (y1 - b.heightHalf() < getBottom() && y1 + b.heightHalf() > getTop()) ||
                (y2 - b.heightHalf() < getBottom() && y2 + b.heightHalf() > getTop());

        // Check if cross the x threshold

        // check 1 dimensional data across direction of ball
        Vec2 Direction = BallTraversal_Local.getNormalized();
        // Compare ball points in 1D
        float b1 = Direction.dot(b.position_prev);
        float b2 = Direction.dot(b.position);
        // Against paddle points in 1D
        float p1 = Direction.dot(new Vec2(getLeft(), y1));
        float p2 = Direction.dot(new Vec2(getRight(), y2));

        // Convenient way to make sure b1, b2 and p1, p2 are smallest to biggest
        Vec2 b_data = new Vec2(b1, b2);
        Vec2 p_data = new Vec2(p1, p2);
        b_data.ascendingSelf();
        p_data.ascendingSelf();

        // 1D values collision check
        boolean X_check = (b_data.y >= p_data.x && p_data.y >= b_data.x);

        return X_check && Y_check;
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

        // Lerp Color to white
        color = Utility.lerp(color, Color.WHITE, 0.005);
    }

    public void resetColor()
    {
        color = Color.WHITE;
    }


    public void draw(GraphicsContext gc)
    {
        gc.setFill(color);
        //System.out.println(color);
        gc.fillRect(position.x - size.x/2, position.y - size.y/2, size.x, size.y);
    }
}
