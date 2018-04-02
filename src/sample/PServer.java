package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;

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

                Platform.runLater(() -> PData.getInstance().changeScene(PData.PSceneState.GAME));

                return 0;
            }
        };
        Thread th = new Thread(r);
        th.start();

        return true;
    }

    public String GetMessage()
    {
        String line = "";
        try {
            line = in.readLine();
        }
        catch (IOException e)
        {
            return "";
        }

        return line;
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
