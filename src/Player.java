import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;


public class Player extends Sprite implements Commons{

    private final int START_Y = GROUND - PLAYER_HEIGHT; 
    private final int START_X = (BOARD_WIDTH - PLAYER_WIDTH)/2 ;

    private final String player = "spacepix/player.png";
    private int width;

    public Player() {

        ImageIcon ii = new ImageIcon(player);

        width = ii.getImage().getWidth(null); 

        setImage(ii.getImage());
        setX(START_X);
        setY(START_Y);
    }

    public void act() {
        x += dx;
        if (x <= 2) 
            x = 2;
        if (x >= BOARD_WIDTH - PLAYER_WIDTH) 
            x = BOARD_WIDTH - PLAYER_WIDTH;
        y += dy;
        if (y <= 2) 
            y = 2;
        if (y >= GROUND - PLAYER_HEIGHT  ) 
            y = GROUND - PLAYER_HEIGHT;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT)
        {
            dx = -4;
        }

        if (key == KeyEvent.VK_RIGHT)
        {
            dx = 4;
        }
        if (key == KeyEvent.VK_UP)
        {
            dy = -4;
        }

        if (key == KeyEvent.VK_DOWN)
        {
            dy = 4;
        }
        

    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT)
        {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT)
        {
            dx = 0;
        }
        if (key == KeyEvent.VK_UP)
        {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN)
        {
            dy = 0;
        }
    }
}
