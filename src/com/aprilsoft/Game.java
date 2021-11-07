package com.aprilsoft;

public class Game {

    private int[][] masComp;
    private int[][] masPlayer;
    private boolean compTurn;
    private int endGame; // 0 - игра продолжается, 1 - игрок выиграл, 2 - компьютер выиграл

    public static final int cellCount = 10;

    public int getEndGame() {
        return endGame;
    }

    public boolean getCompTurn() {
        return compTurn;
    }

    public Game() {
        this.masComp = new int[cellCount][cellCount];
        this.masPlayer = new int[cellCount][cellCount];
    }


    public void start() {
        for (int i = 0; i < masComp.length; i++) {
            for (int k = 0; k < masPlayer.length; k++) {
                this.masComp[i][k] = 0;
                this.masPlayer[i][k] = 0;
            }
        }
        endGame = 0;
        compTurn = false;

        placeSheeps(masPlayer);
        placeSheeps(masComp);
    }

    private void placeSheeps(int[][] mas) {
        makeSheep(mas, 4);

        for (int i = 1; i <= 2; i++) {
            makeSheep(mas, 3);
        }

        for (int i = 1; i <= 3; i++) {
            makeSheep(mas, 2);
        }

        make1Deck(mas);
    }

    private void makeSheep(int[][] mas, int deckCount) {
        while (true) {
            boolean flag = false;

            int i = (int) (Math.random() * 10);
            int j = (int) (Math.random() * 10);

            int napr = (int) (Math.random() * 4);
            // 0 - up, 1 - right, 2 - down, 3 - left
            if (testNewPaluba(mas, i, j)) {
                switch (napr) {
                    case 0:
                        if (testNewPaluba(mas, i - (deckCount - 1), j)) {
                            flag = true;
                        }
                        break;
                    case 1:
                        if (testNewPaluba(mas, i, j + deckCount - 1)) {
                            flag = true;
                        }
                        break;
                    case 2:
                        if (testNewPaluba(mas, i + deckCount - 1, j)) {
                            flag = true;
                        }
                        break;
                    case 3:
                        if (testNewPaluba(mas, i, j - (deckCount - 1))) {
                            flag = true;
                        }
                        break;
                }

                if (flag) {
                    mas[i][j] = deckCount;
                    okrBegin(mas, i, j, -2);

                    switch (napr) {
                        case 0: // up
                            for (int k = deckCount - 1; k >= 1; k--) {
                                mas[i - k][j] = deckCount;
                                okrBegin(mas, i - k, j, -2);
                            }
                            break;
                        case 1: // right
                            for (int k = deckCount - 1; k >= 1; k--) {
                                mas[i][j + k] = deckCount;
                                okrBegin(mas, i, j + k, -2);
                            }
                            break;
                        case 2: // down
                            for (int k = deckCount - 1; k >= 1; k--) {
                                mas[i + k][j] = deckCount;
                                okrBegin(mas, i + k, j, -2);
                            }
                            break;
                        case 3: // left
                            for (int k = deckCount - 1; k >= 1; k--) {
                                mas[i][j - k] = deckCount;
                                okrBegin(mas, i, j - k, -2);
                            }
                            break;
                    }

                    break;
                }
            }
        }

        okrEnd(mas);
    }

    private void make1Deck(int[][] mas) {
        for (int k = 1; k <= 4; k++) {
            while (true) {
                int i = (int) (Math.random() * 10);
                int j = (int) (Math.random() * 10);

                if (mas[i][j] == 0) {
                    mas[i][j] = 1;
                    okrBegin(mas, i, j, -1);
                    break;
                }
            }
        }
    }

    private void okrBegin(int[][] mas, int i, int j, int val) {
        setOkr(mas, i - 1, j - 1, val); // top left
        setOkr(mas, i - 1, j, val); // top
        setOkr(mas, i - 1, j + 1, val); // top right
        setOkr(mas, i, j + 1, val); // right
        setOkr(mas, i + 1, j + 1, val); // down right
        setOkr(mas, i + 1, j, val); // down
        setOkr(mas, i + 1, j - 1, val); // down left
        setOkr(mas, i + 1, j - 1, val); // down left
        setOkr(mas, i, j - 1, val); // left
    }

