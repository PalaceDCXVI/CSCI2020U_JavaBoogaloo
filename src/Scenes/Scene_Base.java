package Scenes;

import javafx.beans.NamedArg;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import sample.Main;
import sample.PData;

public class Scene_Base
{
    public Scene m_scene;

    public Scene GetScene(){return m_scene;}

    public void CreateScene(@NamedArg("root") Parent root)
    {
        m_scene = new Scene(root, PData.getInstance().AppWidth, PData.getInstance().AppHeight, Color.BLACK);;
    }

}
