import javax.swing.*;
import java.awt.*;

public class ClueBoard extends JPanel {

    private final int TILE_SIZE = 28;
    private final int ROWS = 25;
    private final int COLS = 24;

    // 0 = Hallway
    // 1 = Room
    // 2 = Outer Border / Wall
    // 3 = Door
    // 4 = Center "Clue" Box
    // 5 = Start Square
    // 6 = Secret Passage

    private int[][] board = new int[ROWS][COLS];

    public ClueBoard() {
        setPreferredSize(new Dimension(
            COLS * TILE_SIZE,
            ROWS * TILE_SIZE
        ));

        createBoard();
    }

    private void createBoard() {

        // Start with hallways
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                board[r][c] = 0;
            }
        }

        // Outer burnt-orange border spaces
        fillRect(0, 0, 1, 9, 2);
        fillRect(0, 15, 1, 1, 2);
        fillRect(0, 17, 1, 7, 2);
        fillRect(4, 0, 1, 1, 2);
        fillRect(6, 0, 1, 1, 2);
        fillRect(11, 0, 2, 1, 2);
        fillRect(17, 0, 1, 1, 2);
        fillRect(19, 0, 5, 1, 2);
        fillRect(24, 0, 1, 9, 2);
        fillRect(24, 10, 1, 4, 2);
        fillRect(24, 15, 1, 9, 2);
        fillRect(6, 23, 1, 1, 2);
        fillRect(8, 23, 1, 1, 2);
        fillRect(16, 23, 2, 1, 2);
        fillRect(23, 23, 1, 1, 2);

        // Create nine classic rooms
        fillRect(0, 0, 4, 7, 1);     // Study
        fillRect(0, 9, 7, 6, 1);     // Hall
        fillRect(0, 17, 6, 7, 1);    // Lounge
        fillRect(6, 0, 5, 6, 1);     // Library
        fillRect(7, 6, 3, 1, 1);     // Library bay
        fillRect(12, 0, 5, 6, 1);    // Billiard Room
        fillRect(19, 0, 5, 5, 1);    // Conservatory
        fillRect(20, 5, 4, 1, 1);    // Conservatory corner
        fillRect(17, 8, 6, 8, 1);    // Ballroom
        fillRect(23, 10, 2, 4, 1);   // Ballroom alcove
        fillRect(18, 18, 6, 6, 1);   // Kitchen
        fillRect(9, 16, 6, 8, 1);    // Dining Room
        fillRect(15, 19, 1, 5, 1);   // Dining Room notch

        // Center "Clue" box
        fillRect(8, 9, 7, 5, 4);

        // Doors
        int[][] doors = {
            {4, 6},
            {7, 12},
            {6, 17},
            {8, 7}, 
            {11, 2}, {15, 6},
            {18, 4},
            {16, 10}, {16, 13}, {18, 7}, {18, 16},
            {17, 18},
            {8, 17}, {12, 15}
        };
        for (int[] d : doors) {
            board[d[0]][d[1]] = 3;
        }

        // Start squares
        int[][] starts = {
            {0, 16}, {7, 23}, {24, 14}, {24, 9}, {18, 0}, {5, 0}
           //red    //yellow  //white  //green   //blue   //purple
        };
        for (int[] s : starts) {
            board[s[0]][s[1]] = 5;
        }

        // Secret passages
        board[3][6] = 6;
        board[18][18] = 6;
        board[5][17] = 6;
        board[19][4] = 6;
    }

    private void fillRect(int startRow, int startCol, int numRows, int numCols, int val) {
        for (int r = startRow; r < startRow + numRows && r < ROWS; r++) {
            for (int c = startCol; c < startCol + numCols && c < COLS; c++) {
                board[r][c] = val;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        Color hallway = new Color(252, 224, 52);
        Color wall = new Color(214, 114, 34);
        Color roomFloor = new Color(248, 241, 215);
        Color doorColor = new Color(235, 195, 95);
        Color passageColor = new Color(130, 190, 135);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {

                int x = c * TILE_SIZE;
                int y = r * TILE_SIZE;
                int tile = board[r][c];

                if (tile == 0 || tile == 5) {
                    g2.setColor(hallway);
                } else if (tile == 1) {
                    g2.setColor(roomFloor);
                } else if (tile == 3) {
                    g2.setColor(doorColor);
                } else if (tile == 6) {
                    g2.setColor(passageColor);
                } else {
                    g2.setColor(wall);
                }

                g2.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                // Grid lines only on walkable hallway/door/start tiles
                if (tile == 0 || tile == 3 || tile == 5) {
                    g2.setColor(new Color(180, 150, 30));
                    g2.drawRect(x, y, TILE_SIZE, TILE_SIZE);
                }

                if (tile == 3) {
                    g2.setColor(Color.BLACK);
                    g2.setFont(new Font("Arial", Font.BOLD, 7));
                    g2.drawString("DOOR", x + 3, y + 17);
                }
            }
        }

        // Thick black outlines around rooms
        g2.setStroke(new BasicStroke(3f));
        g2.setColor(Color.BLACK);
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int x = c * TILE_SIZE;
                int y = r * TILE_SIZE;
                boolean isRoom = (board[r][c] == 1 || board[r][c] == 6);

                if (isRoom) {
                    if (r == 0 || (board[r - 1][c] != 1 && board[r - 1][c] != 6))
                        g2.drawLine(x, y, x + TILE_SIZE, y);
                    if (r == ROWS - 1 || (board[r + 1][c] != 1 && board[r + 1][c] != 6))
                        g2.drawLine(x, y + TILE_SIZE, x + TILE_SIZE, y + TILE_SIZE);
                    if (c == 0 || (board[r][c - 1] != 1 && board[r][c - 1] != 6))
                        g2.drawLine(x, y, x, y + TILE_SIZE);
                    if (c == COLS - 1 || (board[r][c + 1] != 1 && board[r][c + 1] != 6))
                        g2.drawLine(x + TILE_SIZE, y, x + TILE_SIZE, y + TILE_SIZE);
                }
            }
        }
        g2.setStroke(new BasicStroke(1f));

        // Center "Clue" box
        int clueX = 9 * TILE_SIZE;
        int clueY = 8 * TILE_SIZE;
        int clueW = 5 * TILE_SIZE;
        int clueH = 7 * TILE_SIZE;
        g2.setColor(wall);
        g2.fillRect(clueX, clueY, clueW, clueH);
        g2.setColor(Color.BLACK);
        g2.drawRect(clueX, clueY, clueW, clueH);

        g2.setFont(new Font("Serif", Font.BOLD, 34));
        g2.drawString("\"Clue\"", clueX + 22, clueY + 135);

        // Room labels
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("STUDY", 2 * TILE_SIZE + 10, 2 * TILE_SIZE + 15);
        g2.drawString("HALL", 11 * TILE_SIZE + 5, 3 * TILE_SIZE + 20);
        g2.drawString("LOUNGE", 19 * TILE_SIZE + 5, 3 * TILE_SIZE + 15);
        g2.drawString("LIBRARY", 1 * TILE_SIZE + 15, 9 * TILE_SIZE);
        g2.drawString("BILLIARD ROOM", 8, 14 * TILE_SIZE + 15);
        g2.drawString("CONSERVATORY", 5, 22 * TILE_SIZE);
        g2.drawString("BALLROOM", 10 * TILE_SIZE + 12, 20 * TILE_SIZE + 15);
        g2.drawString("DINING ROOM", 17 * TILE_SIZE + 8, 12 * TILE_SIZE + 15);
        g2.drawString("KITCHEN", 19 * TILE_SIZE + 15, 21 * TILE_SIZE + 15);

        // Start labels
        g2.setFont(new Font("Arial", Font.BOLD, 7));
        g2.drawString("START", 16 * TILE_SIZE + 2, 14);
        g2.drawString("START", 23 * TILE_SIZE + 2, 7 * TILE_SIZE + 16);
        g2.drawString("START", 14 * TILE_SIZE + 2, 24 * TILE_SIZE + 16);
        g2.drawString("START", 9 * TILE_SIZE + 2, 24 * TILE_SIZE + 16);
        g2.drawString("START", 2, 18 * TILE_SIZE + 16);
        g2.drawString("START", 2, 5 * TILE_SIZE + 16);
    }


}