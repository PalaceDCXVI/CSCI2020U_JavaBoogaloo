package Scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import sample.HighScore;
import sample.PData;

public class Scene_Highscore extends Scene_Base
{
    private BorderPane root;
    private TableView<HighScore> table;

    public Scene_Highscore()
    {
        SetupScene();

    }

    public void SetupScene()
    {
        root = new BorderPane();

        table = new TableView<>();
        table.setItems(PData.getInstance().getHighscores());
        table.setEditable(false);
        table.setMaxWidth(305);

        TableColumn<HighScore, String> userColumn = new TableColumn<>("Username");
        userColumn.setMinWidth(200);
        userColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));

        TableColumn<HighScore, Float> timeColumn = new TableColumn<>("Game Time");
        timeColumn.setMinWidth(100);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("GameTime"));

        table.getColumns().addAll(userColumn, timeColumn);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> OnAction_Back());

        root.setCenter(table);
        root.setBottom(backButton);
        BorderPane.setMargin(backButton, new Insets(5));
        BorderPane.setAlignment(backButton, Pos.CENTER);
        root.setPadding(new Insets(5, 5, 5, 5));
        m_scene = new Scene(root, PData.getInstance().AppWidth, PData.getInstance().AppHeight, Color.BLACK);
    }

    public void SetupHandlers()
    {
        // Player Input (KEY PRESS)
        m_scene.setOnKeyPressed((key) ->
        {
            // Escape
            if(key.getCode() == KeyCode.ESCAPE)
                OnAction_Back();
        });
    }

    public void OnAction_Back()
    {
        PData.getInstance().changeScene(PData.PSceneState.MENU);
    }
}
