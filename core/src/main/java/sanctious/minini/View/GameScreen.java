package sanctious.minini.View;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.eskalon.commons.screen.ManagedScreen;
import sanctious.minini.Controllers.Controllers;
import sanctious.minini.Controllers.GameController;
import sanctious.minini.GameMain;
import sanctious.minini.Models.Game.*;
import sanctious.minini.Models.Game.Enemies.Enemy;

import java.util.List;

public class GameScreen extends ManagedScreen {

    public static final float PPM = 32f; // Pixels per meter

    private OrthographicCamera camera;
    private Viewport viewport;
    private World world;
    private RayHandler rayHandler;
    // TODO ?? what to do ??
    private Weapon weapon = new Weapon(WeaponType.SMG);
    private Player player = new Player(20f, 5f);
    private float passedTime = 0f;

    {
        player.setActiveWeapon(weapon);
    }
    private PlayerRenderer playerRenderer = new PlayerRenderer(new TextureAtlas(Gdx.files.internal("Shanker.atlas")));
    private Texture cursorTexture = new Texture(Gdx.files.internal("hit/T_HitMarkerFX_0.png"));
    public static Texture xpTexture = new Texture(Gdx.files.internal("enemies/T_DiamondFilled.png"));
    private Texture weaponTexture = new Texture(Gdx.files.internal("hit/T_Shotgun_SS_0.png"));


    private Texture texture;
    private PointLight pointLight;

    @Override
    public void show() {
//        Gdx.input.setCursorCatched(true);
//        Gdx.input.setCursorVisible(false);
//        Gdx.graphics.setCursor(null);
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
        pointLight = new PointLight(rayHandler, 128, Color.WHITE, 50f, 400 / PPM, 300 / PPM);

        // Add a simple occluder box
        createOccluderBody(400 / PPM, 300 / PPM, 100 / PPM, 50 / PPM);

        GameController controller = Controllers.getGameController();
        controller.initializeMap();

    }

    @Override
    public void render(float delta) {
        passedTime += delta;
        GameController controller = Controllers.getGameController();
        // Weapon rendering
        Vector2 weaponPos = player.getPosition().cpy().add(new Vector2(0, 0));
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mousePos); // If you're using a viewport

        Vector2 direction = mousePos.cpy().sub(weaponPos);
        float targetAngle = direction.angleDeg(); // angle in degrees

        float currentAngle = weapon.getRenderAngle(); // stored rotation in degrees
        float newAngle = MathUtils.lerpAngleDeg(currentAngle, targetAngle, 5 * delta);
        weapon.setRenderAngle(newAngle);


        // Handle shooting
        checkShooting(controller);
        checkReloading(controller);

        controller.updateBullets(delta);
        controller.updateEnemies(player, delta);
        controller.checkCollisions(player, playerRenderer);
        controller.updatePlayerPosition(player, delta);
        controller.trySpawnEnemies(player, delta);

//        for (Bullet b : bullets) b.update(delta);

        world.step(1 / 60f, 6, 2);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        // Lerp camera
        updateCamera();
        weapon.update(delta);
        player.update(delta);
//        pointLight.setPosition(player.getPosition());
//        camera.position.set(player.getPosition(), 0);
//        camera.update();

        SpriteBatch batch = GameMain.getInstance().getSpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        // Draw your sprite
        batch.begin();

