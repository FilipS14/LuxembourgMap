import javax.swing.*;

public class Main {
    private static void initUI() {
        JFrame frame = new JFrame("Harta Luxemburgului");
        frame.add(new MyPanel());

        frame.setSize(1000,1000);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                initUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}