    private void setOkr(int[][] mas, int i, int p, int val) { // устанавливает  val значение для клеточки с координатой i, p
        if (testMasPoz(i, p) && mas[i][p] == 0) {
            setMasValue(mas, i, p, val);
        }
    }

    private void setMasValue(int[][] mas, int i, int p, int val) {
        if (testMasPoz(i, p)) {
            mas[i][p] = val;
        }
    }

    private void okrEnd(int[][] mas) {
        for (int i = 0; i < cellCount; i++) {
            for (int k = 0 ; k < cellCount; k++) {
                if (mas[i][k] == -2) {
                    mas[i][k] = -1;
                }
            }
        }
    }

    private  void okrPodbit(int[][] mas, int i, int j) {
        for (int k = i - 1; k < i + 2; k++) {
            for (int s = j - 1; s < j + 2; s++) {
                if (!(k == i && s == j)) {
                    setOkrPodbit(mas, k, s);
                }
            }
        }
    }

    private void setOkrPodbit(int[][] mas, int k, int s) {
        if (testMasPoz(k, s)) {
            if (mas[k][s] == -1 || mas[k][s] == 6) {
                mas[k][s]--;
            }
        }
    }

    private boolean testNewPaluba(int[][] mas, int i, int j) {
        if (!testMasPoz(i, j)) { // за пределами 0 - 9
            return false;
        }
        if (mas[i][j] == 0 || mas[i][j] == -2) {
            return true;
        }
        return false;
    }

    private void make4Deck(int[][] mas) {
        int i = (int) (Math.random() * 10);
        int j = (int) (Math.random() * 10);

        mas[i][j] = 4;
        okrBegin(mas, i, j, -2);

        int napr = (int) (Math.random() * 4); // ( 0 - 3 )
        // 0 - up, 1 - right, 2 - down, 3 - left
        switch (napr) {
            case 0:
                if (!testNewPaluba(mas, i - 3, j)) {
                    napr = 2;
                }
                break;
            case 1:
                if (!testNewPaluba(mas, i, j + 3)) {
                    napr = 3;
                }
                break;
            case 2:
                if (!testNewPaluba(mas, i + 3, j)) {
                    napr = 0;
                }
                break;
            case 3:
                if (!testNewPaluba(mas, i, j - 3)) {
                    napr = 1;
                }
                break;
        }

        switch (napr) {
            case 0: // up
                mas[i - 3][j] = 4;
                okrBegin(mas, i - 3, j, -2);
                mas[i - 2][j] = 4;
                //okrBegin(mas, i - 2, j, -2);
                mas[i - 1][j] = 4;
                //okrBegin(mas, i - 1, j, -2);
                break;
            case 1: // right
                mas[i][j + 3] = 4;
                okrBegin(mas, i, j + 3, -2);
                mas[i][j + 2] = 4;
                // okrBegin(mas, i, j + 2, -2);
                mas[i][j + 1] = 4;
                // okrBegin(mas, i, j + 1, -2);
                break;
            case 2: // down
                mas[i + 3][j] = 4;
                okrBegin(mas, i + 3, j, -2);
                mas[i + 2][j] = 4;
                // okrBegin(mas, i + 2, j, -2);
                mas[i + 1][j] = 4;
                // okrBegin(mas, i + 1, j, -2);
                break;
            case 3: // left
                mas[i][j - 3] = 4;
                okrBegin(mas, i, j - 3, -2);
                mas[i][j - 2] = 4;
                // okrBegin(mas, i, j - 2, -2);
                mas[i][j - 1] = 4;
                // okrBegin(mas, i, j - 1, -2);
                break;
        }

        okrEnd(mas);
    }

    private boolean testMasPoz(int i, int p) {
        if (0 <= i && i < cellCount && 0 <= p && p < cellCount) {
            return true;
        }
        return false;
    }

    public int getPlayerFieldValueAt(int x, int y) {
        return this.masPlayer[y][x];
    }

    public int getCompFieldValueAt(int x, int y) {
        return this.masComp[y][x];
    }

