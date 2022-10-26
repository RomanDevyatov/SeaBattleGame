package com.aprilsoft;

public class Game {

    private final int[][] computerField;
    private final int[][] playerField;
    private boolean computerTurn;
    private int endGame;

    public static final int CELL_COUNT = 10;
    public static final byte GAME_CONTINUES_STATUS = 0;
    public static final byte PLAYER_WON_STATUS = 1;
    public static final byte COMPUTER_WON_STATUS = 2;
    public static final byte UP_WAY = 0;
    public static final byte RIGHT_WAY = 1;
    public static final byte DOWN_WAY = 2;
    public static final byte LEFT_WAY = 3;

    public static final byte SHOT_VALUE = 7;

    public Game() {
        this.computerField = new int[CELL_COUNT][CELL_COUNT];
        this.playerField = new int[CELL_COUNT][CELL_COUNT];
    }

    public int[][] getComputerField() {
        return this.computerField;
    }

    public int[][] getPlayerField() {
        return this.playerField;
    }

    public int getEndGame() {
        return endGame;
    }

    public boolean getComputerTurn() {
        return computerTurn;
    }

    public void start() {
        for (int yCell = 0; yCell < computerField.length; yCell++) {
            for (int xCell = 0; xCell < playerField.length; xCell++) {
                this.computerField[yCell][xCell] = 0;
                this.playerField[yCell][xCell] = 0;
            }
        }
        endGame = GAME_CONTINUES_STATUS;
        computerTurn = false;

        placeSheeps(playerField);
        placeSheeps(computerField);
    }

    private void placeSheeps(int[][] field) {
        makeSheep(field, 4);

        for (int i = 1; i <= 2; i++) {
            makeSheep(field, 3);
        }

        for (int i = 1; i <= 3; i++) {
            makeSheep(field, 2);
        }

        make1Deck(field);
    }

    private void makeSheep(int[][] field, int deckCount) {
        while (true) {
            boolean isEnoughPlace = false;

            int randY = (int) (Math.random() * 10);
            int randX = (int) (Math.random() * 10);

            int direction = (int) (Math.random() * 4);
            if (field[randY][randX] == 0) {
                switch (direction) {
                    case UP_WAY:
                        if (isNewDeck(field, randY - (deckCount - 1), randX)) {
                            isEnoughPlace = true;
                        }
                        break;
                    case RIGHT_WAY:
                        if (isNewDeck(field, randY, randX + deckCount - 1)) {
                            isEnoughPlace = true;
                        }
                        break;
                    case DOWN_WAY:
                        if (isNewDeck(field, randY + deckCount - 1, randX)) {
                            isEnoughPlace = true;
                        }
                        break;
                    case LEFT_WAY:
                        if (isNewDeck(field, randY, randX - (deckCount - 1))) {
                            isEnoughPlace = true;
                        }
                        break;
                }

                if (isEnoughPlace) {
                    field[randY][randX] = deckCount;
                    setValueAroundIfZero(field, randY, randX, -2);

                    switch (direction) {
                        case UP_WAY:
                            for (int deckIndex = deckCount - 1; deckIndex > 0; deckIndex--) {
                                field[randY - deckIndex][randX] = deckCount;
                                setValueAroundIfZero(field, randY - deckIndex, randX, -2);
                            }
                            break;
                        case RIGHT_WAY:
                            for (int deckIndex = deckCount - 1; deckIndex > 0; deckIndex--) {
                                field[randY][randX + deckIndex] = deckCount;
                                setValueAroundIfZero(field, randY, randX + deckIndex, -2);
                            }
                            break;
                        case DOWN_WAY:
                            for (int deckIndex = deckCount - 1; deckIndex > 0; deckIndex--) {
                                field[randY + deckIndex][randX] = deckCount;
                                setValueAroundIfZero(field, randY + deckIndex, randX, -2);
                            }
                            break;
                        case LEFT_WAY:
                            for (int deckIndex = deckCount - 1; deckIndex > 0; deckIndex--) {
                                field[randY][randX - deckIndex] = deckCount;
                                setValueAroundIfZero(field, randY, randX - deckIndex, -2);
                            }
                            break;
                    }

                    break;
                }
            }
        }

        setMinus1IfMinus2(field);
    }

    private void make1Deck(int[][] field) {
        for (int k = 1; k <= 4; k++) {
            while (true) {
                int y = (int) (Math.random() * 10);
                int x = (int) (Math.random() * 10);

                if (field[y][x] == 0) {
                    field[y][x] = 1;
                    setValueAroundIfZero(field, y, x, -1);
                    break;
                }
            }
        }
    }

