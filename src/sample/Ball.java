package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Ball
{
    // Previous Position
    public Vec2 position_prev = new Vec2();
    // Position
    public Vec2 position = new Vec2();
    // Size
    public Vec2 size = new Vec2(15.0f, 15.0f);
    // Speed
    public float speed = 0.5f;
    // Direction Vector
    public Vec2 direction = new Vec2(-1.0f, 0.0f);
    // Start Direction
    public boolean startLeft = true;

    // Trail Effect
    private ArrayList<BallTrail> trails = new ArrayList<>();
    public class BallTrail
    {
        public BallTrail(Vec2 _pos, Color _color)
        {
            position = _pos;
            color = _color;
            timeAlive = 1.0f;
        }
        public Vec2 position;
        public float timeAlive;
        public Color color;
    }
    public void AddTrail()
    {
        trails.add(new BallTrail(position, Color.RED));
    }
    public void clearTrail()
    {
        trails.clear();
    }

    // Slope of ball (y = [m]x + b)
    public float slope = 0.0f;
    public void updateSlope()
    {
        Vec2 BallTraversal_Local = position.subt(position_prev);
        slope = 0.0f;
        if(BallTraversal_Local.x != 0)
            slope = BallTraversal_Local.y / BallTraversal_Local.x;
    }

    // Shortcut positions
    public float getLeft()  {return position.x - size.x/2;}
    public float getRight() {return position.x + size.x/2;}
    public float getTop()   {return position.y - size.y/2;}
    public float getBottom(){return position.y + size.y/2;}

    public float width(){return size.x;}
    public float height(){return size.y;}
    public float widthHalf(){return size.x*0.5f;}
    public float heightHalf(){return size.y*0.5f;}

    // Reset Wait
    public float ResetWait = 0.0f;
    public float ResetTime = 1000.0f;


    // Attach To Paddle
    public void attachToPaddle()
    {
        PongGame pong = PongGame.getInstance();

        if(startLeft)
        {
            float x = pong.leftPaddle.position.x + pong.leftPaddle.widthHalf()*0.5f + width() + 10.0f;
            float y = pong.leftPaddle.position.y;
            position.set(x, y);
            position_prev.set(position);
            direction.x = +1.0f;
            direction.y = 0.0f;
        }
        else
        {
            float x = pong.rightPaddle.position.x - pong.leftPaddle.widthHalf()*0.5f - width() - 10.0f;
            float y = pong.rightPaddle.position.y;
            position.set(x, y);
            position_prev.set(position);
            direction.x = -1.0f;
            direction.y = 0.0f;
        }
    }

    // Reset To player paddle
    public void reset()
    {
        ResetWait = ResetTime;
        attachToPaddle();
        clearTrail();
    }

    public void resolveCollisionWithPaddle(Paddle p)
    {
        updateSlope();

        // Fix position
        Vec2 BallTraversal_Local = position.subt(position_prev);
        Vec2 BallTraversal_World = BallTraversal_Local.add(position_prev);
        float y_intercept = BallTraversal_World.y - slope * BallTraversal_World.x;

        // Direction affects position fix

        if(direction.x < 0)
        {
            // MOVE TO RIGHT OF PADDLE
            float y1 = slope * p.getRight() + y_intercept;
            position.set(p.getRight() + width(), y1);

            // Fix Direction
            float ychange = y1 - p.position.y;
            float ychange_fix = ychange / (p.height()*0.5f);
            Vec2 NewDirection = new Vec2(+1, ychange_fix);
            NewDirection.normalizeSelf();
            direction.set(NewDirection);
        }

        else if(direction.x > 0)
        {
            // MOVE TO LEFT OF PADDLE
            float y1 = slope * p.getLeft() + y_intercept;
            position.set(p.getLeft() - width(), y1);

            // Fix Direction
            float ychange = y1 - p.position.y;
            float ychange_fix = ychange / (p.height()*0.5f);
            Vec2 NewDirection = new Vec2(-1, ychange_fix);
            NewDirection.normalizeSelf();
            direction.set(NewDirection);
        }

    }

    public void update(double delta)
    {
        position_prev.set(position);

        // Calculate New Position for Ball
        Vec2 Offset_Speed = new Vec2((float)(delta) * speed);
        Vec2 Offset_Vector = direction.mult(Offset_Speed);

        // Move Ball
        position = position.add(Offset_Vector);

        // Check vertical edges
        if(position.y < size.y/2)
        {
            direction = direction.reflect(new Vec2(0, -1));
            position.y = size.y/2;
        }
        else if(position.y > PData.getInstance().AppHeight - size.y/2)
        {
            direction = direction.reflect(new Vec2(0, +1));
            position.y = PData.getInstance().AppHeight - size.y/2;
        }

        // Check off screen
        if(position.x - size.x/2 < 0)
        {
            reset();
            PongGame.getInstance().player2score++;
            startLeft = true;

            if (PData.getInstance().AppType == PData.ApplicationType.SERVER)
            {
                System.out.println("Score sent");
                PServer.GetInstance().SendMessage(PongGame.ObjectNetId.SCORE, new Vec2(PongGame.getInstance().player1score, PongGame.getInstance().player2score));
            }

        }
        else if(position.x + size.x/2 > PData.getInstance().AppWidth)
        {
            reset();
            PongGame.getInstance().player1score++;
            startLeft = false;

            if (PData.getInstance().AppType == PData.ApplicationType.SERVER)
            {
                System.out.println("Score sent");
                PServer.GetInstance().SendMessage(PongGame.ObjectNetId.SCORE, new Vec2(PongGame.getInstance().player1score, PongGame.getInstance().player2score));
            }
        }

        // trail Effect
        if(System.currentTimeMillis() % 2 == 0)
        {
            AddTrail();
        }

        // Update Trails
        for(int i = 0; i < trails.size(); i++)
        {
            trails.get(i).timeAlive *= 0.99f;
            if(trails.get(i).timeAlive < 0.01f)
            {
                trails.remove(trails.get(i));
                break;
            }

        }
    }

    public void draw(GraphicsContext gc)
    {
        // Draw Trail
        for(int i = 0; i < trails.size(); i++)
        {
            float t =  trails.get(i).timeAlive;
            double ts = Math.pow(t, 0.2);
            gc.setFill(Utility.colorMult(trails.get(i).color,Math.min(t, 0.7f)));
            gc.fillRect(trails.get(i).position.x - ts*size.x/2, trails.get(i).position.y - ts*size.y/2, ts*size.x, ts*size.y);
        }

        gc.setFill(Color.RED);
        gc.fillRect(position.x - size.x/2, position.y - size.y/2, size.x, size.y);
    }

}
