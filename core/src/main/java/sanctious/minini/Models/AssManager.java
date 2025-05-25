package sanctious.minini.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class AssManager extends AssetManager {
//    private AssManager manager = new AssetManager();

    private final String[] avatars = {};
    private Skin skin = null;
    {
        skin = new Skin(Gdx.files.internal("ui/neonui/neon-ui.json"));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/neonui/Orbitron.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        BitmapFont newFont = generator.generateFont(parameter);
        generator.dispose();

        for (Label.LabelStyle style : skin.getAll(Label.LabelStyle.class).values()) {
            style.font = newFont;
        }
        for (TextButton.TextButtonStyle style : skin.getAll(TextButton.TextButtonStyle.class).values()) {
            style.font = newFont;
        }
        for (CheckBox.CheckBoxStyle style : skin.getAll(CheckBox.CheckBoxStyle.class).values()) {
            style.font = newFont;
        }
        for (TextField.TextFieldStyle style : skin.getAll(TextField.TextFieldStyle.class).values()) {
            style.font = newFont;
        }
        for (List.ListStyle style : skin.getAll(List.ListStyle.class).values()) {
            style.font = newFont;
        }
        for (SelectBox.SelectBoxStyle style : skin.getAll(SelectBox.SelectBoxStyle.class).values()) {
            style.font = newFont;
        }
        for (Window.WindowStyle style : skin.getAll(Window.WindowStyle.class).values()) {
            style.titleFont = newFont;
        }
    }

    public String[] getAvatars() {
        return avatars;
    }

    public Skin getSkin() {
        return skin;
    }
}
