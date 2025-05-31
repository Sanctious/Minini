package sanctious.minini.Models;

import com.badlogic.gdx.math.MathUtils;

public enum Upgrade {
    Vitality,
    Damager,
    Procrease,
    Amocrease,
    Speedy,
    ;

    public static Upgrade getRandomUpgrade() {
        return values()[MathUtils.random.nextInt(values().length)];
    }
}
