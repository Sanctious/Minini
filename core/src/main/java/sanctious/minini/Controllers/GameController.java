    package sanctious.minini.Controllers;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Input;
    import com.badlogic.gdx.InputProcessor;
    import com.badlogic.gdx.math.Vector2;
    import sanctious.minini.Models.Bullet;
    import sanctious.minini.Models.Player;
    import sanctious.minini.Models.PlayerState;

    import java.util.ArrayList;
    import java.util.List;

    public class GameController implements InputProcessor {
        // TODO proper usage for these ??
        private final List<Bullet> bullets = new ArrayList<>();

        public void shoot(Player player,
                          Vector2 mouseWorld) {
            Vector2 dir = new Vector2(mouseWorld).sub(player.position);
            bullets.add(new Bullet(new Vector2(player.position), dir));
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
            if (input.len2() > 0) {
                player.setState(PlayerState.Walking);
                player.setSpeed(2.5f);
                if (running) {
                    player.setState(PlayerState.Running);
                    player.setSpeed(5f);
                }
                input.nor().scl(player.getSpeed() * delta);
                player.getPosition().add(input);
            }
        }

        public void updateBullets(float delta) {
    //        position.mulAdd(velocity, delta);
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
