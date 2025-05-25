package sanctious.minini.View;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.eskalon.commons.screen.ManagedScreen;
import sanctious.minini.Controllers.Controllers;
import sanctious.minini.Controllers.GameController;
import sanctious.minini.GameMain;
import sanctious.minini.Models.Bullet;
import sanctious.minini.Models.Player;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends ManagedScreen {

    private static final float PPM = 32f; // Pixels per meter

    private OrthographicCamera camera;
    private Viewport viewport;
    private World world;
    private RayHandler rayHandler;
    // TODO ?? what to do ??
    private Player player = new Player();

    private Texture texture;
    private PointLight pointLight;

    @Override
    public void show() {
        // Set up world with no gravity
        world = new World(new Vector2(0, 0), true);

        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(800 / PPM, 600 / PPM, camera); // World units
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
        camera.update();

        // Set up lighting
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.3f);
        rayHandler.setShadows(true);
        rayHandler.setCulling(true);

        // Load a texture
        texture = new Texture("libgdx.png");

        // Create a point light at center of screen (in meters)
        pointLight = new PointLight(rayHandler, 128, Color.WHITE, 5f, 400 / PPM, 300 / PPM);

        // Add a simple occluder box
        createOccluderBody(400 / PPM, 300 / PPM, 100 / PPM, 50 / PPM);

    }

    @Override
    public void render(float delta) {
        GameController controller = Controllers.getGameController();

        controller.updateBullets(delta);
        controller.updatePlayerPosition(player, delta);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector2 mouseWorld = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            controller.shoot(player, mouseWorld);
        }

//        for (Bullet b : bullets) b.update(delta);

        world.step(1 / 60f, 6, 2);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        SpriteBatch batch = GameMain.getInstance().getSpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        // Draw your sprite
        batch.begin();
        renderBullets(new ArrayList<>(), batch);
        batch.draw(texture,
            400 / PPM - 50 / PPM, 300 / PPM - 50 / PPM,
            100 / PPM, 100 / PPM);
        batch.end();

        // === FIX: Save and restore FBO to not break Eskalon's framebuffer stack ===
//        int oldFbo = Gdx.GL20.GL_FRAMEBUFFER_BINDING);

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

//        Gdx.gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, oldFbo);
    }

    public void renderBullets(List<Bullet> bullets, SpriteBatch batch){
        for (Bullet b : bullets) {
            batch.draw(texture, b.position.x - 0.1f, b.position.y - 0.1f, 0.2f, 0.2f);
        }
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
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
        camera.update();
    }

    @Override
    public void dispose() {
        rayHandler.dispose();
        world.dispose();
        texture.dispose();
    }
}
