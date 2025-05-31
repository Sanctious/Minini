package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import sanctious.minini.Models.GameAPI;

public class SettingsMenu extends Window {

    private static final String PREFS_NAME = "game_settings";
    private static final String KEY_VOLUME = "volume";
    private static final String KEY_MUSIC_TRACK = "music_track";
    private static final String KEY_FULLSCREEN = "fullscreen";
    private static final String KEY_SFX_MUTED = "sfx_muted";
    private static final String KEY_AUTO_RELOAD = "auto_reload";
    private static final String KEY_BW_MODE = "bw_mode";

    private static final int DEFAULT_WINDOW_WIDTH = 1280;
    private static final int DEFAULT_WINDOW_HEIGHT = 720;

    private Preferences prefs;

    private Slider volumeSlider;
    private SelectBox<String> musicSelectBox;
    private CheckBox fullscreenCheck;
    private CheckBox sfxCheck;
    private CheckBox autoReloadCheck;
    private CheckBox blackAndWhiteCheck;

    public SettingsMenu() {
        super("Settings", GameAPI.getAssetManager().getSkin());
        Skin skin = GameAPI.getAssetManager().getSkin();
        prefs = Gdx.app.getPreferences(PREFS_NAME);

        setModal(true);
        setMovable(true);
        setResizable(false);

        Table contentTable = new Table(skin);
        contentTable.pad(10f);

        float controlWidth = 250f;
        float labelWidth = 120f;
        float bottomPad = 10f;

        volumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        volumeSlider.setValue(prefs.getFloat(KEY_VOLUME, 0.75f));
        applyVolumeSetting(volumeSlider.getValue());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = volumeSlider.getValue();
                applyVolumeSetting(volume);
                prefs.putFloat(KEY_VOLUME, volume);
                prefs.flush();
            }
        });
        contentTable.add(new Label("Master Volume:", skin)).width(labelWidth).left();
        contentTable.add(volumeSlider).width(controlWidth).padBottom(bottomPad).row();

        musicSelectBox = new SelectBox<>(skin);
        musicSelectBox.setItems("Fragment", "Hit", "Epic Battle", "No Music");
        String savedMusicTrack = prefs.getString(KEY_MUSIC_TRACK, "Fragment");
        musicSelectBox.setSelected(savedMusicTrack);
        applyMusicTrackSetting(savedMusicTrack);
        musicSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedMusic = musicSelectBox.getSelected();
                applyMusicTrackSetting(selectedMusic);
                prefs.putString(KEY_MUSIC_TRACK, selectedMusic);
                prefs.flush();
            }
        });
        contentTable.add(new Label("Music Track:", skin)).width(labelWidth).left();
        contentTable.add(musicSelectBox).width(controlWidth).padBottom(bottomPad).row();

        fullscreenCheck = new CheckBox(" Enable Fullscreen", skin);
        fullscreenCheck.setChecked(prefs.getBoolean(KEY_FULLSCREEN, false));
        if (Gdx.graphics.isFullscreen() && !fullscreenCheck.isChecked()) fullscreenCheck.setChecked(true);
        else if (!Gdx.graphics.isFullscreen() && fullscreenCheck.isChecked()) fullscreenCheck.setChecked(false);

        fullscreenCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isFullscreen = fullscreenCheck.isChecked();
                applyFullscreenSetting(isFullscreen);
                prefs.putBoolean(KEY_FULLSCREEN, isFullscreen);
                prefs.flush();
            }
        });
        contentTable.add(fullscreenCheck).colspan(2).left().padBottom(bottomPad).row();

        sfxCheck = new CheckBox(" Enable SFX", skin);
        boolean sfxOn = !prefs.getBoolean(KEY_SFX_MUTED, false);
        sfxCheck.setChecked(sfxOn);
        applySfxSetting(sfxOn);
        sfxCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enableSfx = sfxCheck.isChecked();
                applySfxSetting(enableSfx);
                prefs.putBoolean(KEY_SFX_MUTED, !enableSfx);
                prefs.flush();
            }
        });
        contentTable.add(sfxCheck).colspan(2).left().padBottom(bottomPad).row();

        autoReloadCheck = new CheckBox(" Enable Auto-Reload", skin);
        autoReloadCheck.setChecked(prefs.getBoolean(KEY_AUTO_RELOAD, true));
        applyAutoReloadSetting(autoReloadCheck.isChecked());
        autoReloadCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean autoReload = autoReloadCheck.isChecked();
                applyAutoReloadSetting(autoReload);
                prefs.putBoolean(KEY_AUTO_RELOAD, autoReload);
                prefs.flush();
            }
        });
        contentTable.add(autoReloadCheck).colspan(2).left().padBottom(bottomPad).row();

        blackAndWhiteCheck = new CheckBox(" Enable Black & White Mode", skin);
        blackAndWhiteCheck.setChecked(prefs.getBoolean(KEY_BW_MODE, false));
        applyBlackAndWhiteSetting(blackAndWhiteCheck.isChecked());
        blackAndWhiteCheck.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean bwMode = blackAndWhiteCheck.isChecked();
                applyBlackAndWhiteSetting(bwMode);
                prefs.putBoolean(KEY_BW_MODE, bwMode);
                prefs.flush();
            }
        });
        contentTable.add(blackAndWhiteCheck).colspan(2).left().padBottom(bottomPad).row();

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
        contentTable.add(closeButton).colspan(2).padTop(15f).center();

        this.add(contentTable);
        pack();

        setPosition(
            (Gdx.graphics.getWidth() - getWidth()) / 2f,
            (Gdx.graphics.getHeight() - getHeight()) / 2f
        );
    }

    private void applyVolumeSetting(float volume) {
        Gdx.app.log("SettingsMenu", "Applying volume: " + volume);
    }

    private void applyMusicTrackSetting(String trackName) {
        Gdx.app.log("SettingsMenu", "Applying music track: " + trackName);
    }

    private void applyFullscreenSetting(boolean enableFullscreen) {
        Gdx.app.log("SettingsMenu", "Applying fullscreen: " + enableFullscreen);
        if (enableFullscreen) {
            DisplayMode currentMode = Gdx.graphics.getDisplayMode();
            if (!Gdx.graphics.setFullscreenMode(currentMode)) {
                Gdx.app.error("SettingsMenu", "Failed to enter fullscreen mode.");
                fullscreenCheck.setChecked(false);
            }
        } else {
            if (!Gdx.graphics.setWindowedMode(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT)) {
                Gdx.app.error("SettingsMenu", "Failed to enter windowed mode.");
                fullscreenCheck.setChecked(true);
            }
        }
    }

    private void applySfxSetting(boolean enableSfx) {
        Gdx.app.log("SettingsMenu", "Applying SFX enabled: " + enableSfx);
    }

    private void applyAutoReloadSetting(boolean enableAutoReload) {
        Gdx.app.log("SettingsMenu", "Applying auto-reload: " + enableAutoReload);
    }

    private void applyBlackAndWhiteSetting(boolean enableBnW) {
        Gdx.app.log("SettingsMenu", "Applying B&W mode: " + enableBnW);
    }
}
