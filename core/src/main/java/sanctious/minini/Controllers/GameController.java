    package sanctious.minini.Controllers;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Input;
    import com.badlogic.gdx.InputProcessor;
    import com.badlogic.gdx.graphics.OrthographicCamera;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.TextureAtlas;
    import com.badlogic.gdx.graphics.g2d.TextureRegion;
    import com.badlogic.gdx.math.MathUtils;
    import com.badlogic.gdx.math.Rectangle;
    import com.badlogic.gdx.math.Vector2;
    import com.badlogic.gdx.math.Vector3;
    import com.badlogic.gdx.utils.viewport.Viewport;
    import sanctious.minini.Models.Game.*;
    import sanctious.minini.Models.Game.Enemies.*;
    import sanctious.minini.Models.GameAPI;
    import sanctious.minini.Models.PlayerState;
    import sanctious.minini.Models.Upgrade;
    import sanctious.minini.Models.ViewResult;
    import sanctious.minini.View.EnemyRenderer;
    import sanctious.minini.View.GameScreen;
    import sanctious.minini.View.PlayerRenderer;

    import java.util.ArrayList;
    import java.util.Comparator;
    import java.util.List;

    public class GameController implements InputProcessor {
        private float brainMonsterSpawnTimer = 0f;
        private float brainMonsterSpawnInterval = 3f;

        private float batMonsterSpawnTimer = 0f;
        private float batMonsterSpawnInterval = 10f;

        private float bossMonsterSpawnTimer = 0f;
        private float bossMonsterSpawnInterval = 100f;
        private boolean spawnBoss = false;

        private final Texture bulletTexture = new Texture(Gdx.files.internal("hit/T_Shotgun_SS_1.png"));
        // TODO proper usage for these ??
        private final List<Enemy> enemies = new ArrayList<>();
        private final List<Bullet> bullets = new ArrayList<>();
        private final List<XP> xps = new ArrayList<>();

        public void initializeMap(){
            TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("enemies/TreeMonster.atlas"));

            for (int i = 0; i < 40; i++){
                int randomX = MathUtils.random(-100, 100);
                int randomY = MathUtils.random(-100, 100);

                TreeEnemy treeEnemy = new TreeEnemy(new Vector2(randomX, randomY));
                EnemyRenderer renderer = new EnemyRenderer(treeEnemy, atlas, "TreeMonster");
                treeEnemy.setRenderer(renderer);
                enemies.add(treeEnemy);
            }
        }

        public void shoot(Player player,
                          Vector2 mouseWorld) {
            Weapon weapon = player.getActiveWeapon();
            if (!weapon.tryShoot()) return;

            int pelletCount = weapon.getType().getNumBullets();
            float spreadAngle = pelletCount * 2.5f;
            float bulletSpeed = 2f;

            Vector2 dir = new Vector2(mouseWorld).sub(player.getPosition());

            for (int i = 0; i < pelletCount; i++) {
                float angleOffset = MathUtils.random(-spreadAngle / 2f, spreadAngle / 2f);

                Vector2 pelletDir = new Vector2(dir).rotateDeg(angleOffset).nor().scl(bulletSpeed);

                Bullet b = new Bullet(player.getPosition().cpy(), pelletDir, bulletTexture, player.getActiveWeapon().getType(), player);

                bullets.add(b);
            }
        }

        public void applyUpgrade(Player player, Upgrade upgrade){
            WeaponType weaponType = player.getActiveWeapon().getType();
            switch (upgrade){
                case Vitality -> {
                    player.setMaxHealth(player.getMaxHealth() + 1);
                }
                case Procrease -> {
                    weaponType.setNumBullets(weaponType.getNumBullets() + 1);
                }
                case Speedy -> {
                    player.setSpeedBoostTimer(10);
                }
                case Amocrease -> {
                    weaponType.setMaxClipSize(weaponType.getMaxClipSize() + 4);
                }
                case Damager -> {
                    player.setDamageBoostTimer(10);
                }
            }
        }


        public void increasePlayerHealth(Player player, int amount){
            player.addHealth(amount);
        }

        public void autoAim(Player player, OrthographicCamera camera, Viewport viewport) {
            Enemy closestEnemy = this.enemies.stream()
                .filter(e -> e != null && e.getPosition() != null && !e.isDead())
                .min(Comparator.comparingDouble(e -> player.getPosition().dst2(e.getPosition())))
                .orElse(null);

            if (closestEnemy != null) {
                Vector2 enemyWorldPosition = closestEnemy.getPosition();

                Vector3 enemyScreenPositionVec = new Vector3(enemyWorldPosition.x, enemyWorldPosition.y, 0);
                camera.project(enemyScreenPositionVec,
                    viewport.getScreenX(),
                    viewport.getScreenY(),
                    viewport.getScreenWidth(),
                    viewport.getScreenHeight());

                int screenX = (int) enemyScreenPositionVec.x;
                int screenY = Gdx.graphics.getHeight() - (int) enemyScreenPositionVec.y;

                Gdx.input.setCursorPosition(screenX, screenY);

                shoot(player, enemyWorldPosition);
            }
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

            float speedModifier = player.hasSpeedBoost() ? 2 : 1;
            player.setState(PlayerState.Idling);
            player.setSpeed(0f);
            if (input.len2() > 0) {
                player.setState(PlayerState.Walking);
                player.setSpeed(player.getDefaultSpeed() * speedModifier);
                if (running) {
                    player.setState(PlayerState.Running);
                    player.setSpeed(player.getDefaultSpeed() * 1.5f * speedModifier);
                }
                player.setDirVector(input);
            }
        }

        public void tryReload(Player player){
            Weapon weapon = player.getActiveWeapon();
            if (weapon.isReloading() || weapon.isClipFull()) return;

            weapon.startReload();
        }

        public void addXpToPlayer(Player player, float amount){
            player.addXP(amount);

            // Level up player
            while (Float.compare(player.getXP(), player.getXPToNextLevel()) >= 0){
                player.setXP(player.getXP() - player.getXPToNextLevel());
                applyUpgrade(player, Upgrade.getRandomUpgrade());
                player.setLevel(player.getLevel() + 1);
            }
        }

        public void checkCollisions(Player player, PlayerRenderer renderer){
            List<Bullet> removeBulletsEnemies = new ArrayList<>();

            TextureRegion playerSprite = renderer.getFrame();
            float playerWidth = playerSprite.getRegionWidth() / GameScreen.PPM;
            float playerHeight = playerSprite.getRegionHeight() / GameScreen.PPM;
            Rectangle playerRect = new Rectangle(
                player.getPosition().x - playerWidth / 2f,
                player.getPosition().y - playerHeight / 2f,
                playerWidth,
                playerHeight
            );

            for (Bullet bullet : bullets) {
                if (bullet.getShooter() instanceof Player) continue;

                TextureRegion bulletSprite = bullet.getRenderSprite();
                float bulletWidth = bulletSprite.getRegionWidth() / GameScreen.PPM;
                float bulletHeight = bulletSprite.getRegionHeight() / GameScreen.PPM;

                Rectangle bulletRect = new Rectangle(
                    bullet.getPosition().x - bulletWidth / 2f,
                    bullet.getPosition().y - bulletHeight / 2f,
                    bulletWidth,
                    bulletHeight
                );


                if (bulletRect.overlaps(playerRect)){
                    removeBulletsEnemies.add(bullet);

                    player.damage(((Enemy) bullet.getShooter()).getDamage(), true);
                }


            }
            bullets.removeAll(removeBulletsEnemies);

            List<XP> removeXPs = new ArrayList<>();
            for (XP xp : xps) {
                Texture xpRenderSprite = GameScreen.xpTexture;
                float scale = 0.032f / GameScreen.PPM;
                float xpWidth = xpRenderSprite.getWidth() * scale;
                float xpHeight = xpRenderSprite.getHeight() * scale;

                Rectangle xpRect = new Rectangle(
                    xp.getPosition().x - xpWidth / 2f,
                    xp.getPosition().y - xpHeight / 2f,
                    xpWidth,
                    xpHeight
                );


                if (xpRect.overlaps(playerRect)){
                    removeXPs.add(xp);

                    addXpToPlayer(player, xp.getXp());
                }
            }
            xps.removeAll(removeXPs);

            List<Enemy> removeEnemies = new ArrayList<>();
            for (Enemy enemy : enemies) {
                List<Bullet> removeBullets = new ArrayList<>();

                TextureRegion enemySprite = enemy.getRenderer().getFrame();
                float enemyWidth = enemySprite.getRegionWidth() / GameScreen.PPM;
                float enemyHeight = enemySprite.getRegionHeight() / GameScreen.PPM;

                Rectangle enemyRect = new Rectangle(
                    enemy.getPosition().x - enemyWidth / 2f,
                    enemy.getPosition().y - enemyHeight / 2f,
                    enemyWidth,
                    enemyHeight
                );

                for (Bullet bullet : bullets) {
                    if (bullet.getShooter() instanceof Enemy) continue;

                    TextureRegion bulletSprite = bullet.getRenderSprite();
                    float bulletWidth = bulletSprite.getRegionWidth() / GameScreen.PPM;
                    float bulletHeight = bulletSprite.getRegionHeight() / GameScreen.PPM;

                    Rectangle bulletRect = new Rectangle(
                        bullet.getPosition().x - bulletWidth / 2f,
                        bullet.getPosition().y - bulletHeight / 2f,
                        bulletWidth,
                        bulletHeight
                    );

                    if (bulletRect.overlaps(enemyRect)){
                        if (enemy.isDead()) continue;

                        removeBullets.add(bullet);
                        float damageModifier = player.hasDamageBoost() ? 1.25f : 1f;
                        enemy.modifyHealth(-bullet.getWeaponType().getDamage() *  damageModifier);
                        // knockback
                        enemy.getPosition().add(bullet.getDirVector().cpy().scl(1f));

                        if (enemy.isDead()) {
                            // drop stuff here
                            spawnXPPoints(enemy.getPosition(), 5f);
                            player.incrementKills();
                            removeEnemies.add(enemy);
                        }
                    }


                }
                bullets.removeAll(removeBullets);

                if (enemyRect.overlaps(playerRect)){
                    player.damage(enemy.getDamage(), true);
                }


            }
            enemies.removeAll(removeEnemies);
        }

        public List<Enemy> getEnemies() {
            return enemies;
        }

        public List<Bullet> getBullets() {
            return bullets;
        }

        public List<XP> getXps() {
            return xps;
        }

        public void updateBullets(float delta) {
            bullets.forEach(bullet ->bullet.update(delta));
        }

        public void updateEnemies(Player player, float delta) {
            enemies.forEach(enemy -> enemy.update(player, delta));
            enemies.forEach(enemy -> {
                if (enemy instanceof BatMonster batMonster){
                    if (batMonster.readyToShoot()){
                        Bullet b = new Bullet(batMonster.getPosition().cpy(), batMonster.getDirVector(), bulletTexture, null, batMonster);
                        bullets.add(b);
                        batMonster.setShootTimer(0);
                    }
                }
            });

        }

        public void trySpawnEnemies(Player player, float passedTime, float gameDuration, float delta){
            brainMonsterSpawnTimer += delta;
            brainMonsterSpawnTimer -= delta/20;

            batMonsterSpawnTimer += delta;
            if (passedTime >= gameDuration/4) batMonsterSpawnInterval -= delta/20;

            bossMonsterSpawnTimer += delta;
            if (passedTime >= gameDuration/2) bossMonsterSpawnInterval -= delta/20;


            if (brainMonsterSpawnTimer >= brainMonsterSpawnInterval){
                brainMonsterSpawnTimer = 0;
                float radius = 20f;
                for (int i = 0; i < passedTime/30 + 1; i++){
                    float angle = MathUtils.random(0f, 360f);

                    float enemyX = player.getPosition().x + MathUtils.cosDeg(angle) * radius;
                    float enemyY = player.getPosition().y + MathUtils.sinDeg(angle) * radius;
                    BrainMonster brainMonster = new BrainMonster(new Vector2(enemyX,enemyY));
                    EnemyRenderer renderer = new EnemyRenderer(brainMonster, new TextureAtlas(Gdx.files.internal("enemies/BrainMonster.atlas")), "BrainMonster");
                    brainMonster.setRenderer(renderer);
                    enemies.add(brainMonster);
                }
            }

            if (batMonsterSpawnTimer >= batMonsterSpawnInterval && passedTime >= gameDuration/4){
                batMonsterSpawnTimer = 0;
                float radius = 20f;
                for (int i = 0; i < Math.max((4*passedTime - gameDuration + 30)/30, 1); i++){
                    float angle = MathUtils.random(0f, 360f);

                    float enemyX = player.getPosition().x + MathUtils.cosDeg(angle) * radius;
                    float enemyY = player.getPosition().y + MathUtils.sinDeg(angle) * radius;
                    BatMonster batMonster = new BatMonster(new Vector2(enemyX,enemyY));
                    EnemyRenderer renderer = new EnemyRenderer(batMonster, new TextureAtlas(Gdx.files.internal("enemies/EyeBat.atlas")), "EyeBat");
                    batMonster.setRenderer(renderer);
                    enemies.add(batMonster);
                }
            }

            if ((bossMonsterSpawnTimer >= bossMonsterSpawnInterval && passedTime >= gameDuration/2) || spawnBoss){
                bossMonsterSpawnTimer = 0;
                spawnBoss = false;
                float radius = 20f;
                float angle = MathUtils.random(0f, 360f);

                float enemyX = player.getPosition().x + MathUtils.cosDeg(angle) * radius;
                float enemyY = player.getPosition().y + MathUtils.sinDeg(angle) * radius;
                ElderMonster elderMonster = new ElderMonster(new Vector2(enemyX,enemyY));
                EnemyRenderer renderer = new EnemyRenderer(elderMonster, new TextureAtlas(Gdx.files.internal("enemies/ElderBoss.atlas")), "HasturBoss");
                elderMonster.setRenderer(renderer);
                enemies.add(elderMonster);
            }

        }

        public void spawnXPPoints(Vector2 position, float value){
            float count = MathUtils.random(2, 3);
            for (int i = 0; i < count; i++){
                float dx = MathUtils.random(-0.5f, 0.5f);
                float dy = MathUtils.random(-0.5f, 0.5f);

                XP xp = new XP(new Vector2(position.x + dx, position.y + dy), value/count);

                xps.add(xp);
            }
        }

        public ViewResult<Void> checkGameFinished(float gameDuration, float passedTime, Player player){
            if (player.getHealth() <= 0 ||
                passedTime >= gameDuration){

                // Finish logic here
                GameAPI.getUserRegistry().getActiveUser().getData().modifyKills(player.getKills());

                return ViewResult.empty(true);
            }

            return ViewResult.empty(false);
        }

        public void spawnBoss(){
            spawnBoss = true;
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
