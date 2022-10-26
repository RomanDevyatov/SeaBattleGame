package com.aprilsoft;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JButton;
import java.awt.Image;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import static com.aprilsoft.GameParameters.START_X_COMPUTER_FIELD;
import static com.aprilsoft.GameParameters.START_Y_COMPUTER_FIELD;
import static com.aprilsoft.GameParameters.CELL_WIDTH;
import static com.aprilsoft.GameParameters.CELL_HEIGHT;
import static com.aprilsoft.GameParameters.BACKGROUND_WIDTH;
import static com.aprilsoft.GameParameters.BACKGROUND_HEIGHT;
import static com.aprilsoft.GameParameters.START_X_PLAYER_FIELD;
import static com.aprilsoft.GameParameters.START_Y_PLAYER_FIELD;
import static com.aprilsoft.GameParameters.END_X_COMPUTER_FIELD;
import static com.aprilsoft.GameParameters.END_Y_COMPUTER_FIELD;
import static com.aprilsoft.GameParameters.END_X_PLAYER_FIELD;
import static com.aprilsoft.GameParameters.END_Y_PLAYER_FIELD;
import static com.aprilsoft.GameParameters.NUMBER_HEIGHT;
import static com.aprilsoft.GameParameters.NUMBER_WIDTH;
import static com.aprilsoft.GameParameters.BUTTON_WIDTH;
import static com.aprilsoft.GameParameters.BUTTON_HEIGHT;
import static com.aprilsoft.GameParameters.X_START_COMPUTER_CHARS_LINE;
import static com.aprilsoft.GameParameters.Y_PLAYER_CHARS_LINE;
import static com.aprilsoft.GameParameters.X_START_PLAYER_CHARS_LINE;
import static com.aprilsoft.GameParameters.Y_COMPUTER_CHARS_LINE;
import static com.aprilsoft.GameParameters.X_START_WON_IMAGE;
import static com.aprilsoft.GameParameters.Y_START_WON_IMAGE;
import static com.aprilsoft.GameParameters.WIDTH_WON_IMAGE;
import static com.aprilsoft.GameParameters.HEIGHT_WON_IMAGE;


public class MyPanel extends JPanel {
    public Game game;
    private Image backgroundImage;
    private Image deckImage;
    private Image killedImage;
    private Image woundedImage;
    private Image playerWonImage;
    private Image computerWonImage;
    private Image bombImage;
    private int xMouse, yMouse;

