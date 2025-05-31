package sanctious.minini.Models.Game;

import sanctious.minini.Models.PlayerState;
import sanctious.minini.Models.Upgrade;

import java.util.ArrayList;
import java.util.List;

public class Player extends MovableObject{
    private PlayerState state = PlayerState.Idling;
    private final float defaultSpeed;
    private Weapon activeWeapon;
    private float maxHealth;
    private float health;
    private boolean facing = false; // false == left
    private float xp = 0;
    private int level = 1;
    private int kills = 0;
    private final float invincibilityDuration = 2f;
    private float invincibilityTimer = 0;
    private List<Upgrade> upgrades = new ArrayList<>();

    private float speedBoostTimer = 0;
    private float damageBoostTimer = 0;

    public Player(float health, float defaultSpeed) {
        this.maxHealth = health;
        this.health = maxHealth;
        this.defaultSpeed = defaultSpeed;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (speedBoostTimer != 0) speedBoostTimer -= delta;
        if (speedBoostTimer < 0) speedBoostTimer = 0;
        if (damageBoostTimer != 0) damageBoostTimer -= delta;
        if (damageBoostTimer< 0) damageBoostTimer = 0;

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

    public float getXP() {
        return xp;
    }

    public void setXP(float xp) {
        this.xp = xp;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
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

    public boolean isInvincible(){
        return invincibilityTimer > 0;
    }

    public float getDefaultSpeed() {
        return defaultSpeed;
    }

    public void addUpgrade(Upgrade upgrade){
        this.upgrades.add(upgrade);
    }

    public void setSpeedBoostTimer(float speedBoostTimer) {
        this.speedBoostTimer = speedBoostTimer;
    }

    public void setDamageBoostTimer(float damageBoostTimer) {
        this.damageBoostTimer = damageBoostTimer;
    }

    public float getSpeedBoostTimer() {
        return speedBoostTimer;
    }

    public float getDamageBoostTimer() {
        return damageBoostTimer;
    }

    public boolean hasDamageBoost(){
        return damageBoostTimer > 0;
    }

    public boolean hasSpeedBoost(){
        return speedBoostTimer > 0;
    }

    public float getHealth() {
        return health;
    }

    public void addHealth(float amount){
        this.health = Math.min(health + amount, maxHealth);
    }

    public int getKills() {
        return kills;
    }

    public void incrementKills(){
        this.kills++;
    }

    public float getXPToNextLevel(){
        return this.level * 20;
    }
}
