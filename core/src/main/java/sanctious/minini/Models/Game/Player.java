package sanctious.minini.Models.Game;

import sanctious.minini.Models.PlayerState;

public class Player extends MovableObject{
    private PlayerState state = PlayerState.Idling;
    private final float defaultSpeed;
    private Weapon activeWeapon;
    private float health;
    private boolean facing = false; // false == left
    private float xp = 0;
    private int level = 0;
    private final float invincibilityDuration = 2f;
    private float invincibilityTimer = 0;

    public Player(float health, float defaultSpeed) {
        this.health = health;
        this.defaultSpeed = defaultSpeed;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (invincibilityTimer != 0) invincibilityTimer -= delta;
        if (invincibilityTimer < 0) invincibilityTimer = 0;
    }

    public void damage(float amount, boolean triggerInvincibility){
        if (triggerInvincibility){
            if (invincibilityTimer > 0) return;
            invincibilityTimer = invincibilityDuration;
        }

        this.health -= amount;
    }

    public boolean isDead(){
        return this.health <= 0;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void setFacing(boolean direction){
        facing = direction;
    }

    public boolean isFacing() {
        return facing;
    }

    public Weapon getActiveWeapon() {
        return activeWeapon;
    }

    public void setActiveWeapon(Weapon activeWeapon) {
        this.activeWeapon = activeWeapon;
    }

    public void addXP(float amount){
        this.xp += amount;
    }

    public float getXp() {
        return xp;
    }

    public void increaseLevel(){
        this.level++;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public float getDefaultSpeed() {
        return defaultSpeed;
    }
}
