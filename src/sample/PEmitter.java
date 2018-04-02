package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PEmitter
{
    private GraphicsContext gc;
    private Vec2 start_position = null;
    private Vec2[] positions = null;
    private Vec2[] velocities = null;
    private Color[] colors = null;
    private Vec2[] sizes = null;
    private float speed = 0.55f;
    public Color colorTint = new Color(0, 0, 0, 1);

    public double timeAlive = 0.0;
    private double timeAliveMax = 4000.0;

    public PEmitter(GraphicsContext gc, Vec2 position, int amount)
    {
        Setup(gc, position, amount, new Vec2(0,0));
    }
    public PEmitter(GraphicsContext gc, Vec2 position, int amount, Vec2 initialVelocity)
    {
        Setup(gc, position, amount, initialVelocity);
    }

    private void Setup(GraphicsContext gc, Vec2 position, int amount, Vec2 initialVelocity) {
        if (amount == 0)
            return;

        this.timeAlive = timeAliveMax;
        this.gc = gc;
        this.start_position = position;
        this.positions = new Vec2[amount];
        this.velocities = new Vec2[amount];
        this.colors = new Color[amount];
        this.sizes = new Vec2[amount];
        for (int i = 0; i < positions.length; i++) {
            positions[i] = new Vec2(start_position);

            velocities[i] = new Vec2(Utility.Random01() * 2.0f - 1.0f, Utility.Random01() * 2.0f - 1.0f);
            velocities[i] = velocities[i].add(initialVelocity);
            velocities[i].normalizeSelf();
            velocities[i] = velocities[i].mult(Utility.Random(0.2f, 1.0f));

            sizes[i] = new Vec2(Utility.Random(2.0f, 9.0f));
            colors[i] = new Color(
                    Utility.Random01() * 0.75f + 0.25f,
                    Utility.Random01() * 0.75f + 0.25f,
                    Utility.Random01() * 0.75f + 0.25f,
                    1);
        }
    }


    public PEmitter update()
    {
        // If failed to create, do nothing
        if(positions == null)
            return this;

        // Update all positions
        for(int i = 0; i < positions.length; i++)
        {
            positions[i] = positions[i].add(velocities[i].mult(speed));
        }

        return this;
    }
    public void draw()
    {
        // If failed to create, do nothing
        if(positions == null)
            return;

        // Time value
        double t = timeAlive / timeAliveMax;
        t = Math.pow(t, 3);

        // Draw all particles
        for(int i = 0; i < positions.length; i++)
        {
            // Apply all color effects at once (Paint colors are really clunky to use)
            Color finalcolor = Utility.colorMult(Utility.colorAdd(colors[i], colorTint), t);

            // Set correct color and draw
            gc.setFill(finalcolor);
            gc.fillRect(positions[i].x - sizes[i].x*0.5f, positions[i].y - sizes[i].y*0.5f, sizes[i].x, sizes[i].y);
        }
    }
}
