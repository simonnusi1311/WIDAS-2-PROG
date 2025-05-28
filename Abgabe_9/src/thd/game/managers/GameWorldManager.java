package thd.game.managers;

import thd.game.utilities.GameView;
import thd.gameobjects.base.CollidingGameObject;
import thd.gameobjects.base.GameObject;
import thd.gameobjects.movable.*;
import thd.gameobjects.unmovable.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

class GameWorldManager extends GamePlayManager {
    private final List<GameObject> activatableGameObjects;
    private final List<CollidingGameObject> pathDecisionObjects;

    protected GameWorldManager(GameView gameView) {
        super(gameView);
        activatableGameObjects = new LinkedList<>();
        pathDecisionObjects = new LinkedList<>();
        score = new Score(gameView, this);
        redFuelBar = new RedFuelBar(gameView, this);
        jetFighter = new JetFighter(gameView, this, redFuelBar);
        fuelGage = new FuelGage(gameView, this);
        lifeCounter = new LifeCounter(gameView, this);
        statusBar = new StatusBar(gameView, this);
        sceneryLeft = new SceneryLeft(gameView, this);
        sceneryRight = new SceneryRight(gameView, this);
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
        pathDecisionObjects.add(sceneryLeft);
        pathDecisionObjects.add(sceneryRight);
        spawnGameObject(new Jimmy(gameView, this));
    }

    private void addActivatableGameObject(GameObject gameObject) {
        activatableGameObjects.add(gameObject);
        addToShiftableGameObjectsIfShiftable(gameObject);
    }

    protected void initializeLevel() {
        activatableGameObjects.clear();
        destroyAllGameObjects();
        clearListsForPathDecisionsInGameObjects();
        spawnGameObjects();
        spawnGameObjectsFromWorldString();
    }

    private void clearListsForPathDecisionsInGameObjects() {
        jetFighter.removePathDecisionObjects();
        pathDecisionObjects.clear();
    }

