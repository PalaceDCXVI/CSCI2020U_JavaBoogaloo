package sample;

public class HighScore {
    private String player1, player2;
    private int p1score, p2score;
    private float GameTime;

    public HighScore(String p1name, int p1score, String p2name, int p2score, float gametime)
    {
        this.player1 = p1name;
        this.player2 = p2name;
        this.p1score = p1score;
        this.p2score = p2score;
        GameTime = gametime;
    }

    public String getPlayer1() {return player1;}
    public String getPlayer2() {return player2;}
    public int getP1score() {return p1score;}
    public int getP2score() {return p2score;}
    public float getGameTime() {return GameTime;}
    public String toString()
    {
        return player1 + "," + p1score + "," + player2 + "," + p2score + "," + GameTime;
    }
}
