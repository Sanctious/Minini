package sanctious.minini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import de.eskalon.commons.screen.ManagedScreen;
import org.w3c.dom.Text;

public class MainMenuScreen extends ManagedScreen {

    private TextButton settingsButton;
    private TextButton profileButton;
    private TextButton preGameButton;
    private TextButton scoreBoardButton;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.706f * 0.25f, 255, 255, 1);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }
}
