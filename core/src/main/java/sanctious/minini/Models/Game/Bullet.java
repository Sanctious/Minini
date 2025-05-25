package sanctious.minini.Models.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import sanctious.minini.View.GameScreen;

public class Bullet extends MovableObject{
    private final Sprite renderSprite;


    public Bullet(Vector2 start,
                  Vector2 direction,
                  Texture renderTexture) {
        this.speed = 25f;
        this.position = start.cpy();
        this.dirVector = direction.nor().scl(speed);
        this.renderSprite = new Sprite(renderTexture);

//        float width = renderTexture.getWidth() / GameScreen.PPM;
//        float height = renderTexture.getHeight() / GameScreen.PPM;
//        this.renderSprite.setBounds(position.x, position.y, width, height);
//        this.renderSprite.setPosition(this.position.x, this.position.y);
//        this.renderSprite.setScale(0.02f);
        this.renderSprite.setSize(0.2f, 0.2f); // Width and height in world units
        this.renderSprite.setOriginCenter();
        this.renderSprite.setPosition(this.position.x, this.position.y);

    }

    public Sprite getRenderSprite() {
        return renderSprite;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        renderSprite.setRotation(renderSprite.getRotation() + 100f * delta);
        renderSprite.setPosition(this.position.x, this.position.y);
    }
}
