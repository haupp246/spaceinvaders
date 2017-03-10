

import java.util.Random;

import javax.swing.ImageIcon;

public class PowerUp extends Sprite{
	 private String powerup = "spacepix/powerup.png";
	 public PowerUp() {
	 
	 }

	 public PowerUp(int x, int y) {

	        ImageIcon ii = new ImageIcon(powerup);
	        setImage(ii.getImage());
	        setX(x);
	        setY(y);
	        Random generator = new Random();
	        dx=-2+generator.nextInt(5);
	        dy=1;
	 }
	 public void act(){
		 x+=dx;
		 y+=dy;
		 if (x<=2 || x+24>=798) dx=-dx;
		 if (y+24>= 500 || y<=2) die();
		 
	 }
	 
}
