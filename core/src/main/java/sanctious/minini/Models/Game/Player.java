package sanctious.minini.Models.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import sanctious.minini.Models.PlayerState;

public class Player extends MovableObject{
    private PlayerState state = PlayerState.Idling;
    private Weapon activeWeapon;
    public boolean facing = false; // false == left

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
}