    private void setValueAroundIfZero(int[][] field, int y, int x, int val) {
        setValueIfZero(field, y - 1, x - 1, val);
        setValueIfZero(field, y - 1, x, val);
        setValueIfZero(field, y - 1, x + 1, val);
        setValueIfZero(field, y, x + 1, val);
        setValueIfZero(field, y + 1, x + 1, val);
        setValueIfZero(field, y + 1, x, val);
        setValueIfZero(field, y + 1, x - 1, val);
        setValueIfZero(field, y + 1, x - 1, val);
        setValueIfZero(field, y, x - 1, val);
    }

    private void setValueIfZero(int[][] field, int y, int x, int val) {
        if (isField(y, x) && field[y][x] == 0) {
            field[y][x] = val;
        }
    }

    private void setMinus1IfMinus2(int[][] field) {
        for (int i = 0; i < CELL_COUNT; i++) {
            for (int k = 0; k < CELL_COUNT; k++) {
                if (field[i][k] == -2) {
                    field[i][k] = -1;
                }
            }
        }
    }

    private void setMinusOneIfWoundedOrMissed(int[][] field, int y, int x) {
        for (int k = y - 1; k < y + 2; k++) {
            for (int s = x - 1; s < x + 2; s++) {
                if (!(k == y && s == x) && isField(k, s) && (field[k][s] == -1 || field[k][s] == 6)) {
                    field[k][s]--;
                }
            }
        }
    }


    private boolean isNewDeck(int[][] field, int y, int x) {
        return isField(y, x) && (field[y][x] == 0 || field[y][x] == -2);
    }

    private void make4Deck(int[][] field) {
        int i = (int) (Math.random() * 10);
        int j = (int) (Math.random() * 10);

        field[i][j] = 4;
        setValueAroundIfZero(field, i, j, -2);

        int napr = (int) (Math.random() * 4); // ( 0 - 3 )
        // 0 - up, 1 - right, 2 - down, 3 - left
        switch (napr) {
            case 0:
                if (!isNewDeck(field, i - 3, j)) {
                    napr = 2;
                }
                break;
            case 1:
                if (!isNewDeck(field, i, j + 3)) {
                    napr = 3;
                }
                break;
            case 2:
                if (!isNewDeck(field, i + 3, j)) {
                    napr = 0;
                }
                break;
            case 3:
                if (!isNewDeck(field, i, j - 3)) {
                    napr = 1;
                }
                break;
        }

        switch (napr) {
            case 0: // up
                field[i - 3][j] = 4;
                setValueAroundIfZero(field, i - 3, j, -2);
                field[i - 2][j] = 4;
                //okrBegin(mas, i - 2, j, -2);
                field[i - 1][j] = 4;
                //okrBegin(mas, i - 1, j, -2);
                break;
            case 1: // right
                field[i][j + 3] = 4;
                setValueAroundIfZero(field, i, j + 3, -2);
                field[i][j + 2] = 4;
                // okrBegin(mas, i, j + 2, -2);
                field[i][j + 1] = 4;
                // okrBegin(mas, i, j + 1, -2);
                break;
            case 2: // down
                field[i + 3][j] = 4;
                setValueAroundIfZero(field, i + 3, j, -2);
                field[i + 2][j] = 4;
                // okrBegin(mas, i + 2, j, -2);
                field[i + 1][j] = 4;
                // okrBegin(mas, i + 1, j, -2);
                break;
            case 3: // left
                field[i][j - 3] = 4;
                setValueAroundIfZero(field, i, j - 3, -2);
                field[i][j - 2] = 4;
                // okrBegin(mas, i, j - 2, -2);
                field[i][j - 1] = 4;
                // okrBegin(mas, i, j - 1, -2);
                break;
        }

        setMinus1IfMinus2(field);
    }

    private boolean isField(int y, int x) {
        return 0 <= y && y < CELL_COUNT && 0 <= x && x < CELL_COUNT;
    }
    public int getPlayerFieldValueAt(int y, int x) {
        return this.playerField[y][x];
    }

    public int getCompFieldValueAt(int y, int x) {
        return this.computerField[y][x];
    }

    public int getFieldValueAt(int[][] field, int y, int x) {
        return field[y][x];
    }

