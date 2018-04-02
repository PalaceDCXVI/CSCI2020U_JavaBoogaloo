package sample;

public class HighScore {
    private String Username;
    private float GameTime;

    public HighScore(String username, float gametime)
    {
        Username = username;
        GameTime = gametime;
    }

    public String getUsername() {return Username;}
    public float getGameTime() {return GameTime;}
}
