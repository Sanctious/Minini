package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import de.eskalon.commons.screen.ManagedScreen;
import sanctious.minini.Models.Game.Player;
import sanctious.minini.Models.GameAPI;
import sanctious.minini.Models.User;

public class EndScreen extends ManagedScreen {
    private Stage ui = new Stage(new ScreenViewport());
    private Skin skin = GameAPI.getAssetManager().getSkin();

    private float surviveTime;
    private Player player;
    private float points;

    public EndScreen(float surviveTime, Player player, float points) {
        super();
        addInputProcessor(ui);
        this.surviveTime = surviveTime;
        this.player = player;
        this.points = points;
        setupUI();
    }

    public void setupUI(){
        Table root = new Table();
        root.setFillParent(true);
        root.center();

        User user = GameAPI.getUserRegistry().getActiveUser();

        TypingLabel gameState = new TypingLabel(player.isDead() ? "{JUMP}{COLOR=RED}You suck!" : "{JUMP}{COLOR=GREEN}You WON!", skin);
        TextField username = new TextField(user.getUsername(), skin);
        TextField password = new TextField(user.getPassword(), skin);
        TextButton showPassword = new TextButton("Show", skin);
        showPassword.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                password.setPasswordMode(false);
            }
        });
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');
        SelectBox<Option<String>> avatars = new SelectBox<Option<String>>(skin);
        avatars.setItems(
            new Option<>("Destroyer", "address here"),
            new Option<>("Destroyer", "address here"),
            new Option<>("Destroyer", "address here")
        );

        TextButton submit = new TextButton("Delete", skin);
        TypingLabel errorLabel = new TypingLabel("", skin);
        errorLabel.setVisible(false);


        root.setWidth(400);
        root.defaults().pad(5);
        root.setBackground(skin.newDrawable("white", Color.DARK_GRAY));

        root.add(username).row();
        root.add(password);
        root.add(showPassword).row();
        root.add(avatars).row();
        root.add(submit);
        root.add(errorLabel);

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