    public void shootComputerField(int y, int x) {
        computerField[y][x] += SHOT_VALUE;
        checkIfKilled(computerField, y, x);
        checkEndGame();
        if (computerField[y][x] < 8) {
            computerTurn = true;
            while (computerTurn) {
                computerTurn = shootComp();
            }
        }
    }

    private boolean shootPlayerField(int[][] playerField, int y, int x) {
        playerField[y][x] += SHOT_VALUE;
        checkIfKilled(playerField, y, x);

        return playerField[y][x] >= 8;
    }

    private boolean isWounded(int y, int x) {
        return playerField[y][x] >= 9 && playerField[y][x] <= 11;
    }

    public boolean shootComp() {
        boolean isPlayerWounded = false;
        boolean woundedExists = false;
        boolean isShot = false;

        for (int y = 0; y < CELL_COUNT; y++) {
            for (int x = 0; x < CELL_COUNT; x++) {
                woundedExists = isWounded(y, x);
                if (woundedExists) {
                    for (int k = y - 1; k < y + 2; k++) {
                        for (int s = x - 1; s < x + 2; s++) {
                            if (isField(k, s) && playerField[k][s] <= 4 && playerField[k][s] != -2) {
                                isPlayerWounded = shootPlayerField(playerField, k, s);
                                isShot = true;

                                k = y + 666;
                                s = x + 666;
                            }
                        }
                    }

                    if (isShot) {
                        y = CELL_COUNT + 1;
                        x = CELL_COUNT + 1;
                    }
                }
            }
        }

        if (!woundedExists) { // random shoot
            for (int k = 1; k <= 50; k++) {
                int yRandom = (int) (Math.random() * CELL_COUNT);
                int xRandom = (int) (Math.random() * CELL_COUNT);

                if (playerField[yRandom][xRandom] <= 4 && playerField[yRandom][xRandom] != -2) {
                    isPlayerWounded = shootPlayerField(playerField, yRandom, xRandom);
                    isShot = true;
                    break;
                }
            }

            if (!isShot) {
                for (int i = 0; i < CELL_COUNT; i++) {
                    for (int j = 0; j < CELL_COUNT; j++) {
                        if (playerField[i][j] <= 4 && playerField[i][j] != -2) {
                            isPlayerWounded = shootPlayerField(playerField, i, j);
                            break;
                        }
                    }
                }
            }
        }
        checkEndGame();

        return isPlayerWounded;
    }

    private void checkIfKilled(int[][] field, int y, int x) {
        switch (field[y][x]) {
            case 8:
                field[y][x] += SHOT_VALUE;
                setMinusOneIfWoundedOrMissed(field, y, x);
                break;
            case 9:
                analyzeKilled(field, y, x, 2);
                break;
            case 10:
                analyzeKilled(field, y, x, 3);
                break;
            case 11:
                analyzeKilled(field, y, x, 4);
                break;
        }
    }

    private void analyzeKilled(int[][] field, int y, int x, int deckCount) {
        int woundedDeckAmount = 0;
        for (int k = y - (deckCount - 1); k <= y + (deckCount - 1); k++) {
            for (int g = x - (deckCount - 1); g <= x + (deckCount - 1); g++) {
                if (isField(k, g) && field[k][g] == deckCount + SHOT_VALUE) {
                    woundedDeckAmount++;
                }
            }
        }
        if (deckCount == woundedDeckAmount) {
            for (int k = y - (deckCount - 1); k <= y + (deckCount - 1); k++) {
                for (int g = x - (deckCount - 1); g <= x + (deckCount - 1); g++) {
                    if (isField(k, g) && field[k][g] == deckCount + SHOT_VALUE) {
                        field[k][g] += SHOT_VALUE;
                        setMinusOneIfWoundedOrMissed(field, k, g);
                    }
                }
            }
        }
    }

    private void checkEndGame() {
        int maxScore = 15 * 1 * 4 + 16 * 2 * 3 + 17 * 3 * 2 + 18 * 4 * 1; // == 330
        int computerScore = 0, playerScore = 0;

        for (int y = 0; y < CELL_COUNT; y++) {
            for (int x = 0; x < CELL_COUNT; x++) {
                if (playerField[y][x] >= 15) {
                    playerScore += playerField[y][x];
                }
                if (computerField[y][x] >= 15) {
                    computerScore += computerField[y][x];
                }
            }
        }

        if (playerScore == maxScore) {
            endGame = COMPUTER_WON_STATUS;
        } else if (computerScore == maxScore) {
            endGame = PLAYER_WON_STATUS;
        }
    }

}
