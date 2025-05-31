package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import org.w3c.dom.Text;
import sanctious.minini.GameMain;
import sanctious.minini.Models.GameAPI;
import sanctious.minini.Models.User;
import sanctious.minini.Models.UserRegistry;

public class MainMenuScreen extends ManagedScreen {
    private Stage ui = new Stage(new ScreenViewport());
    private Skin skin = GameAPI.getAssetManager().getSkin();

    private TextButton settingsButton;
    private TextButton profileButton;
    private TextButton newGameButton;
    private TextButton loadGameButton;
    private TextButton scoreBoardButton;
    private TextButton logoutButton;
    private TextButton helpButton;

    private Image userPicture;
    private Label userStats;

    public MainMenuScreen() {
        super();
        addInputProcessor(ui);
        setupUI();
    }

    public void setupUI(){
        Table root = new Table();
        root.setFillParent(true);
        root.center();

        settingsButton = new TextButton("Settings", skin);

        profileButton = new TextButton("Profile", skin);
        profileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (GameAPI.getUserRegistry().getActiveUser().getUsername().equals("Guest")) return;

                GameMain gameMain = GameMain.getInstance();
                gameMain.changeScreen(
                    new ProfileMenuScreen(),
                    new BlendingTransition(gameMain.getSpriteBatch(), 1F, Interpolation.pow2In)
                );
            }
        });

        newGameButton = new TextButton("New Game", skin);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMain gameMain = GameMain.getInstance();
                gameMain.changeScreen(
                    new NewGameScreen(),
                    new BlendingTransition(gameMain.getSpriteBatch(), 1F, Interpolation.pow2In)
                );
            }
        });

        loadGameButton = new TextButton("Load Game", skin);

        helpButton = new TextButton("Hint Menu", skin);

        scoreBoardButton = new TextButton("Scoreboard", skin);
        scoreBoardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMain gameMain = GameMain.getInstance();
                gameMain.changeScreen(
                    new ScoreBoardMenuScreen(),
                    new BlendingTransition(gameMain.getSpriteBatch(), 1F, Interpolation.pow2In)
                );
            }
        });

        logoutButton = new TextButton("Logout", skin);

        User user = GameAPI.getUserRegistry().getActiveUser();
//        userPicture = new Image();
        userStats = new Label(String.format("Name: %s\nPoints: %d", user.getUsername(), user.getData().getTotalPoints()), skin);


        Table mainTable = new Table();
        mainTable.setWidth(400);
        mainTable.defaults().pad(5);
        mainTable.setBackground(skin.newDrawable("white", Color.DARK_GRAY));

        mainTable.add(settingsButton);
        mainTable.add(profileButton);
        mainTable.add(newGameButton);
        mainTable.add(loadGameButton);
        mainTable.add(scoreBoardButton);
        mainTable.add(helpButton);
        mainTable.add(logoutButton);

        root.add(userPicture);
        root.add(userStats);


        root.add(mainTable);
        ui.addActor(root);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.706f * 0.25f, 0.851f * 0.25f, 0.847f * 0.25f, 1);
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