    public class MouseListenerImpl implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == 1 && e.getClickCount() == 1) {
                xMouse = e.getX();
                yMouse = e.getY();
                if (xMouse >= START_X_COMPUTER_FIELD && yMouse >= START_Y_COMPUTER_FIELD && xMouse < END_X_COMPUTER_FIELD && yMouse < END_Y_COMPUTER_FIELD) {
                    if (game.getEndGame() == 0 && !game.getComputerTurn()) {
                        int y = (yMouse - 100) / 30;
                        int x = (xMouse - 100) / 30;

                        if (game.getCompFieldValueAt(y, x) >= -1 && game.getCompFieldValueAt(y, x) <= 4) {
                            game.shootComputerField(y, x);
                        }
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }

    public class MouseMotionListenerImpl implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {
            xMouse = e.getX();
            yMouse = e.getY();

            if (xMouse >= START_X_COMPUTER_FIELD && yMouse >= START_Y_COMPUTER_FIELD && xMouse < END_X_COMPUTER_FIELD && yMouse < END_Y_COMPUTER_FIELD) {
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            } else {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    public MyPanel() {
        addMouseListener(new MouseListenerImpl());
        addMouseMotionListener(new MouseMotionListenerImpl());

        setFocusable(true);

        this.game = new Game();
        this.game.start();

        try {
            this.backgroundImage = ImageIO.read(new File(".\\img\\fon.jpg"));
            this.deckImage = ImageIO.read(new File(".\\img\\paluba.png"));
            this.killedImage = ImageIO.read(new File(".\\img\\killed.png"));
            this.woundedImage = ImageIO.read(new File(".\\img\\wounded.png"));
            this.playerWonImage = ImageIO.read(new File(".\\img\\player_won.png"));
            this.computerWonImage = ImageIO.read(new File(".\\img\\computer_won.png"));
            this.bombImage = ImageIO.read(new File(".\\img\\bomb.png"));
        } catch (IOException e) {
            System.out.println("Image cannot be read: " + e.getMessage());
        }

        Timer drawTimer = new Timer(50, e -> repaint());
        drawTimer.start();

        JButton newGameBtn = new JButton();
        newGameBtn.setText("New game");
        newGameBtn.setForeground(Color.BLUE);
        newGameBtn.setFont(new Font("serif", 0, 20));
        newGameBtn.setBounds(130, 450, BUTTON_WIDTH, BUTTON_HEIGHT);
        newGameBtn.addActionListener(e -> game.start());
        add(newGameBtn);

        JButton endGameBtn = new JButton();
        endGameBtn.setText("Exit");
        endGameBtn.setForeground(Color.BLUE);
        endGameBtn.setFont(new Font("serif", 0, 20));
        endGameBtn.setBounds(530, 450, BUTTON_WIDTH, BUTTON_HEIGHT);
        endGameBtn.addActionListener(e -> System.exit(0));
        add(endGameBtn);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        graphics.drawImage(backgroundImage, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, null);
        graphics.setFont(new Font("serif", Font.PLAIN, 40));

        graphics.setColor(Color.GRAY);
        graphics.drawString("Computer", 150, 50);
        graphics.drawString("Player", 590, 50);

        drawDeckImages(graphics);

        drawFieldImages(graphics, game.getComputerField(), START_X_COMPUTER_FIELD, START_Y_COMPUTER_FIELD);
        drawFieldImages(graphics, game.getPlayerField(), START_X_PLAYER_FIELD, START_Y_PLAYER_FIELD);

        graphics.setColor(Color.RED);
        drawFilledCursor(graphics);

        graphics.setColor(Color.BLUE);
        drawField(graphics, START_X_COMPUTER_FIELD, START_Y_COMPUTER_FIELD, END_X_COMPUTER_FIELD, END_Y_COMPUTER_FIELD);
        drawField(graphics, START_X_PLAYER_FIELD, START_Y_PLAYER_FIELD, END_X_PLAYER_FIELD, END_Y_PLAYER_FIELD);

        graphics.setFont(new Font("serif", 0, 20));
        graphics.setColor(Color.RED);

        drawNumberColumnAndCharsLine(graphics, NUMBER_WIDTH, X_START_COMPUTER_CHARS_LINE, Y_COMPUTER_CHARS_LINE);
        drawNumberColumnAndCharsLine(graphics, END_X_COMPUTER_FIELD + NUMBER_WIDTH, X_START_PLAYER_CHARS_LINE, Y_PLAYER_CHARS_LINE);

        checkIfSomeoneWon(graphics);
    }

    private void drawFieldImages(Graphics graphics, int[][] field, int startXCoordinate, int startYCoordinate) {
        for (int y = 0; y < Game.CELL_COUNT; y++) {
            for (int x = 0; x < Game.CELL_COUNT; x++) {
                int fieldValue = game.getFieldValueAt(field, y, x);
                if (fieldValue != 0) {
                    if (fieldValue >= 8 && fieldValue <= 11) {
                        graphics.drawImage(woundedImage, startXCoordinate + x * CELL_WIDTH, startYCoordinate + y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, null);
                    } else if (fieldValue >= 15) {
                        graphics.drawImage(killedImage, startXCoordinate + x * CELL_WIDTH, startYCoordinate + y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, null);
                    } else if (fieldValue >= 5 || fieldValue == -2) {
                        graphics.drawImage(bombImage, startXCoordinate + x * CELL_WIDTH, startYCoordinate + y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, null);
                    }
                }
            }
        }
    }

    private void drawFilledCursor(Graphics graphics) {
        if (xMouse > START_X_COMPUTER_FIELD && yMouse > START_Y_COMPUTER_FIELD && xMouse < END_X_COMPUTER_FIELD && yMouse < END_Y_COMPUTER_FIELD) {
            if (game.getEndGame() == 0 && !game.getComputerTurn()) {
                int y = (yMouse - START_Y_COMPUTER_FIELD) / CELL_HEIGHT;
                int x = (xMouse - START_X_COMPUTER_FIELD) / CELL_WIDTH;

                graphics.fillRect(START_X_COMPUTER_FIELD + CELL_WIDTH * x, START_Y_COMPUTER_FIELD + CELL_HEIGHT * y, CELL_WIDTH, CELL_HEIGHT);
            }
        }
    }

    private void drawField(Graphics graphics, final int startXCoordinate, final int startYCoordinate, final int endXCoordinate, final int endYCoordinate) {
        for (int cellIndex = 0; cellIndex <= Game.CELL_COUNT; cellIndex++) {
            graphics.drawLine(startXCoordinate + cellIndex * CELL_WIDTH, startYCoordinate, startXCoordinate + cellIndex * CELL_WIDTH, endYCoordinate);
            graphics.drawLine(startXCoordinate, startYCoordinate + cellIndex * CELL_HEIGHT, endXCoordinate, startYCoordinate + cellIndex * CELL_HEIGHT);
        }
    }

    private void checkIfSomeoneWon(Graphics graphics) {
        if (game.getEndGame() == Game.PLAYER_WON_STATUS) {
            graphics.drawImage(playerWonImage, X_START_WON_IMAGE, Y_START_WON_IMAGE, WIDTH_WON_IMAGE, HEIGHT_WON_IMAGE, null);
        } else if (game.getEndGame() == Game.COMPUTER_WON_STATUS) {
            graphics.drawImage(computerWonImage, X_START_WON_IMAGE, Y_START_WON_IMAGE, WIDTH_WON_IMAGE, HEIGHT_WON_IMAGE, null);
        }
    }

    private void drawNumberColumnAndCharsLine(Graphics graphics, int xNumbersColumn, int xStartCharsLine, int yCharsLine) {
        for (int cellIndex = 0; cellIndex < Game.CELL_COUNT; cellIndex++) {
            graphics.drawString(String.valueOf(cellIndex + 1), xNumbersColumn, NUMBER_HEIGHT + (cellIndex + 1) * CELL_HEIGHT);
            graphics.drawString(String.valueOf((char) ('A' + cellIndex)), xStartCharsLine + (cellIndex + 1) * 30, yCharsLine);
        }
    }

    private void drawDeckImages(Graphics graphics) {
        for (int y = 0; y < Game.CELL_COUNT; y++) {
            for (int x = 0; x < Game.CELL_COUNT; x++) {
                if (game.getPlayerFieldValueAt(y, x) >= 1 && game.getPlayerFieldValueAt(y, x) <= 4) {
                    graphics.drawImage(deckImage, START_X_PLAYER_FIELD + x * CELL_WIDTH, START_Y_PLAYER_FIELD + y * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, null);
                }
            }
        }
    }
}
