package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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

    private final Stage uiStage;
    private final Skin skin;

    private TypingLabel messageLabel;
    private TextField usernameField;
    private TextField passwordField;
    private TextField securityQuestionField;
    private SelectBox<String> securityQuestions;
    private TextButton registerButton;
    private TextButton gotoLoginMenuButton;
    private TextButton registerAsGuestButton;

    public RegisterMenuScreen() {
        super();
        this.uiStage = new Stage(new ScreenViewport());
        this.skin = GameAPI.getAssetManager().getSkin();
        addInputProcessor(uiStage);
        setupUi();
    }

    private void setupUi() {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();

        messageLabel = new TypingLabel("", skin);
        messageLabel.setVisible(false);
        messageLabel.setAlignment(Align.center);
        messageLabel.setWrap(true);

        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username");

        passwordField = new TextField("", skin);
        passwordField.setMessageText("Enter password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        securityQuestionField = new TextField("", skin);
        securityQuestionField.setMessageText("Your Answer");

        securityQuestions = new SelectBox<>(skin);
        securityQuestions.setItems(
            "1. What is your pet's name?",
            "2. What is your mother's maiden name?",
            "3. In what city were you born?"
        );

        registerButton = new TextButton("Register", skin);
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ViewResult<Void> result = Controllers.getRegisterController().register(
                    usernameField.getText(),
                    passwordField.getText(),
                    securityQuestionField.getText());

                messageLabel.setVisible(true);
                messageLabel.restart();
                if (!result.isSuccess()) {
                    messageLabel.setText("{EASE}{SPEED=8}{COLOR=RED}" + result.getMessage() + "{ENDEASE}");
                } else {
                    messageLabel.setText("{EASE}{SPEED=8}{COLOR=GREEN}Registration successful! Redirecting...{ENDEASE}");
                    GameMain gameMain = GameMain.getInstance();
                    gameMain.getScreenManager().pushScreen(
                        new MainMenuScreen(),
                        new BlendingTransition(gameMain.getSpriteBatch(), 1F, Interpolation.pow2In)
                    );
                }
            }
        });

        registerAsGuestButton = new TextButton("Continue as Guest", skin);
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

        gotoLoginMenuButton = new TextButton("Already have an account? Login", skin);
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

        Label titleLabel = new Label("Create Your Account", skin);
        titleLabel.setAlignment(Align.center);

        Table formTable = new Table(skin);
        formTable.pad(20f);
        formTable.defaults().pad(5f).width(350f).fillX();
        formTable.setBackground(skin.newDrawable("white", new Color(0.2f, 0.2f, 0.2f, 0.7f)));
        formTable.setRound(true);

        formTable.add(usernameField).row();
        formTable.add(passwordField).row();
        formTable.add(securityQuestions).row();
        formTable.add(securityQuestionField).row();
        formTable.add(registerButton).padTop(10f).row();
        formTable.add(registerAsGuestButton).row();
        formTable.add(gotoLoginMenuButton).row();
        formTable.add(messageLabel).minHeight(40f).padTop(10f).row();

        rootTable.add(titleLabel).padBottom(20f).row();
        rootTable.add(formTable).center();
        uiStage.addActor(rootTable);
    }

    @Override
    public void show() {
        super.show();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.12f, 0.16f, 1f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isTouched() && messageLabel.isVisible() && !messageLabel.hasEnded()) {
            messageLabel.skipToTheEnd();
        }

        uiStage.getViewport().apply();
        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (uiStage != null) {
            uiStage.dispose();
        }
    }
}
