import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        super();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new GamePanel());
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);


    }

}
