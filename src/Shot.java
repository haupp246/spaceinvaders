import javax.swing.ImageIcon;


public class Shot extends Sprite {

    private String shot = "spacepix/shot.png";
    private final int H_SPACE = 20;
    private final int V_SPACE = 2;

    public Shot() {
    }

    public Shot(int x, int y) {

        ImageIcon ii = new ImageIcon(shot);
        setImage(ii.getImage());
        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }
}
