public enum ChickenType {
    SMALL(3, 10),
    MEDIUM(6, 25),
    BIG(10, 40);

    public final int hp;
    public final int score;

    ChickenType(int hp, int score) {
        this.hp = hp;
        this.score = score;
    }
}
