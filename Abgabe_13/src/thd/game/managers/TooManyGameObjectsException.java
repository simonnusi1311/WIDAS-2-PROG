package thd.game.managers;

class TooManyGameObjectsException extends RuntimeException {

    TooManyGameObjectsException(String message) {
        super(message);
    }
}
