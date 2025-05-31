package sanctious.minini.View;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.eskalon.commons.screen.ManagedScreen;
import de.eskalon.commons.screen.transition.impl.BlendingTransition;
import sanctious.minini.Controllers.Controllers;
import sanctious.minini.Controllers.GameController;
import sanctious.minini.GameMain;
import sanctious.minini.Models.Game.Bullet;
import sanctious.minini.Models.Game.Enemies.Enemy;
import sanctious.minini.Models.Game.Player;
import sanctious.minini.Models.Game.Weapon;
import sanctious.minini.Models.Game.WeaponType;
import sanctious.minini.Models.Game.XP;
import sanctious.minini.Models.GameAPI;

import java.util.List;

public class GameScreen extends ManagedScreen {

    public static final float PPM = 32f;

    private OrthographicCamera camera;
    private Viewport viewport;
    private World world;
    private RayHandler rayHandler;
    private Weapon weapon = new Weapon(WeaponType.SMG);
    private Player player = new Player(20f, 5f);
    private final float gameDuration;
    private float passedTime = 0f;

    private PlayerRenderer playerRenderer = new PlayerRenderer(new TextureAtlas(Gdx.files.internal("heroes/Lilith.atlas")));
    private Texture cursorTexture = new Texture(Gdx.files.internal("hit/T_HitMarkerFX_0.png"));
    public static Texture xpTexture = new Texture(Gdx.files.internal("enemies/T_DiamondFilled.png"));
    private Texture weaponTexture = new Texture(Gdx.files.internal("hit/T_Shotgun_SS_0.png"));
    private Texture texture; // Generic libgdx.png
    private PointLight pointLight;

    private SpriteBatch uiBatch;
    private OrthographicCamera uiCamera;
    private BitmapFont font;
    private Skin skin; // For pause menu

    private Texture xpBarBackgroundTexture;
    private Texture xpBarForegroundTexture;
    private float currentXpProgress = 0f;
    private float targetXpProgress = 0f;
    private float lerpSpeed = 5.0f;

    private boolean isPaused = false;
    private Stage pauseMenuStage;
    private Table pauseMenuTable;

    {
        player.setActiveWeapon(weapon);
    }

    public GameScreen(float gameDuration) {
        this.gameDuration = gameDuration;
    }

    @Override
    public void show() {
        font = GameAPI.getAssetManager().getNewFont();
        skin = GameAPI.getAssetManager().getSkin();

        Gdx.input.setCursorCatched(true);
        world = new World(new Vector2(0, 0), true);

        camera = new OrthographicCamera();
        viewport = new FitViewport(800 / PPM, 600 / PPM, camera);
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
        camera.update();

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.3f);
        rayHandler.setShadows(true);
        rayHandler.setCulling(true);

        texture = new Texture("libgdx.png");
        pointLight = new PointLight(rayHandler, 128, Color.WHITE, 15f, player.getPosition().x, player.getPosition().y);
        pointLight.setSoftnessLength(1.5f);


        createOccluderBody(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 100 / PPM, 50 / PPM);

        GameController controller = Controllers.getGameController();
        controller.initializeMap();

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        uiBatch = new SpriteBatch();
        font.setColor(Color.WHITE);

        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(Color.DARK_GRAY);
        bgPixmap.fill();
        xpBarBackgroundTexture = new Texture(bgPixmap);
        bgPixmap.dispose();

        Pixmap fgPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        fgPixmap.setColor(Color.GREEN);
        fgPixmap.fill();
        xpBarForegroundTexture = new Texture(fgPixmap);
        fgPixmap.dispose();

        if (player.getXPToNextLevel() > 0) {
            targetXpProgress = (float) player.getXP() / player.getXPToNextLevel();
        } else {
            targetXpProgress = 0f;
        }
        currentXpProgress = targetXpProgress;

        setupPauseMenu();
        addInputProcessor(pauseMenuStage); // Add pause menu stage to handle its own input
    }

    private void setupPauseMenu() {
        pauseMenuStage = new Stage(new ScreenViewport());
        pauseMenuTable = new Table(skin);
        pauseMenuTable.setFillParent(true);
        pauseMenuTable.center();
        pauseMenuTable.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.75f))); // Semi-transparent black
        pauseMenuTable.setVisible(false); // Initially hidden

        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });

        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SettingsMenu settingsWindow = new SettingsMenu();
                pauseMenuStage.addActor(settingsWindow);
