package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import sanctious.minini.Controllers.Controllers;
import sanctious.minini.GameMain;
import sanctious.minini.Models.GameAPI;
import sanctious.minini.Models.ViewResult;

public class LoginMenuScreen extends ManagedScreen {

    private final Stage ui = new Stage(new ExtendViewport(1920, 1080));
    Skin skin = GameAPI.getAssetManager().getSkin();

    private float delay = 1.2f;

    private TypingLabel label;

    private TextField usernameField;
    private TextField passwordField;
    private TextButton loginButton;
    private TextButton forgetPasswordButton;

    private Group overlayGroup;


    private boolean sceneSwitched;

    public LoginMenuScreen() {
        super();
        addInputProcessor(ui);

        setupUi();
    }


    private void setupUi() {
        Table root = new Table();
        root.setFillParent(true);

//        popup = new Dialog("State:", skin);
//        root.add(popup);

        label = new TypingLabel("{EASE}{SPEED=SLOWER}Glad that you arrived!{SPEED}\n" +
            "{ENDEASE}{COLOR=#743f39}Firebread{CLEARCOLOR} and {COLOR=#0095e9}Icebread{CLEARCOLOR} need your {RAINBOW}help!{ENDRAINBOW}\n" +
            "It looks like the evil {SHAKE}snail king{ENDSHAKE}...\n" +
            "{SPEED=SLOWER}...merged{SPEED} their worlds together!\n" +
            "Only {RAINBOW}you{ENDRAINBOW} can help them out of this mess...\n\n" +
            "{SPEED=SLOW}{SLIDE}save them please!{ENDSLIDE}{SPEED}\n\n" +
            "Ps. {JUMP}Jump{ENDJUMP} onto enemy souls to destroy them!{WAIT=5} ", skin);


        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username");
        passwordField = new TextField("", skin);
        passwordField.setMessageText("Enter password");
        loginButton = new TextButton("Login", skin);
        forgetPasswordButton = new TextButton("Forget Password", skin);

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ViewResult<Void> result = Controllers.getLoginMenuController().login(usernameField.getText(), passwordField.getText());

                if (!result.isSuccess()){
                    label.restart();
                    label.setText("{EASE}{SPEED=8}{COLOR=#FF0000}" + result.getMessage());
                } else {
                    GameMain gameMain = GameMain.getInstance();
                    gameMain.getScreenManager().pushScreen(new MainMenuScreen(), new BlendingTransition(gameMain.getSpriteBatch(), 1F, Interpolation.pow2In));
                }
            }
        });

        forgetPasswordButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFullscreenPopup();
            }
        });


        root.pad(10).top().left();

        root.add(label).colspan(2).expandX().left().row();
        root.add(usernameField).width(300).padTop(20).center();
        root.row();
        root.add(passwordField).width(300).padTop(10).center();
        root.row();
        root.add(loginButton).width(200).padTop(20).center();
        root.row();
        root.add(forgetPasswordButton).width(200).padTop(20).center();


        ui.addActor(root);
    }

    private void showFullscreenPopup() {
        // Clear any existing overlay
        if (overlayGroup != null) overlayGroup.remove();

        overlayGroup = new Group();

        // Create a semi-transparent black background to dim the screen
//        Image dimBackground = new Image(new TextureRegionDrawable(new TextureRegion(
//            new Texture(Gdx.files.internal("white.png"))))); // white.png: a plain white texture
//        dimBackground.setSize(ui.getWidth(), ui.getHeight());
//        dimBackground.setColor(0, 0, 0, 0.7f); // RGBA for 70% opacity black
//        overlayGroup.addActor(dimBackground);

        // Create a centered popup window
        Window popup = new Window("Popup Menu", skin);
        popup.setSize(300, 200);
        popup.setPosition(
            (ui.getWidth() - popup.getWidth()) / 2,
            (ui.getHeight() - popup.getHeight()) / 2
        );

        // Add buttons or content
        TextButton closeBtn = new TextButton("Close", skin);
        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                overlayGroup.remove();
            }
        });

        popup.add(closeBtn).pad(10);
        overlayGroup.addActor(popup);

        // Optional: make it block input to anything behind
        overlayGroup.setTouchable(Touchable.enabled);
//        dimBackground.setTouchable(Touchable.enabled); // catches clicks

        ui.addActor(overlayGroup);
    }

    @Override
    public void show() {
        super.show();

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.706f * 0.25f, 0.851f * 0.25f, 0.847f * 0.25f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        if ((delay -= delta) > 0)
            return;

        if (Gdx.input.isTouched())
            label.skipToTheEnd();

        if (label.hasEnded() && !sceneSwitched) {
            sceneSwitched = true;
        }

        ui.getViewport().apply();
        ui.act(delta);
        ui.draw();
    }

    @Override
    public void resize(int width, int height) {
        ui.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
