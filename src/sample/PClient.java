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
    int clientSocket = 20500;

    //Write
    PrintWriter out;

    //Read
    BufferedReader in;

    public boolean Connect(String _targetIP) throws IOException
    {
        this.targetIP = _targetIP;

        try {
            socket = new Socket(targetIP, clientSocket);
        }
        catch (IOException e)
        {
            return false;
        }

        //write messages
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        //read messages
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        return true;
    }


}
