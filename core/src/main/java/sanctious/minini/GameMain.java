package sanctious.minini;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import de.eskalon.commons.core.ManagedGame;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.ScreenTransition;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import de.eskalon.commons.utils.BasicInputMultiplexer;
import sanctious.minini.Controllers.Controllers;
import sanctious.minini.Models.GameAPI;
import sanctious.minini.View.GameScreen;
import sanctious.minini.View.RegisterMenuScreen;

public class GameMain extends ManagedGame<ManagedScreen, ScreenTransition> {
    private static GameMain instance;

    private SpriteBatch spriteBatch;

    private GameMain(){

    }
    public static GameMain getInstance() {
        if (instance == null){
            instance = new GameMain();
        }
        return instance;
    }


    @Override
    public void create() {
        super.create();
        Controllers.getRegisterController().register("Guest", "Asd123@32!");

        spriteBatch = new SpriteBatch();
        this.screenManager.setAutoDispose(true, true);
//
//        this.screenManager.pushScreen(new RegisterMenuScreen(),
//            new BlendingTransition(spriteBatch, 1F, Interpolation.pow2In));

        this.screenManager.pushScreen(new GameScreen(),
            null);

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

    public void changeScreen(ManagedScreen screen, ScreenTransition transition){
        this.screenManager.pushScreen(screen, transition);
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}
