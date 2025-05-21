package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import sanctious.minini.GameMain;
import sanctious.minini.Models.GameAPI;
import sanctious.minini.Models.User;

public class ScoreBoardMenuScreen extends ManagedScreen {
    private Stage ui = new Stage(new ScreenViewport());
    private Skin skin = GameAPI.getAssetManager().getSkin();

    private TextButton settingsButton;
    private TextButton profileButton;
    private TextButton newGameButton;
    private TextButton scoreBoardButton;

    public ScoreBoardMenuScreen() {
        super();
        addInputProcessor(ui);
        setupUI();
    }

    public void setupUI(){
        Table root = new Table();
        root.setFillParent(true);
        root.center();

        TextButton sortByUsername = new TextButton("Username", skin);
        sortByUsername.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateScoreBoard("username");
            }
        });

        TextButton sortByKills = new TextButton("Kills", skin);
        sortByKills.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateScoreBoard("kills");
            }
        });

        TextButton sortByMoney = new TextButton("Money", skin);
        sortByMoney.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateScoreBoard("money");
            }
        });



        TextButton back = new TextButton("Back", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMain gameMain = GameMain.getInstance();
                gameMain.changeScreen(
                    // TODO how to fix it ?
                    new MainMenuScreen(),
                    new BlendingTransition(gameMain.getSpriteBatch(), 1F, Interpolation.pow2In)
                );
            }
        });

        root.setWidth(400);
        root.defaults().pad(5);
        root.setBackground(skin.newDrawable("white", Color.DARK_GRAY));

        root.add(sortByUsername);
        root.add(sortByKills);
        root.add(sortByMoney);
        root.add(back);

        ui.addActor(root);
    }

    public void updateScoreBoard(String sortMethod){
        switch(sortMethod){
            case "username": {

            }
            case "kills": {

            }
            case "money": {

            }
        }

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
