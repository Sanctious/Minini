package sanctious.minini.Models.Game;

public enum WeaponType {
    SMG(5f, 5f, 10)

    ;
    private float reloadTime;
    private float damage;
    private int maxClipSize;

    WeaponType(float reloadTime, float damage, int maxClipSize) {
        this.reloadTime = reloadTime;
        this.damage = damage;
        this.maxClipSize = maxClipSize;
    }

    public int getMaxClipSize() {
        return maxClipSize;
    }

    public float getReloadTime() {
        return reloadTime;
    }

    public float getDamage() {
        return damage;
    }
}
