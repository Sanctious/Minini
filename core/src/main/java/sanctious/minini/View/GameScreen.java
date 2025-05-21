package sanctious.minini.View;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.eskalon.commons.screen.ManagedScreen;

public class GameScreen extends ManagedScreen {

    private OrthographicCamera camera;
    private Viewport viewport;
    private World world;
    private RayHandler rayHandler;

    private Texture texture;
    private float lightX = 400;
    private float lightY = 300;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        camera.position.set(400, 300, 0);
        camera.update();

        world = new World(new Vector2(0, 0), true);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.2f);
        rayHandler.setCulling(true);
        rayHandler.setShadows(true);

        // Create a test light
        new PointLight(rayHandler, 128, Color.WHITE, 300, lightX, lightY);

        // Load a texture to draw
        texture = new Texture("badlogic.jpg");

        // Optional: create a shadow occluder
        createOccluderBody(400, 250, 100, 30);
    }

    private void createOccluderBody(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);
        body.createFixture(shape, 0.0f);
        shape.dispose();
    }

    @Override
    public void render(float delta) {
        world.step(delta, 6, 2);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // Draw something
        rayHandler.getRayHandlerShader(); // force shader load before drawing
        rayHandler.setCombinedMatrix(camera);

        // Draw sprite
        rayHandler.getSpriteBatch().begin();
        rayHandler.getSpriteBatch().draw(texture, 350, 250, 100, 100);
        rayHandler.getSpriteBatch().end();

        // Render lights
        rayHandler.updateAndRender();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        rayHandler.dispose();
        world.dispose();
        texture.dispose();
    }
}
