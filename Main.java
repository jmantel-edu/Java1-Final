import javax.swing.JFrame;

/**
 * Java 1 Final Project
 * By:
 *  Jordan Mantel
 *  Darsh Singh
 *  Iremide Omoniyi
 *  Yusuke Ho
 * 
 */

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Clue Board Game");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ClueBoard());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
