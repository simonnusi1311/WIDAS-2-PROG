package thd.gameobjects.base;

import thd.gameobjects.movable.JetFighter;

/**
 * Base enum for game objects that gets destroyed,
 * by {@link JetFighter}.
 * Each state shows one image of the explosion.
 */
public enum ExplosionState {

    /**
     * First explosion animation.
     */
    EXPLOSION_1("explosion_one.png"),
    /**
     * Second explosion animation.
     */
    EXPLOSION_2("explosion_two.png"),
    /**
     * Third explosion animation.
     */
    EXPLOSION_3("explosion_three.png");

    private final String image;

    ExplosionState(String image) {
        this.image = image;
    }

    /**
     * Goes to the next explosion state.
     *
     * @return The next explosion state.
     */
    public ExplosionState next() {
        return values()[(ordinal() + 1) % values().length];
    }

    /**
     * Gets the image file name for the current explosion state.
     *
     * @return the image file name.
     */
    public String getImage() {
        return image;
    }
}
