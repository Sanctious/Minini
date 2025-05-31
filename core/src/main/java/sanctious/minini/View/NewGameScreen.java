package sanctious.minini.View;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import sanctious.minini.GameMain;
import sanctious.minini.Models.GameAPI;
import sanctious.minini.Models.User;

public class NewGameScreen extends ManagedScreen {
    private Stage ui = new Stage(new ScreenViewport());
    private Skin skin = GameAPI.getAssetManager().getSkin();

    private TextButton settingsButton;
    private TextButton profileButton;
    private TextButton newGameButton;
    private TextButton scoreBoardButton;

    public NewGameScreen() {
        super();
        addInputProcessor(ui);
        setupUI();
    }

    public void setupUI(){
        Table root = new Table();
        root.setFillParent(true);
        root.center();

        SelectBox<Option<String>> heroes = new SelectBox<Option<String>>(skin);
        heroes.setItems(
            new Option<>("Mechanic", "address here"),
            new Option<>("Sussy", "address here"),
            new Option<>("Amogus", "address here")
        );

        SelectBox<Option<String>> weapons = new SelectBox<Option<String>>(skin);
        weapons.setItems(
            new Option<>("Shotgun", "address here"),
            new Option<>("Ammogun", "address here"),
            new Option<>("Amogus", "address here")
        );

        SelectBox<Option<Integer>> time = new SelectBox<Option<Integer>>(skin);
        time.setItems(
            new Option<>("2 Minutes", 2 * 60),
            new Option<>("5 Minutes", 5 * 60),
            new Option<>("10 Minutes", 10 * 60),
            new Option<>("20 Minutes", 20 * 60)
        );

        TextButton play = new TextButton("Play", skin);
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMain gameMain = GameMain.getInstance();
                gameMain.changeScreen(
                    new GameScreen(time.getSelected().getValue()),
                    null
                );
            }
        });

        root.setWidth(400);
        root.defaults().pad(5);
        root.setBackground(skin.newDrawable("white", Color.DARK_GRAY));

        root.add(heroes).row();
        root.add(weapons).row();
        root.add(time).row();
        root.add(play);

        ui.addActor(root);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.706f * 0.25f, 255, 255, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        ui.getViewport().apply();
        ui.act(delta);
        ui.draw();
    }

    @Override
    public void resize(int width, int height) {
        ui.getViewport().update(width, height, true);
    }


    @Override
    public void dispose() {

    }
}
