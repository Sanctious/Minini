package sanctious.minini.View;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import elemental2.dom.AddEventListenerOptions;
import sanctious.minini.Models.Game.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;

public class PlayerRenderer {
    private final Animation<TextureRegion> walkAnim;
    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> running;
    private TextureRegion frame;

    private float flickerDuration = 0.5f;
    private float minAlpha = 0.2f;
    private float maxAlpha = 0.9f;
    private float flickerTimer = 0f;
    private boolean isFlickering = false;

    public PlayerRenderer(TextureAtlas atlas) {
        walkAnim = new Animation<>(0.1f, atlas.findRegions("Walk"), Animation.PlayMode.LOOP);
        idle = new Animation<>(0.1f, atlas.findRegions("Idle"), Animation.PlayMode.LOOP);
        running = new Animation<>(0.1f, atlas.findRegions("Run"), Animation.PlayMode.LOOP);
        frame = idle.getKeyFrame(0, true);
    }

    public void render(SpriteBatch batch, Player player, float delta, float stateTime) {
        frame = switch (player.getState()){
            case Idling -> idle.getKeyFrame(stateTime, true);
            case Walking -> walkAnim.getKeyFrame(stateTime, true);
            case Running -> running.getKeyFrame(stateTime, true);
            default -> null;
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

        if (player.isInvincible()) {
            flickerTimer += delta;
            if (flickerTimer > flickerDuration) {
                flickerTimer -= flickerDuration;
            }

            float alpha = (float) (Math.sin(flickerTimer / flickerDuration * Math.PI * 2) * 0.5 + 0.5);
            alpha = minAlpha + (maxAlpha - minAlpha) * alpha;

            Color color = batch.getColor();
            batch.setColor(color.r, color.g, color.b, alpha);
        } else {
            flickerTimer = 0;
            Color color = batch.getColor();
            batch.setColor(color.r, color.g, color.b, maxAlpha);
        }

        batch.draw(frame, drawX, drawY, spriteWidth, spriteHeight);

        batch.setColor(Color.WHITE);
    }

    public TextureRegion getFrame() {
        return frame;
    }
}
