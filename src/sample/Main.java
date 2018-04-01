package sample;

import Scenes.Scene_MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application
{

    public static Stage primaryStage;

    public void start(Stage primaryStage) throws Exception
    {
        this.primaryStage = primaryStage;

        Scene_MainMenu mainMenu = new Scene_MainMenu();

        primaryStage.setScene(mainMenu.GetScene());
        //mainMenu.SetAsMainScene();

        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