    private void spawnGameObjectsFromWorldString() {
        String[] lines = level.world.split("\\R");
        int factorForXCoordinate = 11;
        int factorForYCoordinate = 50;
        for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
            for (int columnIndex = 0; columnIndex < lines[lineIndex].length(); columnIndex++) {
                char character = lines[lineIndex].charAt(columnIndex);
                double x = (columnIndex - level.worldOffsetColumns) * factorForXCoordinate;
                double y = (lineIndex - level.worldOffsetLines) * factorForYCoordinate;
                if (character == 'B') {
                    Balloon balloon = new Balloon(gameView, this);
                    balloon.getPosition().updateCoordinates(x, y);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(balloon);
                    } else {
                        spawnGameObject(balloon);
                    }
                } else if (character == 'S') {
                    Ship ship = new Ship(gameView, this);
                    ship.getPosition().updateCoordinates(x, y);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(ship);
                    } else {
                        spawnGameObject(ship);
                    }
                } else if (character == 'H') {
                    Helicopter helicopter = new Helicopter(gameView, this);
                    helicopter.getPosition().updateCoordinates(x, y);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(helicopter);
                    } else {
                        spawnGameObject(helicopter);
                    }
                } else if (character == 'G') {
                    GreyJet greyJet = new GreyJet(gameView, this);
                    greyJet.getPosition().updateCoordinates(x, y);
                    greyJet.initializeTheSpawnPoint(columnIndex < 40);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(greyJet);
                    } else {
                        spawnGameObject(greyJet);
                    }
                } else if (character == 'F') {
                    FuelItem fuelItem = new FuelItem(gameView, this);
                    fuelItem.getPosition().updateCoordinates(x, y);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(fuelItem);
                    } else {
                        spawnGameObject(fuelItem);
                    }
                } else if (character == 'T') {
                    Tank tank = new Tank(gameView, this);
                    tank.getPosition().updateCoordinates(x, y);
                    tank.initializeTheSpawnPoint(columnIndex < 40);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(tank);
                    } else {
                        spawnGameObject(tank);
                    }
                } else if (character == 'X') {
                    Bridge bridge = new Bridge(gameView, this);
                    bridge.getPosition().updateCoordinates(x + 4, y - 12);
                    bridge.setCounterForLevel(level.number + 1);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(bridge);
                    } else {
                        spawnGameObject(bridge);
                    }
                } else if (character == 'L') {
                    BridgeLeft bridgeLeft = new BridgeLeft(gameView, this);
                    bridgeLeft.getPosition().updateCoordinates(x, y);
                    pathDecisionObjects.add(bridgeLeft);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(bridgeLeft);
                    } else {
                        spawnGameObject(bridgeLeft);
                    }
                } else if (character == 'R') {
                    BridgeRight bridgeRight = new BridgeRight(gameView, this);
                    bridgeRight.getPosition().updateCoordinates(x - 6, y);
                    pathDecisionObjects.add(bridgeRight);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(bridgeRight);
                    } else {
                        spawnGameObject(bridgeRight);
                    }
                } else if (character == 'Y') {
                    RadioTower radioTower = new RadioTower(gameView, this);
                    radioTower.getPosition().updateCoordinates(x, y);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(radioTower);
                    } else {
                        spawnGameObject(radioTower);
                    }
                } else if (character == 'Z') {
                    RocketLaunch rocketLaunch = new RocketLaunch(gameView, this);
                    rocketLaunch.getPosition().updateCoordinates(x, y);
                    rocketLaunch.initializeTheSpawnPoint(columnIndex < 40);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(rocketLaunch);
                    } else {
                        spawnGameObject(rocketLaunch);
                    }
                } else if (character == 'A') {
                    Satellite satellite = new Satellite(gameView, this);
                    satellite.getPosition().updateCoordinates(x, y);
                    satellite.initializeTheSpawnPoint(columnIndex < 40);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(satellite);
                    } else {
                        spawnGameObject(satellite);
                    }
                } else if (character == 'C') {
                    MovableSceneryLeft movableSceneryLeft = new MovableSceneryLeft(gameView, this);
                    movableSceneryLeft.getPosition().updateCoordinates(x - 700, y);
                    jetFighter.addPathDecisionObjects(movableSceneryLeft);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(movableSceneryLeft);
                    } else {
                        spawnGameObject(movableSceneryLeft);
                    }
                } else if (character == 'E') {
                    MovableSceneryRight movableSceneryRight = new MovableSceneryRight(gameView, this);
                    movableSceneryRight.getPosition().updateCoordinates(x, y);
                    jetFighter.addPathDecisionObjects(movableSceneryRight);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(movableSceneryRight);
                    } else {
                        spawnGameObject(movableSceneryRight);
                    }
                } else if (character == 'I') {
                    MovableSceneryFill movableSceneryFill = new MovableSceneryFill(gameView, this);
                    movableSceneryFill.getPosition().updateCoordinates(x - 400, y);
                    jetFighter.addPathDecisionObjects(movableSceneryFill);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(movableSceneryFill);
                    } else {
                        spawnGameObject(movableSceneryFill);
                    }
                } else if (character == 'W') {
                    SpecialBorderForTank specialBorderForTank = new SpecialBorderForTank(gameView, this);
                    specialBorderForTank.getPosition().updateCoordinates(x, y);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(specialBorderForTank);
                    } else {
                        spawnGameObject(specialBorderForTank);
                    }
                } else if (character == 'V') {
                    BigIsland bigIsland = new BigIsland(gameView, this);
                    bigIsland.getPosition().updateCoordinates(x, y);
                    jetFighter.addPathDecisionObjects(bigIsland);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(bigIsland);
                    } else {
                        spawnGameObject(bigIsland);
                    }
                } else if (character == 'U') {
                    SmallIsland smallIsland = new SmallIsland(gameView, this);
                    smallIsland.getPosition().updateCoordinates(x, y);
                    jetFighter.addPathDecisionObjects(smallIsland);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(smallIsland);
                    } else {
                        spawnGameObject(smallIsland);
                    }
                } else if (character == 'D') {
                    InitializeSpawnPoint initializeSpawnPoint = new InitializeSpawnPoint(gameView, this);
                    initializeSpawnPoint.getPosition().updateCoordinates(x, y);
                    jetFighter.setInitializeSpawnPoint(initializeSpawnPoint);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(initializeSpawnPoint);
                    } else {
                        spawnGameObject(initializeSpawnPoint);
                    }
                } else if (character == 't') {
                    Tree tree = new Tree(gameView, this);
                    tree.getPosition().updateCoordinates(x, y);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(tree);
                    } else {
                        spawnGameObject(tree);
                    }
                } else if (character == 'a') {
                    TestFillClassForBorder testFillClassForBorder = new TestFillClassForBorder(gameView, this);
                    testFillClassForBorder.getPosition().updateCoordinates(x - 730, y);
                    if (lineIndex < level.worldOffsetLines) {
                        addActivatableGameObject(testFillClassForBorder);
                    } else {
                        spawnGameObject(testFillClassForBorder);
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
            } else if (gameObject instanceof Tank tank) {
                if (tank.tryToActivate(jetFighter)) {
                    spawnGameObject(tank);
                    iterator.remove();
                }
            } else if (gameObject instanceof Bridge bridge) {
                if (bridge.tryToActivate(jetFighter)) {
                    bridge.setCounterForLevel(level.number + 1);
                    spawnGameObject(bridge);
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
            } else if (gameObject instanceof RadioTower radioTower) {
                if (radioTower.tryToActivate(jetFighter)) {
                    spawnGameObject(radioTower);
                    iterator.remove();
                }
            } else if (gameObject instanceof RocketLaunch rocketLaunch) {
                if (rocketLaunch.tryToActivate(jetFighter)) {
                    spawnGameObject(rocketLaunch);
                    iterator.remove();
                }
            } else if (gameObject instanceof Satellite satellite) {
                if (satellite.tryToActivate(jetFighter)) {
                    spawnGameObject(satellite);
                    iterator.remove();
                }
            } else if (gameObject instanceof MovableSceneryLeft movableSceneryLeft) {
                if (movableSceneryLeft.tryToActivate(jetFighter)) {
                    spawnGameObject(movableSceneryLeft);
                    iterator.remove();
                }
            } else if (gameObject instanceof MovableSceneryRight movableSceneryRight) {
                if (movableSceneryRight.tryToActivate(jetFighter)) {
                    spawnGameObject(movableSceneryRight);
                    iterator.remove();
                }
            } else if (gameObject instanceof MovableSceneryFill movableSceneryFill) {
                if (movableSceneryFill.tryToActivate(jetFighter)) {
                    spawnGameObject(movableSceneryFill);
                    iterator.remove();
                }
            } else if (gameObject instanceof SpecialBorderForTank specialBorderForTank) {
                if (specialBorderForTank.tryToActivate(jetFighter)) {
                    spawnGameObject(specialBorderForTank);
                    iterator.remove();
                }
            } else if (gameObject instanceof BigIsland bigIsland) {
                if (bigIsland.tryToActivate(jetFighter)) {
                    spawnGameObject(bigIsland);
                    iterator.remove();
                }
            } else if (gameObject instanceof InitializeSpawnPoint initializeSpawnPoint) {
                if (initializeSpawnPoint.tryToActivate(jetFighter)) {
                    spawnGameObject(initializeSpawnPoint);
                    iterator.remove();
                }
            } else if (gameObject instanceof Tree tree) {
                if (tree.tryToActivate(jetFighter)) {
                    spawnGameObject(tree);
                    iterator.remove();
                }
            } else if (gameObject instanceof TestFillClassForBorder testFillClassForBorder) {
                if (testFillClassForBorder.tryToActivate(jetFighter)) {
                    spawnGameObject(testFillClassForBorder);
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Returns a list of all game objects that are used as obstacles
     * for path decision logic.
     *
     * @return A list of {@link CollidingGameObject} that block horizontally movement.
     */
    List<CollidingGameObject> getPathDecisionObjects() {
        return pathDecisionObjects;
    }

    @Override
    protected void gameLoop() {
        super.gameLoop();
        activateGameObjects();
    }
}
