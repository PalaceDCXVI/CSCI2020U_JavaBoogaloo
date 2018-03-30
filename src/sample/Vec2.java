package sample;

public class Vec2
{
    public float x;
    public float y;

    public Vec2()
    {
        x = 0.0f;
        y = 0.0f;
    }
    public Vec2(float _all)
    {
        x = _all;
        y = _all;
    }
    public Vec2(float _x, float _y)
    {
        x = _x;
        y = _y;
    }

    // Utility
    public float magnitude()
    {
        return (float)Math.sqrt(x * x + y + y);
    }
    public float length()
    {
        return magnitude();
    }
    static public Vec2 normalize(Vec2 v)
    {
        Vec2 newVec = new Vec2();
        float m = v.magnitude();
        newVec.x = v.x /= m;
        newVec.y = v.y /= m;
        return newVec;
    }

    // Math operations
    public Vec2 add(Vec2 v)
    {
        return new Vec2(x + v.x, y + v.y);
    }
    public Vec2 subt(Vec2 v)
    {
        return new Vec2(x - v.x, y - v.y);
    }
    public Vec2 mult(Vec2 v)
    {
        return new Vec2(x * v.x, y * v.y);
    }
    public Vec2 div(Vec2 v)
    {
        return new Vec2(x / v.x, y / v.y);
    }
    // Copy
    public Vec2(Vec2 v)
    {
        set(v);
    }
    public void set(Vec2 v)
    {
        x = v.x;
        y = v.y;
    }
    public void set(float _x, float _y)
    {
        x = _x;
        y = _y;
    }

}
