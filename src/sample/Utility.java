package sample;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class Utility {
    // String Converters (From String)
    public static int ToInteger(String str)
    {
        try {
            return Integer.parseInt(str.trim());
        } catch (Exception e) {
            System.out.println("ERROR: Cannot convert String: " + str  + ", to Integer");
            return 0;
        }
    }
    public static float ToFloat(String str)
    {
        try {
            return Float.parseFloat(str.trim());
        } catch (Exception e) {
            System.out.println("ERROR: Cannot convert String: " + str  + ", to Float");
            return 0.0f;
        }
    }
    public static double ToDouble(String str)
    {
        try {
            return Double.parseDouble(str.trim());
        } catch (Exception e) {
            System.out.println("ERROR: Cannot convert String: " + str  + ", to Double");
            return 0.0;
        }
    }
    public static double ToLong(String str)
    {
        try {
            return Long.parseLong(str.trim());
        } catch (Exception e) {
            System.out.println("ERROR: Cannot convert String: " + str  + ", to Double");
            return 0.0;
        }
    }

    // String Converters (To String)
    public static String ToString(int value)
    {
        try {
            return Integer.toString(value);
        } catch (Exception e) {
            System.out.println("ERROR: Cannot convert Integer: " + value + ", to String");
            return "";
        }
    }
    public static String ToString(float value)
    {
        try{
            return Float.toString(value);
        }catch(Exception e){
            System.out.println("ERROR: Cannot convert Float: " + value + ", to String");
            return "";
        }
    }
    public static String ToString(double value)
    {
        try{
            return Double.toString(value);
        }catch(Exception e){
            System.out.println("ERROR: Cannot convert Double: " + value + ", to String");
            return "";
        }
    }
    public static String ToString(long value)
    {
        try{
            return Long.toString(value);
        }catch(Exception e){
            System.out.println("ERROR: Cannot convert Long: " + value + ", to String");
            return "";
        }
    }

    // Char to Integer
    public static int ToString(char c) { return Character.getNumericValue(c); }
    public static char ToChar(String str)
    {
        if(str.length() == 0)
            return '?';

        return str.toCharArray()[0];
    }

    // Close Socket
    public static void CloseSocket(Socket socket)
    {
        try{
            if(socket != null)
                socket.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void CloseSocket(ServerSocket socket)
    {
        try{
            if(socket != null)
                socket.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // Create Socket Buffered Reader
    public static BufferedReader CreateBufferedReader(Socket _socket)
    {
        try {
            return new BufferedReader(new InputStreamReader(_socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void log(String message){System.out.println(message);}

    public enum FontType
    {
        VERDANA,
        ARIAL,
        HELEVETICA,
        COURIER,
        TIMESNEWROMAN
    }
    public static Font getFont(FontType t, FontPosture posture, int size)
    {
        String fontFamily = "Arial";

        switch(t) {
            case VERDANA:
                fontFamily = "Verdana";
                break;

            case ARIAL:
                fontFamily = "Arial";
                break;

            case HELEVETICA:
                fontFamily = "Helvetica";
                break;

            case COURIER:
                fontFamily = "Courier";
                break;

            case TIMESNEWROMAN:
                fontFamily = "Times New Roman";
                break;
        }

        return Font.font(fontFamily, posture, size);
    }

    public static void PrintAllFontFamilies()
    {
        System.out.println(javafx.scene.text.Font.getFamilies());
    }

    // Function taken from
    // https://stackoverflow.com/questions/4581877/validating-ipv4-string-in-java
    public static boolean validIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    // Linear Interpolation
    public static float lerp(float v0, float v1, float t)
    {
        return (1.0f - t) * v0 + t * v1;
    }
    public static double lerp(double v0, double v1, double t)
    {
        return (1.0 - t) * v0 + t * v1;
    }

    // Linear Interpolation for color
    public static Color lerp(Color c0, Color c1, double t)
    {
        double r = lerp(c0.getRed(), c1.getRed(), t);
        double g = lerp(c0.getGreen(), c1.getGreen(), t);
        double b = lerp(c0.getBlue(), c1.getBlue(), t);
        return new Color(r, g, b, 1);
    }

    // Random Number
    public static int Random(int min, int max)
    {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    public static float Random01()
    {
        return ThreadLocalRandom.current().nextFloat();
    }
    public static float Random(float min, float max)
    {
        return ThreadLocalRandom.current().nextFloat() * (max - min) + min;
    }

    // Color Multiplication
    public static Color colorMult(Color c1, Color c2)
    {
        return new Color(
                Math.min(c1.getRed() * c2.getRed(), 1.0),
                Math.min(c1.getBlue() * c2.getGreen(), 1.0),
                Math.min(c1.getGreen() * c2.getBlue(), 1.0),
                Math.min(c1.getBrightness() * c2.getBrightness(), 1.0)
        );
    }
    public static Color colorMult(Color c1, float c2)
    {
        return new Color(
                Math.min(c1.getRed() * c2, 1.0),
                Math.min(c1.getBlue() * c2, 1.0),
                Math.min(c1.getGreen() * c2, 1.0),
                Math.min(c1.getBrightness() * c2, 1.0)
        );
    }
    public static Color colorMult(Color c1, double c2)
    {
        return new Color(
                Math.min(c1.getRed() * c2, 1.0),
                Math.min(c1.getBlue() * c2, 1.0),
                Math.min(c1.getGreen() * c2, 1.0),
                Math.min(c1.getBrightness() * c2, 1.0)
        );
    }

    // Color Addition
    public static Color colorAdd(Color c1, Color c2)
    {
        return new Color(
                Math.min(c1.getRed() + c2.getRed(), 1.0),
                Math.min(c1.getBlue() + c2.getBlue(), 1.0),
                Math.min(c1.getGreen() + c2.getGreen(), 1.0),
                Math.min(c1.getBrightness() + c2.getBrightness(), 1.0)
        );
    }
    public static Color colorAdd(Color c1, float c2)
    {
        return new Color(
                Math.min(c1.getRed() + c2, 1.0),
                Math.min(c1.getBlue() + c2, 1.0),
                Math.min(c1.getGreen() + c2, 1.0),
                Math.min(c1.getBrightness() + c2, 1.0)
                );
    }
    public static Color colorAdd(Color c1, double c2)
    {
        return new Color(
                Math.min(c1.getRed() + c2, 1.0),
                Math.min(c1.getBlue() + c2, 1.0),
                Math.min(c1.getGreen() + c2, 1.0),
                Math.min(c1.getBrightness() + c2, 1.0)
                );
    }


}
