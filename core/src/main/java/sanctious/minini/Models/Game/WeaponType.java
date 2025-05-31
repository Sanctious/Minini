package sanctious.minini.Models.Game;

public enum WeaponType {
    Revolver(1f, 1, 1, 20f, 6),
    Shotgun(1f, 2, 4, 10f, 2),
    SMG(2f, 12, 1, 8f, 24),
    ;
    private float reloadTime;
    private int fireRate;
    private int numBullets;
    private float damage;
    private int maxClipSize;

    WeaponType(float reloadTime, int fireRate, int numBullets, float damage, int maxClipSize) {
        this.reloadTime = reloadTime;
        this.fireRate = fireRate;
        this.numBullets = numBullets;
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

    public int getFireRate() {
        return fireRate;
    }

    public int getNumBullets() {
        return numBullets;
    }

    public void setMaxClipSize(int maxClipSize) {
        this.maxClipSize = maxClipSize;
    }

    public void setNumBullets(int numBullets) {
        this.numBullets = numBullets;
    }
}
