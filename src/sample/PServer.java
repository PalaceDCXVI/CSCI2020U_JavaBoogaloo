package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class PServer
{
    private static PServer instance;
    public static PServer GetInstance()
    {
        if (instance == null)
        {
            instance = new PServer();
        }

        return instance;
    }

    private ServerSocket serverSocket;
    private Socket socket;
    public static final int serverPort = 20501;

    //Write
    PrintWriter out;

    //Read
    BufferedReader in;

    //Inits the server, returns true if it succeeds.
    public boolean StartServer()
    {
        try {
            serverSocket = new ServerSocket(serverPort);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        //Accept() blocks while waiting for a connection, so we spawn a thread to wait for an incoming message. It's kinda dumb, but functional enough.
        Task<Integer> r = new Task<>()
        {
            @Override
            protected Integer call()
            {
                try {
                    socket = serverSocket.accept();

                    //write messages
                    out = new PrintWriter(socket.getOutputStream(), true);

                    //read messages
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                } catch (IOException e) {
                    e.printStackTrace();

                }

                //This is an annoying thing, but it wouldn't change the scene otherwise.
                Platform.runLater(() -> PData.getInstance().changeScene(PData.PSceneState.GAME));

                return 0;
            }
        };
        Thread th = new Thread(r);
        th.start();

        return true;
    }

    //Send standardized message
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

    //Send specialized message that contains all the information for the particle emitters.
    public String SendMessage(PongGame.ObjectNetId id, Vec2 pos, Integer amnt, Vec2 Dir, Color col)
    {
        if (out == null)
        {
            return "";
        }

        String message = id.toString() + "," + pos.x + "," + pos.y + "," + amnt + "," + Dir.x + "," + Dir.y + "," + col.getRed() + "," + col.getGreen() + "," + col.getBlue();

        out.println(message);

        return "";
    }

    //Receive standardized messages
    public void ReceiveUpdate()
    {
        //Error checking
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


        //Receive message and respond
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

            case PARTICLE:
                PongGame.getInstance().AddEmitter(new Vec2(Float.parseFloat(words[1]), Float.parseFloat(words[2])),  Math.round(Float.parseFloat(words[3])),  new Vec2(Float.parseFloat(words[4]), Float.parseFloat(words[5])), new Color(Float.parseFloat(words[6]), Float.parseFloat(words[7]), Float.parseFloat(words[8]), 1.0));

        }
    }

    public void CloseServer()
    {
        if(socket == null)
            return;

        if(serverSocket == null)
            return;

        try
        {
            socket.close();
            serverSocket.close();
        }
        catch (IOException e)
        {
            System.out.println("Failed to close server.");
        }
    }

}
