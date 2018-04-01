package sample;

import java.util.concurrent.ThreadLocalRandom;

public class RandomNames
{
    public static String GetName()
    {
        int randomNum = ThreadLocalRandom.current().nextInt(0, Names.length);
        return Names[randomNum];
    }

    public static String[] Names =
    {
             "Venice Vaca",
             "Danita Doggett",
             "Mireya Mcpherson",
             "Chas Camp",
             "Jolanda Jawad",
             "Mercedez Munsey",
             "Maud Mennenga",
             "Rolland Romo",
             "Isaiah Ishida",
             "Merrill Mehring",
             "Kyle Kenner",
             "Mila Mccullers",
             "Magdalene Mcclain",
             "Dennis Dockstader",
             "Summer Scheer",
             "Cristal Carlson",
             "Nona Nagy",
             "Tracey Toro",
             "Bernard Buttars",
             "Shavonne Shrout",
             "Narcisa Norton",
             "Hai Heinze",
             "Tanesha Trumbauer",
             "Roselle Robey",
             "Lashonda Lett",
             "Angelia Austria",
             "Violet Vanfossen",
             "Lorrine Ladson",
             "Mirian Moudy",
             "Zana Zemlicka",
             "Tamatha Tea",
             "Soon Snuggs",
             "Latrina Lawless",
             "Refugio Ramerez",
             "Harlan Hagwood",
             "Boyce Body",
             "Virgil Vos",
             "Season Sabella",
             "Kenyatta Kruger",
             "Aline Anspach",
             "Lael Lash",
             "Vinita Vecchio",
             "Dotty Detamore",
             "Casey Caine",
             "Clint Crumpton",
             "Chong Charleston",
             "Kerry Kim",
             "Nickole Neff",
             "Erlinda Ellisor",
             "Aura Ackman"
    };
}