    public void shootPlayer(int j, int i) {
        masComp[i][j] += 7;
        testKilled(masComp, i, j);
        testEndGame();
        if (masComp[i][j] < 8) {
            compTurn = true;
            while (compTurn) {
                compTurn = shootComp();
            }
        }
    }

    private boolean takeStrike(int[][] masPlayer, int i, int j) {
        masPlayer[i][j] += 7;
        testKilled(masPlayer, i, j);

        if (masPlayer[i][j] >= 8) {
            return true;
        }
        return false;
    }

    public boolean shootComp() {
        boolean
                hitSign = false,
                woundedExists = false,
                isShoot = false;

        for (int i = 0; i < cellCount; i++) {
            for (int j = 0; j < cellCount; j++) {
                //поиск раненого корабля
                if (masPlayer[i][j] >= 9 && masPlayer[i][j] <= 11) {
                    woundedExists = true;

                    for (int k = i - 1; k < i + 2; k++) {
                        for (int s = j - 1; s < j + 2; s++) {
                            if (testMasPoz(k, s) && masPlayer[k][s] <= 4 && masPlayer[k][s] != -2) {
                                hitSign = takeStrike(masPlayer, k, s);
                                isShoot = true;

                                k = i + 10000;
                                s = j + 10000;
                            }
                        }
                    }

                    if (isShoot) {
                        i = cellCount + 1;
                        j = cellCount + 1;
                    }
                }
            }
        }

        if (!woundedExists) { // если нет раненого - то просто делаем выстрел
            for (int k = 1; k <= 50; k++) {
                int i = (int) (Math.random() * cellCount); // 0-9
                int j = (int) (Math.random() * cellCount); // 0-9

                if (masPlayer[i][j] <= 4 && masPlayer[i][j] != -2) {
                    hitSign = takeStrike(masPlayer, i, j);
                    isShoot = true;
                    break;
                }
            }

            if (!isShoot) {
                for (int i = 0; i < cellCount; i++) {
                    for (int j = 0; j < cellCount; j++) {
                        if (masPlayer[i][j] <= 4 && masPlayer[i][j] != -2) {
                            hitSign = takeStrike(masPlayer, i, j);
                            break;
                        }
                    }
                }
            }
        }
        testEndGame();

        return hitSign;
    }

    private void testKilled(int[][] mas, int i, int j) {
        switch (mas[i][j]) {
            case 8:
                mas[i][j] += 7;
                okrPodbit(mas, i, j);
                break;
            case 9:
                analyzeKilled(mas, i, j, 2);
                break;
            case 10:
                analyzeKilled(mas, i, j, 3);
                break;
            case 11:
                analyzeKilled(mas, i, j, 4);
                break;
        }
    }

    private void analyzeKilled(int[][] mas, int i, int j, int deckCount) {
        int woundedDeckAmount = 0;
        for (int k = i - (deckCount - 1); k <= i + (deckCount - 1); k++) {
            for (int g = j - (deckCount - 1); g <= j + (deckCount - 1); g++) {
                if (testMasPoz(k, g) && mas[k][g] == deckCount + 7) {
                    woundedDeckAmount++;
                }
            }
        }
        if (deckCount == woundedDeckAmount) {
            for (int k = i - (deckCount - 1); k <= i + (deckCount - 1); k++) {
                for (int g = j - (deckCount - 1); g <= j + (deckCount - 1); g++) {
                    if (testMasPoz(k, g) && mas[k][g] == deckCount + 7) {
                        mas[k][g] += 7;
                        okrPodbit(mas, k, g);
                    }
                }
            }
        }
    }

    private void testEndGame() {
        int testNumber = 15 * 1 * 4 + 16 * 2 * 3 + 17 * 3 * 2 + 18 * 4 * 1; // == 330
        int kolComp = 0, kolPlayer = 0;

        for (int i = 0; i < cellCount; i++) {
            for (int j = 0; j < cellCount; j++) {
                if (masPlayer[i][j] >= 15) {
                    kolPlayer += masPlayer[i][j];
                }
                if (masComp[i][j] >= 15) {
                    kolComp += masComp[i][j];
                }
            }
        }

        if (kolPlayer == testNumber) {
            endGame = 2;
        } else if (kolComp == testNumber) {
            endGame = 1;
        }
    }
}
