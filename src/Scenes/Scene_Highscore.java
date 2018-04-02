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
        SetupHandlers();
    }

    public void SetupScene()
    {
        root = new BorderPane();

        table = new TableView<>();
        table.setItems(PData.getInstance().getHighscores());
        table.setEditable(false);
        table.setMaxWidth(555);

        TableColumn<HighScore, String> p1Column = new TableColumn<>("Player 1");
        p1Column.setMinWidth(150);
        p1Column.setCellValueFactory(new PropertyValueFactory<>("Player1"));

        TableColumn<HighScore, String> p1ScoreCol = new TableColumn<>("P1 Score");
        p1ScoreCol.setMinWidth(50);
        p1ScoreCol.setCellValueFactory(new PropertyValueFactory<>("P1score"));

        TableColumn<HighScore, String> p2ScoreCol = new TableColumn<>("P2 Score");
        p2ScoreCol.setMinWidth(50);
        p2ScoreCol.setCellValueFactory(new PropertyValueFactory<>("P2score"));

        TableColumn<HighScore, String> p2Column = new TableColumn<>("Player 2");
        p2Column.setMinWidth(150);
        p2Column.setCellValueFactory(new PropertyValueFactory<>("Player2"));

        TableColumn<HighScore, Float> timeColumn = new TableColumn<>("Game Time");
        timeColumn.setMinWidth(100);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("GameTime"));

        table.getColumns().addAll(p1Column, p1ScoreCol, p2ScoreCol, p2Column, timeColumn);
        table.setId("score-table");

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> OnAction_Back());
        backButton.setId("button");

        root.setCenter(table);
        root.setBottom(backButton);
        BorderPane.setMargin(backButton, new Insets(5));
        BorderPane.setAlignment(backButton, Pos.CENTER);
        root.setPadding(new Insets(5, 5, 5, 5));
        CreateScene(root);
        m_scene.getStylesheets().add("stylesheet.css");
    }

    public void RefreshScores()
    {
        table.setItems(PData.getInstance().getHighscores());
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
