package sanctious.minini.View;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import sanctious.minini.Models.Player;

public class PlayerRenderer {
    private final Animation<TextureRegion> walkAnim;
    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> running;
    private float stateTime = 0f;

    public PlayerRenderer(TextureAtlas atlas) {
        walkAnim = new Animation<>(0.1f, atlas.findRegions("Walk"), Animation.PlayMode.LOOP);
        idle = new Animation<>(0.1f, atlas.findRegions("Idle"), Animation.PlayMode.LOOP);
        running = new Animation<>(0.1f, atlas.findRegions("Run"), Animation.PlayMode.LOOP);
    }

    public void render(SpriteBatch batch, Player player, float delta) {
        stateTime += delta;

        TextureRegion frame = switch (player.getState()){
            case Idling -> idle.getKeyFrame(stateTime, true);
            case Walking -> walkAnim.getKeyFrame(stateTime, true);
            case Running -> running.getKeyFrame(stateTime, true);
        };

        boolean facingRight = player.isFacing();

        if (facingRight && frame.isFlipX()) {
            frame.flip(true, false);
        } else if (!facingRight && !frame.isFlipX()) {
            frame.flip(true, false);
        }

        float scale = 0.05f;
        float spriteWidth = frame.getRegionWidth() * scale;
        float spriteHeight = frame.getRegionHeight() * scale;

        float drawX = player.getPosition().x - spriteWidth / 2f;
        float drawY = player.getPosition().y - spriteHeight / 2f;

        batch.draw(frame, drawX, drawY, spriteWidth, spriteHeight);
    }
}
