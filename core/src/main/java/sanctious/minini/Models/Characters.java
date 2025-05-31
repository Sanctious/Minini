package sanctious.minini.Models;

public enum Characters {
    Shana(4, 4),
    Diamond(7, 1),
    Scarlet(3, 5),
    Lilith(5, 3),
    Dasher(2, 10),

    ;
    private int hp;
    private float speed;

    Characters(int hp, float speed) {
        this.hp = hp;
        this.speed = speed;
    }
}