//                settingsWindow.show(pauseMenuStage); // Show settings on top of pause menu
            }
        });

        TextButton exitButton = new TextButton("Exit to Main Menu", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setCursorCatched(false); // Show system cursor before leaving
                GameMain.getInstance().changeScreen(new MainMenuScreen(),
                    new BlendingTransition(GameMain.getInstance().getSpriteBatch(), 1f, Interpolation.fade));
            }
        });

        pauseMenuTable.add(resumeButton).width(200).pad(10).row();
        pauseMenuTable.add(settingsButton).width(200).pad(10).row();
        pauseMenuTable.add(exitButton).width(200).pad(10).row();

        pauseMenuStage.addActor(pauseMenuTable);
    }

    private void togglePause() {
        isPaused = !isPaused;
        pauseMenuTable.setVisible(isPaused);
        Gdx.input.setCursorCatched(!isPaused); // Catch cursor when game is active, free when paused
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();
        }

        GameController controller = Controllers.getGameController();

        if (!isPaused) {
            passedTime += delta;

            Vector2 weaponPos = player.getPosition().cpy();
            Vector2 mousePosScreen = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector2 mousePosWorld = viewport.unproject(new Vector2(mousePosScreen.x, mousePosScreen.y));

            Vector2 directionToMouse = mousePosWorld.cpy().sub(weaponPos);
            float targetAngle = directionToMouse.angleDeg();

            float currentAngle = weapon.getRenderAngle();
            float newAngle = MathUtils.lerpAngleDeg(currentAngle, targetAngle, 10 * delta); // Faster lerp
            weapon.setRenderAngle(newAngle);

            checkShooting(controller);
            checkReloading(controller);
            checkAutoAim(controller);
            checkCheatCodes(controller);

            controller.updateBullets(delta);
            controller.updateEnemies(player, delta);
            controller.checkCollisions(player, playerRenderer);
            controller.updatePlayerPosition(player, delta);
            controller.trySpawnEnemies(player, passedTime, (int)gameDuration, delta); // gameDuration passed as int

            world.step(1 / 60f, 6, 2);

            updateCamera();
            weapon.update(delta);
            player.update(delta);
        }

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1); // Darker background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        SpriteBatch batch = GameMain.getInstance().getSpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        renderBullets(controller.getBullets(), batch);
        renderXPs(controller.getXps(), batch, delta);
        renderEnemies(controller.getEnemies(), batch, delta);
        drawWeapon(batch);
        playerRenderer.render(batch, player, delta, passedTime);
        batch.draw(texture,
            viewport.getWorldWidth() / 2f - (100 / PPM / 2f), viewport.getWorldHeight() / 2f - (50 / PPM / 2f),
            100 / PPM, 50 / PPM);
        drawCustomCursor(batch);
        batch.end();

        rayHandler.setCombinedMatrix(camera);
        pointLight.setPosition(player.getPosition().x, player.getPosition().y);
        rayHandler.updateAndRender();

        uiBatch.setProjectionMatrix(uiCamera.combined);
        uiBatch.begin();
        font.draw(uiBatch, "Health: " + player.getHealth() + "/" + player.getMaxHealth(), 10, Gdx.graphics.getHeight() - 10);
        font.draw(uiBatch, "Ammo: " + player.getActiveWeapon().getAmmoInClip() + "/" + player.getActiveWeapon().getMaxClipSize(), 10, Gdx.graphics.getHeight() - 30);
        font.draw(uiBatch, "Time: " + remainingTime(), 10, Gdx.graphics.getHeight() - 50);
        font.draw(uiBatch, "Kills: " + player.getKills(), 10, Gdx.graphics.getHeight() - 70);
        font.draw(uiBatch, String.format("Level: %d (XP: %.0f/%.0f)", player.getLevel(), player.getXP(), player.getXPToNextLevel()), 10, Gdx.graphics.getHeight() - 90);
        font.draw(uiBatch, String.format("Damage Boost: %s (%.1fs)", player.hasDamageBoost() ? "Active" : "Inactive", player.getDamageBoostTimer()), 10, Gdx.graphics.getHeight() - 110);
        font.draw(uiBatch, String.format("Speed Boost: %s (%.1fs)",  player.hasSpeedBoost() ? "Active" : "Inactive", player.getSpeedBoostTimer()), 10, Gdx.graphics.getHeight() - 130);

        drawXPBar(uiBatch);
        uiBatch.end();

        if (isPaused) {
            pauseMenuStage.act(delta);
            pauseMenuStage.draw();
        }
    }

    private void drawXPBar(SpriteBatch batch) {
        float barWidth = 200;
        float barHeight = 20;
        float barX = (Gdx.graphics.getWidth() - barWidth) / 2f;
        float barY = 10;

        float newTargetXpProgress = 0f;
        if (player.getXPToNextLevel() > 0) {
            newTargetXpProgress = MathUtils.clamp((float) player.getXP() / player.getXPToNextLevel(), 0f, 1f);
        }

        if (newTargetXpProgress < targetXpProgress && player.getXP() < player.getXPToNextLevel() * 0.1f) { // Reset animation on level up
            currentXpProgress = 0f;
        }
        targetXpProgress = newTargetXpProgress;
        currentXpProgress = MathUtils.lerp(currentXpProgress, targetXpProgress, lerpSpeed * Gdx.graphics.getDeltaTime());

        batch.draw(xpBarBackgroundTexture, barX, barY, barWidth, barHeight);
        batch.draw(xpBarForegroundTexture, barX, barY, barWidth * currentXpProgress, barHeight);

        font.draw(batch, "Level: " + player.getLevel(), barX + 5, barY + barHeight + 15);
        font.draw(batch, String.format("XP: %.0f / %.0f", player.getXP(), player.getXPToNextLevel()), barX + barWidth - 95, barY + barHeight + 15);
    }


    public String remainingTime() {
        int remainingTimeSeconds = Math.max(0, (int) (gameDuration - passedTime));
        int minutes = remainingTimeSeconds / 60;
        int seconds = remainingTimeSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void checkShooting(GameController controller) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector2 mouseWorld = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            controller.shoot(player, mouseWorld);
        }
    }

    public void checkReloading(GameController controller) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            controller.tryReload(player);
        }
    }

    public void checkAutoAim(GameController controller) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) { // Changed to isKeyPressed for continuous aiming while held
            controller.autoAim(player, camera, viewport);
        }
    }

    private void updateCamera() {
        Vector2 playerPos = player.getPosition();
        Vector2 mouseWorld = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        Vector2 offset = mouseWorld.cpy().sub(playerPos);

        float deadzoneRadius = 3f / PPM; // Deadzone in world units
        float maxOffset = 5f / PPM;    // Max camera offset in world units

        float distance = offset.len();
        Vector2 cameraTarget;

        if (distance > deadzoneRadius) {
            float desiredOffsetLength = MathUtils.clamp(distance, deadzoneRadius, maxOffset);
            Vector2 desiredOffset = offset.setLength(desiredOffsetLength * 0.3f); // Scale down the influence
            cameraTarget = playerPos.cpy().add(desiredOffset);
        } else {
            cameraTarget = playerPos.cpy(); // Camera centered on player if mouse is within deadzone
        }

        float lerpFactor = 0.08f; // Adjusted for smoother/slower lerp
        camera.position.lerp(new Vector3(cameraTarget.x, cameraTarget.y, 0), lerpFactor);
        camera.update();
    }

    private void drawCustomCursor(SpriteBatch batch) {
        if (Gdx.input.isCursorCatched()) { // Only draw custom cursor if system one is hidden
            Vector2 mouseWorld = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            float cursorSize = 10f / PPM; // Size of cursor in world units
            batch.draw(cursorTexture,
                mouseWorld.x - cursorSize / 2f,
                mouseWorld.y - cursorSize / 2f,
                cursorSize, cursorSize);
        }
    }

    private void drawWeapon(SpriteBatch batch) {
        Vector2 playerPos = player.getPosition().cpy(); // Use center for rotation pivot
        Vector2 mouseWorld = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        Vector2 direction = mouseWorld.cpy().sub(playerPos).nor(); // Normalized direction
        float targetAngle = direction.angleDeg();

        player.setFacing(direction.x >= 0); // Set facing based on actual aim direction

        // Weapon offset from player center
        float weaponOffsetX = player.isFacing() ? 0.3f : -0.3f;
        float weaponOffsetY = -0.1f; // slight vertical offset
        Vector2 weaponPivotOffset = new Vector2(weaponOffsetX, weaponOffsetY);
        Vector2 weaponRenderPos = playerPos.cpy().add(weaponPivotOffset);

        float weaponTexWidthPPM = weaponTexture.getWidth() / PPM;
        float weaponTexHeightPPM = weaponTexture.getHeight() / PPM;

        float originXRelative = 0.15f * weaponTexWidthPPM; // Origin near the handle, relative to texture width in PPM
        float originYRelative = 0.5f * weaponTexHeightPPM; // Origin at vertical center of texture in PPM

        batch.draw(weaponTexture,
            weaponRenderPos.x - originXRelative, // Position so origin is at weaponRenderPos
            weaponRenderPos.y - originYRelative,
            originXRelative, // Origin X for rotation (in world units)
            originYRelative, // Origin Y for rotation (in world units)
            weaponTexWidthPPM,    // Width of texture (in world units)
            weaponTexHeightPPM,   // Height of texture (in world units)
            1f, 1f,      // Scale X, Scale Y
            weapon.getRenderAngle(), // Use smoothed angle from render loop
            0, 0,        // SrcX, SrcY
            weaponTexture.getWidth(), weaponTexture.getHeight(), // SrcWidth, SrcHeight
            false,       // FlipX
            !player.isFacing() // FlipY if not facing right to keep weapon upright relative to aim
        );
    }

    public void checkCheatCodes(GameController controller) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) passedTime += 60;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) controller.addXpToPlayer(player, player.getXPToNextLevel());
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) controller.increasePlayerHealth(player, 100); // More health
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) controller.spawnBoss();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) player.setMaxHealth(player.getMaxHealth() * 2);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) player.setDamageBoostTimer(600f); // Longer boost
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) player.setSpeedBoostTimer(600f);  // Longer boost
    }

    public void renderBullets(List<Bullet> bullets, SpriteBatch batch) {
        for (Bullet b : bullets) {
            b.getRenderSprite().setPosition(b.getPosition().x - b.getRenderSprite().getWidth()/2, b.getPosition().y - b.getRenderSprite().getHeight()/2);
            b.getRenderSprite().setRotation(b.getDirVector().angleDeg());
            b.getRenderSprite().draw(batch);
        }
    }

    public void renderEnemies(List<Enemy> enemies, SpriteBatch batch, float delta) {
        for (Enemy enemy : enemies) {
            enemy.getRenderer().render(batch, delta);
        }
    }

    public void renderXPs(List<XP> xps, SpriteBatch batch, float delta) {
        float scale = 0.025f / PPM; // Adjusted scale for XP orb
        float spriteWidth = xpTexture.getWidth() * scale;
        float spriteHeight = xpTexture.getHeight() * scale;
        for (XP xp : xps) {
            batch.draw(xpTexture, xp.getPosition().x - spriteWidth / 2f, xp.getPosition().y - spriteHeight / 2f, spriteWidth, spriteHeight);
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
        viewport.update(width, height, false); // Set false to not center camera, we do it manually or via player
        camera.position.set(player.getPosition().x, player.getPosition().y, 0); // Recenter on player if desired on resize
        camera.update();

        uiCamera.setToOrtho(false, width, height);
        uiCamera.update();

        if(pauseMenuStage != null) {
            pauseMenuStage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void dispose() {
        if (rayHandler != null) rayHandler.dispose();
        if (world != null) world.dispose();
        if (texture != null) texture.dispose();
        if (cursorTexture != null) cursorTexture.dispose();
        if (xpTexture != null) xpTexture.dispose();
        if (weaponTexture != null) weaponTexture.dispose();
        if (uiBatch != null) uiBatch.dispose();
        // font is from GameAPI.getAssetManager(), usually disposed there
        if (xpBarBackgroundTexture != null) xpBarBackgroundTexture.dispose();
        if (xpBarForegroundTexture != null) xpBarForegroundTexture.dispose();
        if (pauseMenuStage != null) pauseMenuStage.dispose();
//        if (playerRenderer != null) playerRenderer.dispose(); // If PlayerRenderer has disposable assets
    }

    public float getPassedTime() {
        return passedTime;
    }

    public void setPassedTime(float passedTime) {
        this.passedTime = passedTime;
    }
}
