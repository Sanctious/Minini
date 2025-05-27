    package sanctious.minini.Controllers;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Input;
    import com.badlogic.gdx.InputProcessor;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.TextureAtlas;
    import com.badlogic.gdx.math.MathUtils;
    import com.badlogic.gdx.math.Rectangle;
    import com.badlogic.gdx.math.Vector2;
    import sanctious.minini.Models.Game.Bullet;
    import sanctious.minini.Models.Game.Enemy;
    import sanctious.minini.Models.Game.Player;
    import sanctious.minini.Models.Game.Weapon;
    import sanctious.minini.Models.PlayerState;
    import sanctious.minini.View.EnemyRenderer;

    import java.util.ArrayList;
    import java.util.List;

    public class GameController implements InputProcessor {
        private final float spawnInterval = 5f;
        private float spawnTimer = 0f;
        private final Texture bulletTexture = new Texture(Gdx.files.internal("hit/T_Shotgun_SS_1.png"));
        // TODO proper usage for these ??
        private final List<Enemy> enemies = new ArrayList<>();
        private final List<Bullet> bullets = new ArrayList<>();

        public void shoot(Player player,
                          Vector2 mouseWorld) {
            if (!player.getActiveWeapon().tryShoot()) return;

            int pelletCount = 4; // Number of bullets per shot
            float spreadAngle = 20f; // Total spread in degrees (e.g., ±10 degrees)
            float bulletSpeed = 2f; // Bullet speed

            Vector2 dir = new Vector2(mouseWorld).sub(player.getPosition());

            for (int i = 0; i < pelletCount; i++) {
                // Random angle in range [-spreadAngle/2, +spreadAngle/2]
                float angleOffset = MathUtils.random(-spreadAngle / 2f, spreadAngle / 2f);

                // Rotate the base direction
                Vector2 pelletDir = new Vector2(dir).rotateDeg(angleOffset).nor().scl(bulletSpeed);

                // Create bullet with position and direction
                Bullet b = new Bullet(player.getPosition().cpy(), pelletDir, bulletTexture, player.getActiveWeapon().getType());

                // Add bullet to your list
                bullets.add(b);
            }


//            bullets.add(new Bullet(player.getPosition(), dir, bulletTexture));
        }



        public void updatePlayerPosition(Player player, float delta){
            Vector2 input = new Vector2();
            boolean running = false;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) running = true;
            if (Gdx.input.isKeyPressed(Input.Keys.W)) input.y += 1;
            if (Gdx.input.isKeyPressed(Input.Keys.S)) input.y -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) input.x -= 1;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) input.x += 1;

            if (input.x == -1) player.setFacing(false);
            else if (input.x == 1) player.setFacing(true);


            player.setState(PlayerState.Idling);
            player.setSpeed(0f);
            if (input.len2() > 0) {
                player.setState(PlayerState.Walking);
                player.setSpeed(2.5f);
                if (running) {
                    player.setState(PlayerState.Running);
                    player.setSpeed(5f);
                }
                player.setDirVector(input);
            }
        }

        public void tryReload(Player player){
            Weapon weapon = player.getActiveWeapon();
            if (weapon.isReloading() || weapon.isClipFull()) return;

            weapon.startReload();
        }

        public void checkCollisions(){
            List<Enemy> removeEnemies = new ArrayList<>();
            for (Enemy enemy : enemies) {
                List<Bullet> removeBullets = new ArrayList<>();
                for (Bullet bullet : bullets) {
                    Rectangle rect1 = new Rectangle(
                        bullet.getPosition().x,
                        bullet.getPosition().y,
                        bullet.getRenderSprite().getWidth(),
                        bullet.getRenderSprite().getHeight()
                    );
                    Rectangle rect2 = new Rectangle(
                        enemy.getPosition().x,
                        enemy.getPosition().y,
                        enemy.getRenderer().getFrame().getRegionWidth(),
                        enemy.getRenderer().getFrame().getRegionHeight()
                    );

                    if (rect1.overlaps(rect2)){
                        removeBullets.add(bullet);
                        enemy.modifyHealth(-bullet.getWeaponType().getDamage());

                        if (enemy.isDead()) {
                            // drop stuff here
                            removeEnemies.add(enemy);
                        }
                    }


                }
                bullets.removeAll(removeBullets);
            }
            enemies.removeAll(removeEnemies);
        }

        public List<Enemy> getEnemies() {
            return enemies;
        }

        public List<Bullet> getBullets() {
            return bullets;
        }

        public void updateBullets(float delta) {
            bullets.forEach(bullet ->bullet.update(delta));
        }

        public void updateEnemies(Player player, float delta) {
            enemies.forEach(enemy -> enemy.update(player, delta));

        }

        public void trySpawnEnemies(Player player, float delta){
            spawnTimer += delta;
            if (spawnTimer < spawnInterval) return;

            spawnTimer = 0;
            float radius = 1000f;
            float angle = MathUtils.random(0f, 360f);

            float enemyX = player.getPosition().x + MathUtils.cosDeg(angle) * radius;
            float enemyY = player.getPosition().y + MathUtils.sinDeg(angle) * radius;

            Enemy enemy = new Enemy(new Vector2(enemyX,enemyY));
            EnemyRenderer renderer = new EnemyRenderer(enemy, new TextureAtlas(Gdx.files.internal("BrainMonster.atlas")));
            enemy.setRenderer(renderer);
            enemies.add(enemy);
        }


        // Proper way to implement this :)
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
