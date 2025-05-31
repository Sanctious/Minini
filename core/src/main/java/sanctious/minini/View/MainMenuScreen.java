package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import sanctious.minini.GameMain;
import sanctious.minini.Models.GameAPI;
import sanctious.minini.Models.User;

public class MainMenuScreen extends ManagedScreen {
    private Stage uiStage;
    private Skin skin;

    public MainMenuScreen() {
        super();
        this.uiStage = new Stage(new ScreenViewport());
        this.skin = GameAPI.getAssetManager().getSkin();
        addInputProcessor(uiStage);
        setupUI();
    }

    public void setupUI() {
        User currentUser = GameAPI.getUserRegistry().getActiveUser();
        if (currentUser == null) {
            Gdx.app.error("MainMenuScreen", "Critical: Active user is null.");
            return;
        }

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();
        rootTable.pad(20f);

        Table userInfoTable = new Table(skin);
        userInfoTable.setBackground(skin.newDrawable("white", new Color(0.2f, 0.2f, 0.2f, 0.5f)));
        userInfoTable.pad(10f);

        Image userPicture = new Image(GameAPI.getAssetManager().getAvatar(currentUser.getAvatar()));
        Label userStatsLabel = new Label(String.format("%s\nScore: %d", currentUser.getUsername(), currentUser.getData().getTotalPoints()), skin);
        userStatsLabel.setAlignment(Align.left);

        userInfoTable.add(userPicture).size(64, 64).padRight(15f);
        userInfoTable.add(userStatsLabel).expandX().left();

        rootTable.add(userInfoTable).prefWidth(350f).padBottom(30f).row();

        Table menuButtonsTable = new Table();
        menuButtonsTable.defaults().pad(8f).width(300f).fillX();

        TextButton newGameButton = new TextButton("New Game", skin);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMain.getInstance().changeScreen(
                    new NewGameScreen(),
                    new BlendingTransition(GameMain.getInstance().getSpriteBatch(), 1F, Interpolation.pow2In)
                );
            }
        });

        TextButton loadGameButton = new TextButton("Load Game", skin);

        TextButton scoreBoardButton = new TextButton("Scoreboard", skin);
        scoreBoardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMain.getInstance().changeScreen(
                    new ScoreBoardMenuScreen(),
                    new BlendingTransition(GameMain.getInstance().getSpriteBatch(), 1F, Interpolation.pow2In)
                );
            }
        });

        TextButton profileButton = new TextButton("Profile", skin);
        if (currentUser.getUsername().equalsIgnoreCase("Guest")) {
            profileButton.setDisabled(true);
        } else {
            profileButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    GameMain.getInstance().changeScreen(
                        new ProfileMenuScreen(),
                        new BlendingTransition(GameMain.getInstance().getSpriteBatch(), 1F, Interpolation.pow2In)
                    );
                }
            });
        }

        TextButton settingsButton = new TextButton("Settings", skin);

        TextButton helpButton = new TextButton("Help/Hints", skin);

        TextButton logoutButton = new TextButton("Logout", skin);
        logoutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameAPI.getUserRegistry().setActiveUser(null);
                GameMain.getInstance().changeScreen(
                    new RegisterMenuScreen(),
                    new BlendingTransition(GameMain.getInstance().getSpriteBatch(), 1F, Interpolation.pow2In)
                );
            }
        });

        menuButtonsTable.add(newGameButton).row();
        menuButtonsTable.add(loadGameButton).row();
        menuButtonsTable.add(scoreBoardButton).row();
        menuButtonsTable.add(profileButton).row();
        menuButtonsTable.add(settingsButton).row();
        menuButtonsTable.add(helpButton).row();
        menuButtonsTable.add(logoutButton).row();

        rootTable.add(menuButtonsTable).row();

        uiStage.addActor(rootTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1765f, 0.21275f, 0.21175f, 1f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        uiStage.getViewport().apply();
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (uiStage != null) {
            uiStage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void dispose() {
        if (uiStage != null) {
            uiStage.dispose();
        }
    }
}
