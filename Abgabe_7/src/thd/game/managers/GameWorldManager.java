package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

class GameWorldManager extends GamePlayManager {
    private final String world;
    private final int worldOffsetColumns;
    private final int worldOffsetLines;
    private final List<GameObject> activatableGameObjects;

    protected GameWorldManager(GameView gameView) {
        super(gameView);
        world = """
                \s
                                                                           F                                                                                 \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                       S                                                                                                     \s
                                                                                                                                                             \s
                                                                                                              B                                              \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                               H                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                               H                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                         S                                                                                   \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                   B                                                                         \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                              L                                                                       R                                                      \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s
                                                                                                                                                             \s""";
        worldOffsetColumns = 14;
        worldOffsetLines = 4;
        activatableGameObjects = new LinkedList<>();
        score = new Score(gameView, this);
        redFuelBar = new RedFuelBar(gameView, this);
        jetFighter = new JetFighter(gameView, this, redFuelBar);
        fuelGage = new FuelGage(gameView, this);
        lifeCounter = new LifeCounter(gameView, this);
        statusBar = new StatusBar(gameView, this);
        sceneryLeft = new SceneryLeft(gameView, this);
        sceneryRight = new SceneryRight(gameView, this);
        spawnGameObjects();
        spawnGameObjectsFromWorldString();
    }

    private void spawnGameObjects() {
        spawnGameObject(statusBar);
        spawnGameObject(lifeCounter);
        spawnGameObject(jetFighter);
        spawnGameObject(fuelGage);
        spawnGameObject(score);
        spawnGameObject(redFuelBar);
        spawnGameObject(sceneryLeft);
        spawnGameObject(sceneryRight);
    }

    private void addActivatableGameObject(GameObject gameObject) {
        activatableGameObjects.add(gameObject);
        addToShiftableGameObjectsIfShiftable(gameObject);
    }

    private void spawnGameObjectsFromWorldString() {
        String[] lines = world.split("\\R");
        int factorForRepresentation = 10;
        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            for (int columnIndex = 0; columnIndex < lines[lineIndex].length(); columnIndex++) {
                char character = lines[lineIndex].charAt(columnIndex);
                double x = (columnIndex - worldOffsetColumns) * factorForRepresentation;
                double y = (lineIndex - worldOffsetLines) * factorForRepresentation;

                if (character == 'B') {
                    Balloon balloon = new Balloon(gameView, this);
                    balloon.getPosition().updateCoordinates(x, y);
                    spawnGameObject(balloon);
                } else if (character == 'S') {
                    Ship ship = new Ship(gameView, this);
                    ship.getPosition().updateCoordinates(x, y);
                    spawnGameObject(ship);
                } else if (character == 'H') {
                    Helicopter helicopter = new Helicopter(gameView, this);
                    helicopter.getPosition().updateCoordinates(x, y);
                    spawnGameObject(helicopter);
                } else if (character == 'G') {
                    GreyJet greyJet = new GreyJet(gameView, this);
                    greyJet.getPosition().updateCoordinates(x, y);
                    spawnGameObject(greyJet);
                } else if (character == 'F') {
                    FuelItem fuelItem = new FuelItem(gameView, this);
                    fuelItem.getPosition().updateCoordinates(x, y);
                    addActivatableGameObject(fuelItem);
                } else if (character == 'L') {
                    BridgeLeft bridgeLeft = new BridgeLeft(gameView, this);
                    jetFighter.addPathDecisionObjects(bridgeLeft);
                    bridgeLeft.getPosition().updateCoordinates(x, y);
                    spawnGameObject(bridgeLeft);
                } else if (character == 'R') {
                    BridgeRight bridgeRight = new BridgeRight(gameView, this);
                    jetFighter.addPathDecisionObjects(bridgeRight);
                    bridgeRight.getPosition().updateCoordinates(x, y);
                    spawnGameObject(bridgeRight);
                }
            }
        }
    }

    private void activateGameObjects() {
        ListIterator<GameObject> iterator = activatableGameObjects.listIterator();
        while (iterator.hasNext()) {
            GameObject gameObject = iterator.next();
            if (gameObject instanceof FuelItem fuelItem) {
                if (fuelItem.tryToActivate(jetFighter)) {
                    spawnGameObject(gameObject);
                    iterator.remove();
                }
            }
        }
    }

    @Override
    protected void gameLoop() {
        super.gameLoop();
        activateGameObjects();
    }
}
