package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.HashMap;
import java.util.Map;

public class ControllersMenu extends Window {
    private final Map<String, Integer> keyBindings = new HashMap<>();

    public ControllersMenu(Skin skin) {
        super("Controls", skin);
        setModal(true);
        pad(20);
        setMovable(true);

        // Example bindings
        keyBindings.put("Move Left", Input.Keys.A);
        keyBindings.put("Move Right", Input.Keys.D);
        keyBindings.put("Jump", Input.Keys.SPACE);

        updateUI(skin);
        pack();
        centerOnScreen();
    }

    private void updateUI(Skin skin) {
        clear(); // clear UI before rebuilding
        for (String action : keyBindings.keySet()) {
            final String keyName = Input.Keys.toString(keyBindings.get(action));
            final Label label = new Label(action + ": " + keyName, skin);
            final TextButton button = new TextButton("Change", skin);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    listenForKey(action, label);
                }
            });

            add(label).pad(5);
            add(button).pad(5).row();
        }

        TextButton close = new TextButton("Close", skin);
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
        add(close).colspan(2).padTop(15);
    }

    private void listenForKey(String action, Label label) {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                keyBindings.put(action, keycode);
                label.setText(action + ": " + Input.Keys.toString(keycode));
                Gdx.input.setInputProcessor(null); // return to UI input
                return true;
            }
        });
    }

    private void centerOnScreen() {
        setPosition(
            (Gdx.graphics.getWidth() - getWidth()) / 2f,
            (Gdx.graphics.getHeight() - getHeight()) / 2f
        );
    }
}

