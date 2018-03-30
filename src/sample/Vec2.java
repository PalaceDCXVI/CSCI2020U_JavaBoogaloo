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
        return (float)Math.sqrt(x * x + y * y);
    }
    public float length()
    {
        return magnitude();
    }
    static public Vec2 normalize(Vec2 v)
    {
        Vec2 newVec = new Vec2();
        float m = v.magnitude();
        if(m == 0)
            return newVec;
        newVec.x = v.x /= m;
        newVec.y = v.y /= m;
        return newVec;
    }
    public Vec2 getNormalized()
    {
        Vec2 newVec = new Vec2();
        float m = magnitude();
        if(m == 0)
            return newVec;
        newVec.x = x /= m;
        newVec.y = y /= m;
        return newVec;
    }
    public void normalizeSelf()
    {
        float m = magnitude();
        if(m == 0)
            return;
        x /= m;
        y /= m;
    }

    public Vec2 reflect(Vec2 normal)
    {
        float d = this.dot(normal);
        Vec2 d2n = normal.mult(2 * d);
        return this.subt(d2n);
    }

    static public float dot(Vec2 a, Vec2 b)
    {
        return a.x * b.x + a.y * b.y;
    }
    public float dot(Vec2 v)
    {
        return x * v.x + y * v.y;
    }
    public float dot()
    {
        return x * x + y * y;
    }

    // Organize (smallest number = x, biggest number = y)
    public void ascendingSelf()
    {
        if(x > y)
        {
            float temp = x;
            x = y;
            y = temp;
        }
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
        if(v.length() == 0)
            return this;

        return new Vec2(x / v.x, y / v.y);
    }

    public Vec2 add(float v)
    {
        return new Vec2(x + v, y + v);
    }
    public Vec2 subt(float v)
    {
        return new Vec2(x - v, y - v);
    }
    public Vec2 mult(float v)
    {
        return new Vec2(x * v, y * v);
    }
    public Vec2 div(float v)
    {
        if(v == 0)
            return this;

        return new Vec2(x / v, y / v);
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
