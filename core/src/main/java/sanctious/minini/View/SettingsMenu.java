package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Select;
import sanctious.minini.Models.GameAPI;

public class SettingsMenu extends Window {

    public SettingsMenu() {
        super("Settings", GameAPI.getAssetManager().getSkin());
        Skin skin = GameAPI.getAssetManager().getSkin();
        setModal(true);
        setMovable(true);
        setResizable(false);
        pad(20);

        add(new Label("Volume", skin)).left().row();
        Slider volumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        add(volumeSlider).width(300).padBottom(10).row();

        add(new Label("Music", skin)).left().row();
        Carousel<String> music = new Carousel<>(skin);
        music.addEntry("Fragment", "Fragment");
        music.addEntry("Hit", "Hit");
        add(music).width(300).padBottom(10).row();

        add(new Label("Fullscreen", skin)).left().row();
        CheckBox fullscreenCheck = new CheckBox("", skin);
        add(fullscreenCheck).left().row();

        add(new Label("SFX", skin)).left().row();
        CheckBox sfxCheck = new CheckBox("", skin);
        add(sfxCheck).left().row();

        add(new Label("Auto-Reload", skin)).left().row();
        CheckBox autoReloadCheck = new CheckBox("", skin);
        add(autoReloadCheck).left().row();

        add(new Label("Black&White", skin)).left().row();
        CheckBox blackAndWhite = new CheckBox("", skin);
        add(blackAndWhite).left().row();

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove(); // Closes the settings window
            }
        });
        add(closeButton).colspan(2).padTop(15).center().row();

        pack();
        setPosition(
            (Gdx.graphics.getWidth() - getWidth()) / 2f,
            (Gdx.graphics.getHeight() - getHeight()) / 2f
        );
    }
}
