package sanctious.minini.View;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Carousel<T> extends Table {
    private final Label selectedLabel;
    private final Map<String, T> items = new LinkedHashMap<>();
    private List<String> keys;
    private int selectedIndex = 0;

    public Carousel(Skin skin) {

        TextButton leftButton = new TextButton("<", skin);
        TextButton rightButton = new TextButton(">", skin);
        selectedLabel = new Label("", skin);

        leftButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedIndex = (selectedIndex - 1 + items.size()) % items.size();
                updateLabel();
            }
        });

        rightButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedIndex = (selectedIndex + 1) % items.size();
                updateLabel();
            }
        });

        add(leftButton).padRight(10);
        add(selectedLabel).width(150).center();
        add(rightButton).padLeft(10);
    }

    public void addEntry(String displayName, T data){
        items.put(displayName, data);
        // Update key-set
        keys = new ArrayList<>(items.keySet());
        updateLabel();
    }

    private void updateLabel() {
        selectedLabel.setText(keys.get(selectedIndex));
    }

    public T getSelectedItem() {
        return items.get(keys.get(selectedIndex));
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}

