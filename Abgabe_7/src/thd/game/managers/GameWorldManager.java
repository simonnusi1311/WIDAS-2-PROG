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
    private final String world2;
    private final int worldOffsetColumns;
    private final int worldOffsetLines;
    private final List<GameObject> activatableGameObjects;

    protected GameWorldManager(GameView gameView) {
        super(gameView);
        world = """
                                                                          F                                                                                     \s
                                                                                                                                                                \s
                                                                                           S                                                                    \s
                G                                                                                                                                               \s
                                                                          H                                                                                     \s
                                                                                                                                                                \s
                                                          B                                                                                                     \s
                                                                                                 H                                                              \s
                                                                                                                                                                \s
                                                                                                                                                                \s
                                                    B                                                                                                           \s
                                                                                                                                                                \s
                                                                                              B                                                                 \s
                                                                                                                                                                \s
                                                                                                                                                               G\s
                G                                                                       H                                                                       \s
                                                                                                                                                                \s
                                                                                                                                                                \s
                                                                                                                                                                \s
                                                                                                 B                                                              \s
                                                                                                                                                                \s
                                                S                                                                                                              G\s
                                                                                                                                                                \s
                                                                                                                                                                \s
                                                             B                                                                                                  \s
                                                                                              B                                                                 \s
                                                        S                                                                                                       \s
                                                                                                                                                                \s
                                                                     H                                                                                          \s
                                                                                                                                                                \s
                G                                                                                                                                               \s
                                                                                                                                                                \s
                                                                                    B                                                                           \s
                                                                                                                                                                \s
                                                                                            H                                                                    \s
                                                        S                                                                                                       \s
                                                                                                                                                                \s
                                                                          F                                                                                     \s
                                                                                                                                                                \s
                                           H                                                                                                                    \s
                                                                                                                                                                \s
                                                                        S                                                                                       \s
                                                                                                                                                                \s
                                                                                                                                                                \s
                                                        B                                                                                                       \s
                                                                                                                                                                \s
                                                                                                                                                                \s
                                                        H                                                                                                       \s
                                                                                                                                                                \s
                                                                                           H                                                                    \s
                                                                                                                                                               G\s
                                                                                                                                                                \s
                                                                                                                                                                \s
                                                                                                                                                               \s
                                                                                                                                                                 \s
                L                                                                  R                                                                            \s
                """;

        world2 = "B";


        worldOffsetColumns = 0;
        worldOffsetLines = 50;
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
        int factorForXCoordinate = 11;
        int factorForYCoordinate = 50;
        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            for (int columnIndex = 0; columnIndex < lines[lineIndex].length(); columnIndex++) {
                char character = lines[lineIndex].charAt(columnIndex);
                double x = (columnIndex - worldOffsetColumns) * factorForXCoordinate;
                double y = (lineIndex - worldOffsetLines) * factorForYCoordinate;
                if (character == 'B') {
                    Balloon balloon = new Balloon(gameView, this);
                    balloon.getPosition().updateCoordinates(x, y);
                    if (lineIndex < worldOffsetLines) {
                        addActivatableGameObject(balloon);
                    } else {
                        spawnGameObject(balloon);
                    }
                } else if (character == 'S') {
                    Ship ship = new Ship(gameView, this);
                    ship.getPosition().updateCoordinates(x, y);
                    if (lineIndex < worldOffsetLines) {
                        addActivatableGameObject(ship);
                    } else {
                        spawnGameObject(ship);
                    }
                } else if (character == 'H') {
                    Helicopter helicopter = new Helicopter(gameView, this);
                    helicopter.getPosition().updateCoordinates(x, y);
                    if (lineIndex < worldOffsetLines) {
                        addActivatableGameObject(helicopter);
                    } else {
                        spawnGameObject(helicopter);
                    }
                } else if (character == 'G') {
                    GreyJet greyJet = new GreyJet(gameView, this);
                    greyJet.getPosition().updateCoordinates(x, y);
                    greyJet.initializeTheSpawnPoint(columnIndex < 40);
                    if (lineIndex < worldOffsetLines) {
                        addActivatableGameObject(greyJet);
                    } else {
                        spawnGameObject(greyJet);
                    }
                } else if (character == 'F') {
                    FuelItem fuelItem = new FuelItem(gameView, this);
                    fuelItem.getPosition().updateCoordinates(x, y);
                    if (lineIndex < worldOffsetLines) {
                        addActivatableGameObject(fuelItem);
                    } else {
                        spawnGameObject(fuelItem);
                    }
                } else if (character == 'L') {
                    BridgeLeft bridgeLeft = new BridgeLeft(gameView, this);
                    jetFighter.addPathDecisionObjects(bridgeLeft);
                    bridgeLeft.getPosition().updateCoordinates(x, y);
                    if (lineIndex < worldOffsetLines) {
                        addActivatableGameObject(bridgeLeft);
                    } else {
                        spawnGameObject(bridgeLeft);
                    }
                } else if (character == 'R') {
                    BridgeRight bridgeRight = new BridgeRight(gameView, this);
                    jetFighter.addPathDecisionObjects(bridgeRight);
                    bridgeRight.getPosition().updateCoordinates(x, y);
                    if (lineIndex < worldOffsetLines) {
                        addActivatableGameObject(bridgeRight);
                    } else {
                        spawnGameObject(bridgeRight);
                    }
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
            } else if (gameObject instanceof Balloon balloon) {
                if (balloon.tryToActivate(jetFighter)) {
                    spawnGameObject(balloon);
                    iterator.remove();
                }
            } else if (gameObject instanceof Ship ship) {
                if (ship.tryToActivate(jetFighter)) {
                    spawnGameObject(ship);
                    iterator.remove();
                }
            } else if (gameObject instanceof Helicopter helicopter) {
                if (helicopter.tryToActivate(jetFighter)) {
                    spawnGameObject(helicopter);
                    iterator.remove();
                }

            } else if (gameObject instanceof GreyJet greyJet) {
                if (greyJet.tryToActivate(jetFighter)) {
                    spawnGameObject(greyJet);
                    iterator.remove();
                }
            } else if (gameObject instanceof BridgeLeft bridgeLeft) {
                if (bridgeLeft.tryToActivate(jetFighter)) {
                    spawnGameObject(bridgeLeft);
                    iterator.remove();
                }
            } else if (gameObject instanceof BridgeRight bridgeRight) {
                if (bridgeRight.tryToActivate(jetFighter)) {
                    spawnGameObject(bridgeRight);
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
