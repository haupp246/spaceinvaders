import java.util.Random;

import javax.swing.ImageIcon;


public class Alien extends Sprite {

    private Bomb bomb;
    private final String shot = "spacepix/alien.png";
    private int b=0;
       public Alien(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(shot);
        setImage(ii.getImage());

    }

    public void act(int direction) {
    	
    	this.dy=1;
    
        this.x += direction;
        b++;
        if (b==15){
        this.y += dy;
        b=0;
        }
        
    }

    public Bomb getBomb() {
        return bomb;
    }

    public class Bomb extends Sprite {

        private final String bomb = "spacepix/bomb.png";
        private boolean destroyed;

        public Bomb(int x, int y) {
            setDestroyed(true);
            this.x = x;
            this.y = y;
            ImageIcon ii = new ImageIcon(bomb);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }
    }
}
