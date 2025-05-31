package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import sanctious.minini.Controllers.Controllers;
import sanctious.minini.GameMain;
import sanctious.minini.Models.GameAPI;
import sanctious.minini.Models.ViewResult;

public class RegisterMenuScreen extends ManagedScreen {

    private final Stage ui = new Stage(new ScreenViewport());
    private final Skin skin = GameAPI.getAssetManager().getSkin();

    private float delay = 1.2f;
    private boolean sceneSwitched;

    private TypingLabel label;
    private TextField usernameField;
    private TextField passwordField;
    private TextField securityQuestionField;
    private SelectBox<String> securityQuestions;
    private TextButton registerButton;
    private TextButton gotoLoginMenuButton;
    private TextButton registerAsGuestButton;

    public RegisterMenuScreen() {
        super();
        addInputProcessor(ui);
        setupUi();
    }

    private void setupUi() {
        Table root = new Table();
        root.setFillParent(true);
        root.center();

        label = new TypingLabel("{EASE}{SPEED=SLOWER}Glad that you arrived!{SPEED}\n" +
            "{ENDEASE}{COLOR=#743f39}Firebread{CLEARCOLOR} and {COLOR=#0095e9}Icebread{CLEARCOLOR} need your {RAINBOW}help!{ENDRAINBOW}\n" +
            "It looks like the evil {SHAKE}snail king{ENDSHAKE}...\n" +
            "{SPEED=SLOWER}...merged{SPEED} their worlds together!\n" +
            "Only {RAINBOW}you{ENDRAINBOW} can help them out of this mess...\n\n" +
            "{SPEED=SLOW}{SLIDE}save them please!{ENDSLIDE}{SPEED}\n\n" +
            "Ps. {JUMP}Jump{ENDJUMP} onto enemy souls to destroy them!{WAIT=5} ", skin);
        label.setVisible(false);

        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username");

        passwordField = new TextField("", skin);
        passwordField.setMessageText("Enter password");

        securityQuestionField = new TextField("", skin);
        securityQuestionField.setMessageText("Answer");

        securityQuestions = new SelectBox<>(skin);
        securityQuestions.setItems(
            "1. What is your pet's name?",
            "2. What is your wife's address?",
            "3. Nuclear launch code"
        );

        registerButton = new TextButton("Register", skin);
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ViewResult<Void> result = Controllers.getRegisterController().register(
                    usernameField.getText(),
                    passwordField.getText(),
                    securityQuestionField.getText());
                if (!result.isSuccess()) {
                    label.setVisible(true);
                    label.restart();
                    label.setText("{EASE}{SPEED=8}{COLOR=#FF0000}" + result.getMessage());
                } else {
                    GameMain gameMain = GameMain.getInstance();
                    gameMain.getScreenManager().pushScreen(
                        new MainMenuScreen(),
                        new BlendingTransition(gameMain.getSpriteBatch(), 1F, Interpolation.pow2In)
                    );
                }
            }
        });

        registerAsGuestButton = new TextButton("Register As Guest", skin);
        registerAsGuestButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Controllers.getLoginMenuController().login("Guest", "Asd123@32!");
                GameMain gameMain = GameMain.getInstance();
                gameMain.changeScreen(
                    new MainMenuScreen(),
                    new BlendingTransition(gameMain.getSpriteBatch(), 1F, Interpolation.pow2In)
                );
            }
        });

        gotoLoginMenuButton = new TextButton("Go to Login", skin);
        gotoLoginMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMain gameMain = GameMain.getInstance();
                gameMain.changeScreen(
                    new LoginMenuScreen(),
                    new BlendingTransition(gameMain.getSpriteBatch(), 1F, Interpolation.pow2In)
                );
            }
        });

        Label label = new Label("Register Account", skin);
        label.setAlignment(Align.center);

// Form table for fields and buttons
        Table formTable = new Table();
        formTable.defaults().pad(10).width(400);
        formTable.setBackground(skin.newDrawable("white", Color.DARK_GRAY));
        formTable.add(registerButton).fillX().padTop(20).row();
        formTable.pad(20);
        formTable.setRound(true); // if using a rounded skin


// Add form elements
        formTable.add(usernameField).fillX().row();
        formTable.add(passwordField).fillX().row();
        formTable.add(securityQuestions).fillX().row();
        formTable.add(securityQuestionField).fillX().row();
        formTable.add(registerButton).fillX().row();
        formTable.add(registerAsGuestButton).fillX().row();
        formTable.add(gotoLoginMenuButton).fillX().row();
        formTable.add(this.label).fillX().row();
        root.add(new SettingsMenu());
//        root.add(new ControllersMenu(skin));

        root.add(label).padBottom(30).colspan(1).center().row();
        root.add(formTable).center();
        ui.addActor(root);
    }

//    public void showInputDialog(String title, String defaultValue, final InputListener callback) {
//        Dialog dialog = new Dialog(title, skin) {
//            protected void result(Object object) {
//                if (object instanceof String) {
//                    callback.onInput((String) object);
//                }
//            }
//        };
//
//        TextField inputField = new TextField(defaultValue, skin);
//        inputField.setMessageText("Type here...");
//
//        dialog.getContentTable().add(inputField).width(300).pad(10);
//        dialog.button("OK", inputField::getText);
//        dialog.button("Cancel", null);
//        dialog.show(stage);
//    }


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
