package sanctious.minini.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.util.*;
import java.util.List;

public class AssManager extends AssetManager {
//    private AssManager manager = new AssetManager();

    private final Map<String, Texture> avatars = new HashMap<>();
    private final BitmapFont newFont;
    private Skin skin = null;
    {
        skin = new Skin(Gdx.files.internal("ui/neonui/neon-ui.json"));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/neonui/Orbitron.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        newFont = generator.generateFont(parameter);
        generator.dispose();

        avatars.put("Dasher", new Texture(Gdx.files.internal("avatars/Dasher_Portrait.png")));
        avatars.put("Diamond", new Texture(Gdx.files.internal("avatars/Diamond_Portrait.png")));
        avatars.put("Hastur", new Texture(Gdx.files.internal("avatars/Hastur_Portrait.png")));
        avatars.put("Hina", new Texture(Gdx.files.internal("avatars/Hina_Portrait.png")));
        avatars.put("Luna", new Texture(Gdx.files.internal("avatars/Luna_Portrait.png")));
        avatars.put("Raven", new Texture(Gdx.files.internal("avatars/Raven_Portrait.png")));
        avatars.put("Scarlett", new Texture(Gdx.files.internal("avatars/Scarlett_Portrait.png")));

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
//        for (List.ListStyle style : skin.getAll(List.ListStyle.class).values()) {
//            style.font = newFont;
//        }
        for (SelectBox.SelectBoxStyle style : skin.getAll(SelectBox.SelectBoxStyle.class).values()) {
            style.font = newFont;
        }
        for (Window.WindowStyle style : skin.getAll(Window.WindowStyle.class).values()) {
            style.titleFont = newFont;
        }
    }

    public Texture getAvatar(String name){
        return avatars.get(name);
    }

    public void addAvatar(String name, String path){
        this.avatars.put(name, new Texture(Gdx.files.internal(path)));
    }

    public Map<String, Texture> getAvatars() {
        return avatars;
    }

    public String getRandomAvatar(){
        Collection<String> valuesCollection = avatars.keySet();
        List<String> valuesList = new ArrayList<>(valuesCollection);
        Random random = new Random();
        int randomIndex = random.nextInt(valuesList.size());
        return valuesList.get(randomIndex);
    }

    public BitmapFont getNewFont() {
        return newFont;
    }

    public Skin getSkin() {
        return skin;
    }
}
