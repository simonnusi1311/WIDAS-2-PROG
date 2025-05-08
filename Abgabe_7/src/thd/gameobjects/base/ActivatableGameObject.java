package thd.gameobjects.base;

/**
 * The interface is responsible for all inactive game objects at
 * the beginning of the loop.
 *
 * @param <T> the type of game object in the respective context to determine activation.
 */
public interface ActivatableGameObject<T> {

    /**
     * Attempts to activate the object based on the provided override in the method.
     *
     * @param info an object containing context information.
     * @return {@code true} if the object can be activated during the loop.
     */
    boolean tryToActivate(T info);
}
