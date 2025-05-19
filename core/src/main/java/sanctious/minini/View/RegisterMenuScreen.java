package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.sun.org.apache.bcel.internal.generic.NEW;
import de.eskalon.commons.screen.ManagedScreen;
import jdk.vm.ci.code.Register;
import sanctious.minini.Controllers.RegisterMenuController;
import sanctious.minini.Models.ViewResult;

public class RegisterMenuScreen extends ManagedScreen {

    private final Stage ui = new Stage(new ExtendViewport(1920, 1080));
    Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

    private float delay = 1.2f;

    private TypingLabel label;

    private TextField usernameField;
    private TextField passwordField;
    private TextButton registerButton;
    private Dialog popup;


    private boolean sceneSwitched;

    public RegisterMenuScreen() {
        super();
        addInputProcessor(ui);

        setupUi();
    }


    private void setupUi() {
        Table root = new Table();
        root.setFillParent(true);

        popup = new Dialog("State:", skin);
        root.add(popup);

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
        registerButton = new TextButton("test", skin);

        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ViewResult result = RegisterMenuController.register(usernameField.getText(), passwordField.getText());
                label.restart();
                label.setText("{EASE}{SPEED=SLOWER}" + result.getMessage());
            }
        });

        root.add(label).top().left().expand().padLeft(10).padTop(10);
        root.add(registerButton).top().left().expand().padLeft(10).padTop(32);
        root.add(usernameField).top().left();
        root.add(passwordField).top().left();



        ui.addActor(root);
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
