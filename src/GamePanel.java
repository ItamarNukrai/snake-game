import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 1200;
    static final int SCREEN_HEIGHT = 1200;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 200;
    Direction direction = Direction.RIGHT;
    boolean running = true;
    Timer timer;
    int appleX;
    int appleY;
    int bodyParts = 1;

    Tile[] tiles = new Tile[GAME_UNITS];

    public class Tile {

        public int tileX;
        public int tileY;

        public Tile(int tileX, int tileY) {
            this.tileX = tileX;
            this.tileY = tileY;
        }

        public int getTileY() {
            return tileY;
        }

        public void setTileY(int tileY) {
            this.tileY = tileY;
        }

        public int getTileX() {
            return tileX;
        }

        public void setTileX(int tileX) {
            this.tileX = tileX;
        }
    }


    public GamePanel() {
        startGame();
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new KeyAdapter() { //מבקש פרמטר אינטרפס מסוג KeyListener

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {

                    case KeyEvent.VK_LEFT:
                        if (direction != Direction.RIGHT) {
                            direction = Direction.LEFT;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != Direction.LEFT) {
                            direction = Direction.RIGHT;
                        }
                        break;

                    case KeyEvent.VK_UP:
                        if (direction != Direction.DOWN) {
                            direction = Direction.UP;
                        }
                        break;

                    case KeyEvent.VK_DOWN:
                        if (direction != Direction.UP) {
                            direction = Direction.DOWN;
                        }
                        break;
                }
            }

        });
    }

    public void startGame() {
        generateAppleCoordinates();

      //  int startX = (int) (Math.random() * ((SCREEN_WIDTH / UNIT_SIZE - 1) + 1));
      //  int startY = (int) (Math.random() * ((SCREEN_HEIGHT / UNIT_SIZE - 1) + 1));
        tiles[0] = new Tile(5, 5);
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void drawGrid(Graphics g) {
        for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++)         // קווים של ציר הY לגובה
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);

        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++)          //קווים של ציר הX לרוחב
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);

    }

    public void generateAppleCoordinates() {
        // פעולה שמדפיסה על המסך את התפוח
        int maxUnitX = SCREEN_WIDTH / UNIT_SIZE - 1; // 23
        int maxUnitY = SCREEN_HEIGHT / UNIT_SIZE - 1; // 23

        appleX = (int) (Math.random() * (maxUnitX + 1));
        appleY = (int) (Math.random() * (maxUnitY + 1));        // מספר בין 0 - 23

    }

    public void paintApple(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(appleX * UNIT_SIZE, appleY * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {
           // drawGrid(g);
            // generateAppleCoordinates(); // זאת הפעולה שהעבריה את התפוח ממקום למקום מהר
            paintApple(g);

            // moveSnake();

            drawSnake(g);

            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + (bodyParts-1), (SCREEN_WIDTH - metrics.stringWidth("Score: " + bodyParts)) / 2, g.getFont().getSize());

        } else gameOver(g);

    }

    public void moveSnake() {
        // עכשיו מטפלים בכל הTILES שהם לא הראש
        for (int i = bodyParts - 1; i > 0; i--) { // tiles[0] is the snake head and tiles[1] is near snake head
            //  tiles[i] = tiles[i - 1];
            tiles[i].tileX = tiles[i - 1].tileX;
            tiles[i].tileY = tiles[i - 1].tileY;

        }

        switch (direction) {

            case UP:
                tiles[0].setTileY(tiles[0].getTileY() - 1);
                break;

            case DOWN:
                tiles[0].setTileY(tiles[0].getTileY() + 1);
                break;

            case RIGHT:
                tiles[0].setTileX(tiles[0].getTileX() + 1);
                break;


            case LEFT:
                tiles[0].setTileX(tiles[0].getTileX() - 1);
                break;
        }


    }

    public void drawSnake(Graphics g) {

        for (int i = bodyParts - 1; i > 0; i--) {
            g.setColor(Color.GREEN);
            g.fillRect(tiles[i].tileX * UNIT_SIZE, tiles[i].tileY * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        }
        g.setColor(new Color(0x3CCD27));
        g.fillRect(tiles[0].tileX * UNIT_SIZE, tiles[0].tileY * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
    }

    public void checkCollision() {

        for (int i = 1; i < bodyParts - 1; i++) {
            if (tiles[0].tileX == tiles[i].tileX && tiles[0].tileY == tiles[i].tileY) {
                running = false;
                return;
            }

        }
        if (tiles[0].tileX >= SCREEN_WIDTH / UNIT_SIZE || tiles[0].tileX < 0 || tiles[0].tileY >= SCREEN_HEIGHT / UNIT_SIZE || tiles[0].tileY < 0) {
            running = false;
        }
    }

    public void gameOver(Graphics g) {

        g.setColor(Color.GREEN);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        JButton startOver = new JButton("star over");
        startOver.addActionListener(this);
        startOver.setSize(100, 100);


        g.drawString("Score: " + bodyParts, (SCREEN_WIDTH) / 2, g.getFont().getSize());

        timer.stop();

    }

    public void isEat() {
        if (tiles[0].tileX == appleX && tiles[0].tileY == appleY) {
            bodyParts++;
            tiles[bodyParts - 1] = new Tile(appleX, appleY);
            generateAppleCoordinates();

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            moveSnake();

            checkCollision();
            isEat();
        }
        repaint();

    }
}
