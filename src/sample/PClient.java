package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PClient {
    private static PClient instance;
    public static PClient GetInstance()
    {
        if (instance == null)
        {
            instance = new PClient();
        }

        return instance;
    }

    Socket socket;
    String targetIP;
    public static final int clientPort = 20500;

    //Write
    PrintWriter out;

    //Read
    BufferedReader in;

    public boolean Connect(String _targetIP)
    {
        this.targetIP = _targetIP;

        try
        {
            socket = new Socket(targetIP, PServer.serverPort, null, clientPort);

            //write messages
            out = new PrintWriter(socket.getOutputStream(), true);

            //read messages
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e)
        {
            return false;
        }

        return true;
    }

    public String SendMessage(PongGame.ObjectNetId id, Vec2 pos)
    {
        if (out == null)
        {
            return "";
        }

        String message = id.toString() + "," + pos.x + "," + pos.y;

        out.println(message);

        return "";
    }

    public void ReceiveUpdate()
    {
        if (in == null)
        {
            return;
        }

        try {
            if (!in.ready())
            {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line = "";
        try
        {
            line = in.readLine();
        }
        catch (IOException e)
        {

        }

        if (line.equals(""))
        {
            return;
        }

        String[] words = line.split(",");
        PongGame.ObjectNetId objectID = PongGame.ObjectNetId.valueOf(words[0]);

        switch (objectID)
        {
            case BALL:
                PongGame.getInstance().ball.position = new Vec2(Float.parseFloat(words[1]), Float.parseFloat(words[2]));
                break;

            case LPADDLE:
                PongGame.getInstance().leftPaddle.position = new Vec2(Float.parseFloat(words[1]), Float.parseFloat(words[2]));
                break;

            case RPADDLE:
                PongGame.getInstance().rightPaddle.position = new Vec2(Float.parseFloat(words[1]), Float.parseFloat(words[2]));
                break;

            case SCORE:
                PongGame.getInstance().player1score = Math.round(Float.parseFloat(words[1]));
                PongGame.getInstance().player2score = Math.round(Float.parseFloat(words[2]));
                break;

            case RESET:
                PongGame.getInstance().reset();
                break;

        }
    }

    public void CloseClient()
    {
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            System.out.println("Failed to close client.");
        }
    }
}
