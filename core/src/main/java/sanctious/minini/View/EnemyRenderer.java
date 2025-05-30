package sanctious.minini.View;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import sanctious.minini.Models.Game.Enemies.Enemy;

public class EnemyRenderer {
    private final Enemy enemy;
    private final Animation<TextureRegion> walkAnim;
    private TextureRegion frame = null; // doesn't it cause issues
    private float stateTime = 0;
//    private final Animation<TextureRegion> idle;
//    private final Animation<TextureRegion> running;

    public EnemyRenderer(Enemy enemy, TextureAtlas atlas, String regionName) {
        this.enemy = enemy;
        walkAnim = new Animation<>(0.3f, atlas.findRegions(regionName), Animation.PlayMode.LOOP);
        frame = walkAnim.getKeyFrame(0, true);
//        idle = new Animation<>(0.1f, atlas.findRegions("Idle"), Animation.PlayMode.LOOP);
//        running = new Animation<>(0.1f, atlas.findRegions("Run"), Animation.PlayMode.LOOP);
    }

    public TextureRegion getFrame() {
        return frame;
    }

    public void render(SpriteBatch batch, float delta) {
        stateTime += delta;
        frame = walkAnim.getKeyFrame(stateTime, true);
//        TextureRegion frame = switch (player.getState()){
//            case Idling -> idle.getKeyFrame(stateTime, true);
//            case Walking -> walkAnim.getKeyFrame(stateTime, true);
//            case Running -> running.getKeyFrame(stateTime, true);
//            default -> null;
//        };

        float scale = 0.03f;
        float spriteWidth = frame.getRegionWidth() * scale;
        float spriteHeight = frame.getRegionHeight() * scale;

        float drawX = enemy.getPosition().x - spriteWidth / 2f;
        float drawY = enemy.getPosition().y - spriteHeight / 2f;

        batch.draw(frame, drawX, drawY, spriteWidth, spriteHeight);
    }


}
