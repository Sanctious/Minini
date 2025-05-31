package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import sanctious.minini.Controllers.RegisterMenuController;
import sanctious.minini.GameMain;
import sanctious.minini.Models.GameAPI;
import sanctious.minini.Models.User;

public class ProfileMenuScreen extends ManagedScreen {
    private Stage uiStage;
    private Skin skin;
    private User currentUser;

    private TextField usernameField;
    private TextField newPasswordField;
    private TextField confirmNewPasswordField;
    private TextButton showHidePasswordButton;
    private SelectBox<Option<String>> avatarSelectBox;
    private TypingLabel messageLabel;

    private static final int MIN_PASSWORD_LENGTH = 6;

    private static class Option<T> {
        public String displayName;
        public T value;

        public Option(String displayName, T value) {
            this.displayName = displayName;
            this.value = value;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public ProfileMenuScreen() {
        super();
        this.uiStage = new Stage(new ScreenViewport());
        this.skin = GameAPI.getAssetManager().getSkin();
        this.currentUser = GameAPI.getUserRegistry().getActiveUser();
        if (this.currentUser == null) {
            Gdx.app.error("ProfileMenuScreen", "No active user found. Redirecting to Login.");
            Gdx.app.postRunnable(() -> GameMain.getInstance().changeScreen(new LoginMenuScreen(), null));
            return;
        }
        addInputProcessor(uiStage);
        setupUI();
    }

    public void setupUI() {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();
        rootTable.pad(20f);

        Label titleLabel = new Label("Profile Settings", skin);
        titleLabel.setAlignment(Align.center);
        rootTable.add(titleLabel).padBottom(20f).colspan(2).row();

        Table formTable = new Table(skin);
        formTable.defaults().pad(5f);
        formTable.setBackground(skin.newDrawable("white", new Color(0.2f, 0.2f, 0.2f, 0.7f)));
        formTable.pad(15f);

        usernameField = new TextField(currentUser.getUsername(), skin);
        formTable.add(new Label("Username:", skin)).left();
        formTable.add(usernameField).width(250f).fillX().row();

        newPasswordField = new TextField("", skin);
        newPasswordField.setMessageText("New Password (optional)");
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');

        confirmNewPasswordField = new TextField("", skin);
        confirmNewPasswordField.setMessageText("Confirm New Password");
        confirmNewPasswordField.setPasswordMode(true);
        confirmNewPasswordField.setPasswordCharacter('*');

        showHidePasswordButton = new TextButton("Show", skin);
        showHidePasswordButton.addListener(new ClickListener() {
            private boolean passwordVisible = false;
            @Override
            public void clicked(InputEvent event, float x, float y) {
                passwordVisible = !passwordVisible;
                newPasswordField.setPasswordMode(!passwordVisible);
                confirmNewPasswordField.setPasswordMode(!passwordVisible);
                showHidePasswordButton.setText(passwordVisible ? "Hide" : "Show");
            }
        });

        Table passwordTable = new Table();
        passwordTable.add(newPasswordField).width(200f).fillX();
        passwordTable.add(showHidePasswordButton).padLeft(5f);

        formTable.add(new Label("New Password:", skin)).left();
        formTable.add(passwordTable).row();
        formTable.add(new Label("Confirm Pwd:", skin)).left();
        formTable.add(confirmNewPasswordField).width(250f).fillX().row();

        avatarSelectBox = new SelectBox<>(skin);
        avatarSelectBox.setItems(
            new Option<>("Dasher", "Dasher"), new Option<>("Diamond", "Diamond"),
            new Option<>("Hastur", "Hastur"), new Option<>("Hina", "Hina"),
            new Option<>("Luna", "Luna"), new Option<>("Raven", "Raven"),
            new Option<>("Scarlett", "Scarlett")
        );
        String currentAvatarName = currentUser.getAvatar();
        for (Option<String> opt : avatarSelectBox.getItems()) {
            if (opt.value.equals(currentAvatarName)) {
                avatarSelectBox.setSelected(opt);
                break;
            }
        }
        formTable.add(new Label("Avatar:", skin)).left();
        formTable.add(avatarSelectBox).width(250f).fillX().row();

        TextButton customAvatarButton = new TextButton("Choose Custom File", skin);
        customAvatarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showCustomAvatarPathDialog();
            }
        });
        formTable.add(new Label("Custom Avatar:", skin)).left();
        formTable.add(customAvatarButton).width(250f).fillX().row();
        formTable.add(new Label("(Drag & Drop not implemented)", skin)).colspan(2).center().row();