        renderBullets(controller.getBullets(), batch);
        renderXPs(controller.getXps(), batch, delta);
        renderEnemies(controller.getEnemies(), batch, delta);
        drawCustomCursor(batch);
        drawWeapon(batch);
        playerRenderer.render(batch, player, passedTime);
        batch.draw(texture,
            400 / PPM - 50 / PPM, 300 / PPM - 50 / PPM,
            100 / PPM, 100 / PPM);
        batch.end();


        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

    }

    public void checkShooting(GameController controller){
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector2 mouseWorld = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            controller.shoot(player, mouseWorld);
        }
    }

    public void checkReloading(GameController controller){
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)){
            controller.tryReload(player);
        }
    }


    private void updateCamera() {
        Vector2 playerPos = player.getPosition();

        // Mouse position in world coords
        Vector2 mouseWorld = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mouseWorld);

        Vector2 offset = mouseWorld.cpy().sub(playerPos);

        float deadzoneRadius = 3f; // max radius of camera offset

        float distance = offset.len();

        if (distance > 0) {
            // Normalize offset to direction only
            Vector2 direction = offset.cpy().nor();

            // Smooth step: ramp offset length from 0 to deadzoneRadius starting at some inner radius
            float innerRadius = 1.5f; // where the offset starts growing smoothly (tweak this)
            float t = MathUtils.clamp((distance - innerRadius) / (deadzoneRadius - innerRadius), 0f, 1f);

            // Use easing for smooth transition (e.g. smoothstep: t*t*(3-2*t))
            float smoothT = t * t * (3 - 2 * t) * 0.3f;

            // Final offset length smoothly scales from 0 to deadzoneRadius
            float offsetLength = smoothT * deadzoneRadius;

            offset = direction.scl(offsetLength);
        } else {
            offset.setZero();
        }

        Vector2 cameraTarget = playerPos.cpy().add(offset);

        float lerp = 0.1f;
        camera.position.lerp(new Vector3(cameraTarget.x, cameraTarget.y, 0), lerp);
        camera.update();
    }

    private void drawCustomCursor(SpriteBatch batch){
        Vector2 mouseWorld = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mouseWorld);
//        float scale = 1f + 0.1f * MathUtils.sinDeg(TimeUtils.millis() * 0.5f);
        batch.draw(cursorTexture, mouseWorld.x, mouseWorld.y,
            cursorTexture.getWidth()/2f, cursorTexture.getHeight()/2f, // origin for scaling (center)
            cursorTexture.getWidth(), cursorTexture.getHeight(),
//            scale*10, scale*10);
            20, 20);


    }
    private void drawWeapon(SpriteBatch batch) {
        Vector2 playerPos = player.getPosition();

        Vector2 mouseWorld = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(mouseWorld);

        Vector2 direction = mouseWorld.cpy().sub(playerPos);
        float targetAngle = direction.angleDeg();

        boolean facingRight = direction.x >= 0;
        player.setFacing(facingRight);

        Vector2 handOffset = new Vector2(facingRight ? 0.3f : -0.4f, 0.0f);
        Vector2 weaponPos = playerPos.cpy().add(handOffset);

// Origin where the hand holds the weapon
        float originX = facingRight ? 5 : weaponTexture.getWidth() - 5;
        float originY = weaponTexture.getHeight() / 2f;

        float rotation = targetAngle;
        boolean flipX = false;
        boolean flipY = false;

        if (!facingRight) {
//            rotation = 180f - targetAngle;
//            flipX = true;
            flipY = true;
        }

        batch.draw(
            weaponTexture,
            weaponPos.x - originX / PPM,
            weaponPos.y - originY / PPM,
            originX / PPM,
            originY / PPM,
            weaponTexture.getWidth() / PPM,
            weaponTexture.getHeight() / PPM,
            1f,
            1f,
            rotation,
            0,
            0,
            weaponTexture.getWidth(),
            weaponTexture.getHeight(),
            flipX,
            flipY
        );

    }


    public void renderBullets(List<Bullet> bullets, SpriteBatch batch){
        for (Bullet b : bullets) {
            b.getRenderSprite().draw(batch);
//            Sprite sprite = b.getRenderSprite();
//            float width = sprite.getTexture().getWidth() / PPM;
//            float height = sprite.getTexture().getHeight() / PPM;
//            batch.draw(sprite.getTexture(),
//                sprite.getX(), sprite.getY(),
//                width, height);

        }
    }

    public void renderEnemies(List<Enemy> enemies, SpriteBatch batch, float delta){
        for (Enemy enemy : enemies) {
            enemy.getRenderer().render(batch, delta);
        }

    }

    public void renderXPs(List<XP> xps, SpriteBatch batch, float delta){
        float scale = 0.032f / PPM;
        float spriteWidth = xpTexture.getWidth() * scale;
        float spriteHeight = xpTexture.getHeight() * scale;
        for (XP xp : xps) {
            float drawX = xp.getPosition().x - spriteWidth / 2f;
            float drawY = xp.getPosition().y - spriteHeight / 2f;

            batch.draw(xpTexture, drawX, drawY, spriteWidth, spriteHeight);
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
