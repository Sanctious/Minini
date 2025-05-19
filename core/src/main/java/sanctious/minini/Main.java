package sanctious.minini;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.ScreenUtils;
import de.eskalon.commons.core.ManagedGame;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import de.eskalon.commons.utils.BasicInputMultiplexer;
import sanctious.minini.View.MainMenuScreen;
import sanctious.minini.View.RegisterMenuScreen;

public class Main extends ManagedGame<ManagedScreen, ScreenTransition> {

    private SpriteBatch spriteBatch;

    @Override
    public void create() {
        super.create();

        spriteBatch = new SpriteBatch();
        this.screenManager.setAutoDispose(true, true);

        this.screenManager.pushScreen(new RegisterMenuScreen(),
            new BlendingTransition(spriteBatch, 1F, Interpolation.pow2In));

        Gdx.app.debug("Game", "Initialization finished.");
    }



    @Override
    public void render() {
        super.render();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public BasicInputMultiplexer getInputMultiplexer() {
        return super.getInputMultiplexer();
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}
