

import java.util.Random;

import javax.swing.ImageIcon;


public class Boss extends Sprite implements Commons{

    

    private final String boss = "spacepix/boss.png";
    private int width;
    private int cycle=0;
    private int changeF=40;
    private int health = 3;
    //private BBomb bbomb;
    
    public Boss() {

        ImageIcon ii = new ImageIcon(boss);

        width = ii.getImage().getWidth(null); 

        setImage(ii.getImage());
        setX(BOARD_WIDTH/2-40);
        setY(10);
        dx=1;
        dy=0;
    }

    public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void act() {
       cycle++;
       //System.out.println(cycle);

     
        if (x <= 2) {
            x = 2;
            dx=-dx;
        }
        if (x >= BOARD_WIDTH - 80) {
            x = BOARD_WIDTH - 80;
            dx=-dx;
        }
        if (y <= 2) {
            y = 2;
            dy=-dy;
        }
        if (y >= GROUND - 80  ) {
            y = GROUND - 80;
            dy=-dy;
        }
        if (cycle== changeF)
        {	
        	Random generator = new Random();
        	dx=-1+ generator.nextInt(3);
        	dy=-1+ generator.nextInt(3);
        	//System.out.println(dx+" "+dy+" "+"Change pls!");
        	changeF= 30+ generator.nextInt(20);
        	cycle=0;
        }
        x+=dx;
        y+=dy;
        
    }

}