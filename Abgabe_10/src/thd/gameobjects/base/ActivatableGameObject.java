package thd.gameobjects.base;

/**
 * The interface is responsible for Activation-Method Declarations.
 *
 * @param <T> the type of game object in the respective context to determine activation.
 */
public interface ActivatableGameObject<T> {

    /**
     * Offset From Before GameObject should activate.
     */
    int ACTIVATION_DISTANCE = 300;

    /**
     * Attempts to activate the object based on the provided override in the method.
     *
     * @param info an object containing context information.
     * @return {@code true} if the object can be activated during the loop.
     */
    boolean tryToActivate(T info);
}