        messageLabel = new TypingLabel("", skin);
        messageLabel.setVisible(false);
        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.center);
        formTable.add(messageLabel).colspan(2).width(300f).minHeight(40f).padTop(10f).row();

        rootTable.add(formTable).row();

        Table actionButtonsTable = new Table();
        actionButtonsTable.defaults().pad(10f).minWidth(150f);

        TextButton saveChangesButton = new TextButton("Save Changes", skin);
        saveChangesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleSaveChanges();
            }
        });

        TextButton deleteAccountButton = new TextButton("Delete Account", skin);
        deleteAccountButton.setColor(Color.SCARLET); // More distinct red
        deleteAccountButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                confirmAccountDeletion();
            }
        });

        TextButton backButton = new TextButton("Back to Menu", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMain.getInstance().changeScreen(new MainMenuScreen(),
                    new BlendingTransition(GameMain.getInstance().getSpriteBatch(), 0.5f, Interpolation.fade));
            }
        });

        actionButtonsTable.add(saveChangesButton);
        actionButtonsTable.add(deleteAccountButton).row();
        rootTable.add(actionButtonsTable).padTop(15f).colspan(2).row();
        rootTable.add(backButton).padTop(10f).colspan(2).center();

        uiStage.addActor(rootTable);
    }

    private void showCustomAvatarPathDialog() {
        final TextField pathField = new TextField("", skin);
        pathField.setMessageText("Enter path to image (e.g., C:/img.png)");

        Dialog dialog = new Dialog("Load Custom Avatar", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    String path = pathField.getText();
                    if (path != null && !path.trim().isEmpty()) {
                        Gdx.app.log("ProfileMenuScreen", "User entered custom avatar path: " + path);
                        messageLabel.setVisible(true);
                        messageLabel.restart();
                        messageLabel.setText("{COLOR=SKYBLUE}Custom avatar path '" + path + "' noted. \n(Full file loading not implemented here.){ENDCOLOR}");
                        // Here you would add logic to load the texture from the path,
                        // copy it to an assets folder, update user.setAvatar(newPathOrId), etc.
                        // This is complex and requires file handling.
                    }
                }
            }
        };
        dialog.text("Enter the full path to your avatar image file.\n(Actual file loading requires native integration or a library.)");
        dialog.getContentTable().row();
        dialog.getContentTable().add(pathField).width(350).pad(10f);
        dialog.button("Use Path", true);
        dialog.button("Cancel", false);
        dialog.show(uiStage);
    }

    private void handleSaveChanges() {
        String originalUsername = currentUser.getUsername();
        String newUsernameAttempt = usernameField.getText().trim();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmNewPasswordField.getText();
        String selectedAvatar = avatarSelectBox.getSelected().value;

        boolean changesMade = false;
        messageLabel.setVisible(true);
        messageLabel.restart();

        if (!newUsernameAttempt.equals(originalUsername)) {
            if (newUsernameAttempt.isEmpty()) {
                messageLabel.setText("{COLOR=RED}Username cannot be empty.{ENDCOLOR}");
                return;
            }
            if (GameAPI.getUserRegistry().userExists(newUsernameAttempt)) {
                messageLabel.setText("{COLOR=RED}Username '" + newUsernameAttempt + "' is already taken.{ENDCOLOR}");
                return;
            }
            currentUser.setUsername(newUsernameAttempt);
            changesMade = true;
        }

        if (!newPassword.isEmpty()) {
            if (!RegisterMenuController.isPasswordStrong(newPassword)) {
                messageLabel.setText("{COLOR=RED}New password isn't strong enough!{ENDCOLOR}");
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                messageLabel.setText("{COLOR=RED}New passwords do not match.{ENDCOLOR}");
                return;
            }
            currentUser.setPassword(newPassword);
            changesMade = true;
        }

        if (!selectedAvatar.equals(currentUser.getAvatar())) {
            currentUser.setAvatar(selectedAvatar);
            changesMade = true;
        }

        if (changesMade) {
            messageLabel.setText("{COLOR=GREEN}Changes saved successfully!{ENDCOLOR}");
            newPasswordField.setText("");
            confirmNewPasswordField.setText("");
            usernameField.setText(currentUser.getUsername()); // Reflect potential username change
            for (Option<String> opt : avatarSelectBox.getItems()) { // Reselect in selectbox
                if (opt.value.equals(currentUser.getAvatar())) {
                    avatarSelectBox.setSelected(opt);
                    break;
                }
            }
        } else {
            messageLabel.setText("No changes detected.");
        }
    }

    private void confirmAccountDeletion() {
        Dialog dialog = new Dialog("Delete Account", skin) {
            @Override
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    performAccountDeletion();
                }
            }
        };
        dialog.text("Are you sure you want to permanently delete your account?\nThis action cannot be undone.");
        dialog.button("Yes, Delete My Account", true);
        dialog.button("Cancel", false);
        dialog.show(uiStage);
    }

    private void performAccountDeletion() {
        GameAPI.getUserRegistry().removeUser(currentUser);
        GameAPI.getUserRegistry().setActiveUser(null);

        messageLabel.setVisible(true);
        messageLabel.restart();
        messageLabel.setText("{COLOR=ORANGE}Account deleted. Redirecting...{ENDCOLOR}");

        Gdx.app.postRunnable(() ->
            GameMain.getInstance().changeScreen(new RegisterMenuScreen(),
                new BlendingTransition(GameMain.getInstance().getSpriteBatch(), 1f, Interpolation.fade))
        );
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
    public void show() {
        super.show();
        // Input processor is added in constructor via addInputProcessor(uiStage) for ManagedScreen
        // Gdx.input.setInputProcessor(uiStage); // This line was correctly removed.
        if (currentUser == null && GameMain.getInstance().getScreenManager().getCurrentScreen() == this) {
            Gdx.app.error("ProfileMenuScreen", "No active user on show(). Critical error or race condition.");
            Gdx.app.postRunnable(() -> GameMain.getInstance().changeScreen(new LoginMenuScreen(), null));
        }
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
