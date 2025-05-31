package sanctious.minini.Models.Game;

public class Weapon {
    private WeaponType type;
    private float renderAngle = 0f;
    private int ammoInClip = 0;
    private float reloadTime;
    private float reloadTimer = 0f;
    private boolean reloading = false;

    private float fireRateTimer = 0f;

    public Weapon(WeaponType type) {
        this.type = type;
        this.ammoInClip = type.getMaxClipSize();
        this.reloadTime = type.getReloadTime();
    }

    public void update(float delta) {
        if (reloading) {
            reloadTimer += delta;
            if (reloadTimer >= reloadTime) {
                finishReload();
            }
        }
        if (fireRateTimer > 0) {
            fireRateTimer -= delta;
        }
    }

    public boolean isClipFull(){
        return type.getMaxClipSize() == ammoInClip;
    }

    public boolean tryShoot() {
        if (reloading || fireRateTimer > 0) return false;
        if (ammoInClip > 0) {
            ammoInClip--;
            fireRateTimer = 1f / type.getFireRate();
            return true;
        } else {
            startReload();
            return false;
        }
    }

    public void startReload() {
        if (!reloading && ammoInClip < type.getMaxClipSize()) {
            reloading = true;
            reloadTimer = 0f;
        }
    }

    private void finishReload() {
        ammoInClip = type.getMaxClipSize();
        reloading = false;
    }

    public boolean isReloading() {
        return reloading;
    }

    public float getRenderAngle() {
        return renderAngle;
    }

    public void setRenderAngle(float renderAngle) {
        this.renderAngle = renderAngle;
    }

    public WeaponType getType() {
        return type;
    }

    public int getAmmoInClip() {
        return ammoInClip;
    }

    public int getMaxClipSize() {
        return type.getMaxClipSize();
    }

    public float getReloadTime() {
        return reloadTime;
    }

    public float getReloadTimer() {
        return reloadTimer;
    }

    public float getFireRateTimer() {
        return fireRateTimer;
    }
}
