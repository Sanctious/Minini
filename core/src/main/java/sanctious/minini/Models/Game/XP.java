package sanctious.minini.Models.Game;

import com.badlogic.gdx.math.Vector2;

public class XP extends MovableObject{
    private float xp;

    public XP(Vector2 pos, float xp) {
        this.xp = xp;
        this.position = pos;
    }

    public float getXp() {
        return xp;
    }
}
