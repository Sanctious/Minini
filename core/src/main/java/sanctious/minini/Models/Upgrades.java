package sanctious.minini.Models;

import com.badlogic.gdx.math.MathUtils;

public enum Upgrades {
    Vitality,
    Damager,
    Procrease,
    Amocrease,
    Speedy,
    ;

    public static Upgrades getRandomUpgrade() {
        return values()[MathUtils.random.nextInt(values().length)];
    }
}
