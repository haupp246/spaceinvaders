import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class Board extends JPanel implements Runnable, Commons { 

    private Dimension d;
    private ArrayList aliens;
    private Player player;
    private Shot shot;
    private ArrayList shots;
    private Boss boss;
    private BBomb bbomb;
    private ArrayList bbombs;
    private PowerUp powerup;
    private ArrayList powerups;
    
    private int alienX = 150;
    private int alienY = 5;
    private int direction = -1;
    private int deaths = 0;
    private int shotCd = 0;
    private int frame =0;
    private int goal =0;
    private int score = 0;
    private int powerlvl = 1;
    private boolean ingame = true;
    private final String expl = "spacepix/explosion.png";
    private final String alienpix = "spacepix/alien.png";
    private String message = "Game Over";
    private int level=0;
    private Thread animator;

    public Board() 
    {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.black);

        levelInit();
        setDoubleBuffered(true);
    }

    public void addNotify() {
        super.addNotify();
        
    }

    public void levelInit() {
    	deaths=0;
    	level++;
    	goal=(level/4+1)*(level+2-(level/4)*2);
        aliens = new ArrayList();
        shots = new ArrayList();
        bbombs = new ArrayList();
        powerups = new ArrayList();
       
        ImageIcon ii = new ImageIcon("spacepix/alien.png");

        for (int i=0; i < (level/4+1); i++) {
            for (int j=0; j < level+2 -(level/4)*2; j++) {
                Alien alien = new Alien(alienX + 50*j, alienY + 50*i);
                alien.setImage(ii.getImage());
                aliens.add(alien);
            }
        }

        player = new Player();
        powerup = new PowerUp();
        shot = new Shot();
        boss = new Boss();
        boss.setHealth(level);
        if (level%5!=0){
       boss.setDying(true);
        }
        bbomb = new BBomb();

        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void drawAliens(Graphics g) 
    {
        Iterator it = aliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();

            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {
                alien.die();
            }
        }
    }

    public void drawPlayer(Graphics g) {

        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {
            player.die();
            ingame = false;
        }
    }
    public void drawBoss(Graphics g) {

        if (boss.isVisible()) {
            g.drawImage(boss.getImage(), boss.getX(), boss.getY(), this);
        }

        if (boss.isDying()) {
            boss.die();
         
        }
    }

    public void drawShot(Graphics g) {
        
        Iterator it = shots.iterator();

        while (it.hasNext()) {
            Shot shot = (Shot) it.next();

            if (shot.isVisible()) {
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }

            if (shot.isDying()) {
                shot.die();
            }
        }
    }
   public void drawBBomb(Graphics g) {
        
        Iterator it = bbombs.iterator();

        while (it.hasNext()) {
            BBomb bbomb = (BBomb) it.next();

            if (bbomb.isVisible()) {
                g.drawImage(bbomb.getImage(), bbomb.getX(), bbomb.getY(), this);
            }

            if (bbomb.isDying()) {
            	bbomb.die();
            }
        }
    }
   public void drawPowerUp(Graphics g) {
       
       Iterator it = powerups.iterator();

       while (it.hasNext()) {
           powerup = (PowerUp) it.next();

           if (powerup.isVisible()) {
               g.drawImage(powerup.getImage(), powerup.getX(), powerup.getY(), this);
           }

           if (powerup.isDying()) {
        	   powerup.die();
           }
       }
   }
    public void drawBombing(Graphics g) {

        Iterator i3 = aliens.iterator();

        while (i3.hasNext()) {
            Alien a = (Alien) i3.next();

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this); 
            }
        }
    }
    

    public void paint(Graphics g)
    {
      super.paint(g);

      g.setColor(Color.black);
      g.fillRect(0, 0, d.width, d.height);
      g.setColor(Color.green);   

      if (ingame) {

        g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
        drawAliens(g);
        drawPlayer(g);
        drawBoss(g);
        drawShot(g);
        drawBombing(g);
        drawBBomb(g);
        drawPowerUp(g);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        g.setColor(Color.white);
        g.setFont(small);
        FontMetrics metr = this.getFontMetrics(small);
        g.drawString("Score "+score, 100,            550);
        g.drawString("Level "+level, (BOARD_WIDTH - metr.stringWidth(message))/2, 
            550);
      }

      Toolkit.getDefaultToolkit().sync();
      g.dispose();
    }

    public void gameOver()
    {

        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message))/2, 
            BOARD_WIDTH/2);
    }

    public void animationCycle()  {

        if (level>10) {
        	System.out.println(level);
            ingame = false;
            message = "Game won!";
        }

        // player

        player.act();

        // shot
        Iterator it2 = shots.iterator();

        while (it2.hasNext()) {
            Shot shot = (Shot) it2.next();

        if (shot.isVisible()) {
            Iterator it = aliens.iterator();
            int shotX = shot.getX();
            int shotY = shot.getY();

            while (it.hasNext()) {
                Alien alien = (Alien) it.next();
                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX+8 >= (alienX) && 
                        shotX <= (alienX + ALIEN_WIDTH) &&
                        shotY+20 >= (alienY) &&
                        shotY <= (alienY+ALIEN_HEIGHT) ) {
                            ImageIcon ii = 
                                new ImageIcon(expl);
                            alien.setImage(ii.getImage());
                            alien.setDying(true);
                            alien.die();
                            deaths++;
                            shot.die();
                            score+= level*10;
                            Random generator = new Random();
                            if (generator.nextInt(20)==1){
                            	powerup = new PowerUp(alienX,alienY);
                            	powerups.add(powerup);
                            }
                    }
                }
            }
            if(!boss.isDying()){
            int bossX=boss.getX();
            int bossY=boss.getY();
            	if (boss.isVisible() && shot.isVisible()) {
            		if (shotX+8 >= (bossX) && 
                        shotX <= (bossX + 80) &&
                        shotY+20 >= (bossY) &&
                        shotY <= (bossY+80) ) {
                            ImageIcon ii = 
                                new ImageIcon(expl);
                            
                            boss.setHealth(boss.getHealth()-1);
                            shot.die();
                    }
            	}
            }
        }


            int y = shot.getY();
            y -= 4;
            if (y < 0)
                shot.die();
            else shot.setY(y);
        }

        // aliens
        
        Random gen = new Random();
        
         Iterator it1 = aliens.iterator();

         while (it1.hasNext()) {
             Alien a1 = (Alien) it1.next();
             int x = a1.getX();

             if (x  >= BOARD_WIDTH - BORDER_RIGHT && direction > 0) {
            	 direction = -2 + gen.nextInt(2);
                 Iterator i1 = aliens.iterator();
                 while (i1.hasNext()) {
                     Alien a2 = (Alien) i1.next();
                   //  a2.setY(a2.getY() + GO_DOWN);
                 }
             }

            if (x <= BORDER_LEFT && direction < 0) {
            	direction =1+gen.nextInt(2);

                Iterator i2 = aliens.iterator();
                while (i2.hasNext()) {
                    Alien a = (Alien)i2.next();
                  //  a.setY(a.getY() + GO_DOWN);
                }
            }
        }

         Iterator it = aliens.iterator();
      
        while (it.hasNext()) {
        	
            Alien alien = (Alien) it.next();
            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > GROUND - ALIEN_HEIGHT) {
                    ingame = false;
                    message = "Invasion!";
                }
                
                //System.out.println(direction);
                alien.act(direction);
            }
        }
        //boss
        if (!boss.isDying()){
        	
        boss.act();
        int bossX = boss.getX();
        int bossY = boss.getY();
        if (boss.getHealth()==0){
        	boss.die();
        	boss.setDying(true);
        	score+= level*50;
        	powerup = new PowerUp(bossX,bossY);
        	powerups.add(powerup);
        }
        
        	  
	           int playerX = player.getX();
	           int playerY = player.getY();
        	
                if ( bossX+80 >= (playerX) && 
                    bossX <= (playerX+PLAYER_WIDTH) &&
                    bossY+80 >= (playerY) && 
                    bossY <= (playerY+PLAYER_HEIGHT) ) {
                        ImageIcon ii = 
                            new ImageIcon(expl);
                        System.out.println("Die by boss");
                        player.setImage(ii.getImage());
                        player.setDying(true);
                      
                    }
        }
      	
        

            
//     bbomb   	

		if (!boss.isDying()){
		        Random bbombGen = new Random();
		       
		        	
		            int bossX = boss.getX();
		            int bossY = boss.getY();
		         	int x = bbomb.getX();
		         	int y = bbomb.getY();
		         	int pX = player.getX();
		            int pY = player.getY();
		            if (bbombGen.nextInt(40)==1)        {  
		     
			             bbomb = new BBomb (bossX,bossY);
			             bbomb.setTarget(pX+24,pY+24);
			             bbombs.add(bbomb);
		             
		             
		            }
		            if (bbombGen.nextInt(150)==1)	{
		            	 bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(3,3);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
			             bbomb.setVelocity(-3,3);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
			             bbomb.setVelocity(3,-3);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
			             bbomb.setVelocity(-3,-3);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
			             bbomb.setVelocity(0,4);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
			             bbomb.setVelocity(4,0);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
			             bbomb.setVelocity(0,-4);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
			             bbomb.setVelocity(-4,0);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(1,4);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(-1,4);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(1,-4);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(-1,-4);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(4,1);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(-4,1);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(4,-1);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(-4,-1);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(2,4);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(-2,4);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(2,-4);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(-2,-4);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(4,2);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(-4,2);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(4,-2);
			             bbombs.add(bbomb);
			             
			             bbomb = new BBomb (bossX,bossY);
		            	 bbomb.setVelocity(-4,-2);
			             bbombs.add(bbomb);
		            }
		}
		        Iterator itb = bbombs.iterator();
		    	
		        while (itb.hasNext()){
		        	BBomb bb=(BBomb) itb.next();
		            int bombX = bb.getX();
		            int bombY = bb.getY();
		            int playerX = player.getX();
		            int playerY = player.getY();
		        	bb.act();
		        	  if (player.isVisible() ) {
		                  if ( bombX+10 >= (playerX) && 
		                      bombX <= (playerX+PLAYER_WIDTH) &&
		                      bombY+10 >= (playerY) && 
		                      bombY <= (playerY+PLAYER_HEIGHT) ) {
		                          ImageIcon ii = 
		                              new ImageIcon(expl);
		                          System.out.println("Die by bbomb");
		                          player.setImage(ii.getImage());
		                          player.setDying(true);
		                        
		                      }
		        	  }
		        	  if(bombY>GROUND)
		        	  {
		        		  bb.die();
		        	  }
		               
		        }
		

        // bombs

        Iterator i3 = aliens.iterator();
        Random generator = new Random();

        while (i3.hasNext()) {
            int shot = generator.nextInt(15);
            Alien a = (Alien) i3.next();
            Alien.Bomb b = a.getBomb();
            int bombX = b.getX();
            int bombY = b.getY();
            int playerX = player.getX();
            int playerY = player.getY();
            if (shot == CHANCE && a.isVisible() && b.isDestroyed()) {

                b.setDestroyed(false);
                b.setX(a.getX());
                b.setY(a.getY());   
            }

            
            if (player.isVisible() && !b.isDestroyed()) {
                if ( bombX+8 >= (playerX) && 
                    bombX <= (playerX+PLAYER_WIDTH) &&
                    bombY+20 >= (playerY) && 
                    bombY <= (playerY+PLAYER_HEIGHT) ) {
                        ImageIcon ii = 
                            new ImageIcon(expl);
                        player.setImage(ii.getImage());
                        System.out.println("Die by bomb");
                        player.setDying(true);
                        b.setDestroyed(true);;
                    }
            }
            if (player.isVisible() && !a.isDying())
                if ( a.getX()+32 >= (playerX) && 
                        a.getX() <= (playerX+PLAYER_WIDTH) &&
                        a.getY()+32 >= (playerY) && 
                        a.getY() <= (playerY+PLAYER_HEIGHT) ) {
                            ImageIcon ii = 
                                new ImageIcon(expl);
                            System.out.println("Die by alien");
                            player.setImage(ii.getImage());
                            player.setDying(true);
                            a.setImage(ii.getImage());
                            a.setDying(true);
                        }
            

            if (!b.isDestroyed()) {
                b.setY(b.getY() + 1);   
                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }
        }
        

        Iterator i4 = powerups.iterator();
        while (i4.hasNext()) {
        	PowerUp powerup = (PowerUp) i4.next();
        	if (!powerup.isDying()) {
	        	int pwX = powerup.getX();
	            int pwY = powerup.getY();
	            int playerX = player.getX();
	            int playerY = player.getY();
	        	powerup.act();
	        	if (player.isVisible()  ) {
	        		if ( pwX+24 >= (playerX) && 
                     pwX <= (playerX+PLAYER_WIDTH) &&
                     pwY+24 >= (playerY) && 
                     pwY <= (playerY+PLAYER_HEIGHT) ) {
                         
                         powerup.die();
                         powerup.setDying(true);
                         score+=50;
                         powerlvl++;
                    }
       	  	 	}
        	}
        }
       
    }

    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();
        
        while (ingame) {
            repaint();
            if(deaths==goal && boss.isDying()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("interrupted");
                }
                score+= level*100;
            	levelInit();
            
            }
            animationCycle();
            frame++;
            shotCd++;
            //System.out.println(frame);
            Random generator = new Random();
          //  if (frame %60 == 0) direction = -3+generator.nextInt(5);
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;
            
            if (sleep < 0) 
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            beforeTime = System.currentTimeMillis();
        }
        gameOver();
    }

    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {

          player.keyPressed(e);

          int x = player.getX();
          int y = player.getY();

          if (ingame)
          {
            if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            	if (shotCd>=20)
            	{
            		if (powerlvl==1){
	                    shot = new Shot(x, y);
	                    shots.add(shot);
	                    shotCd=0;
            		}
            		if (powerlvl==2){
            		    shot = new Shot(x-3, y);
                        shots.add(shot);
                        shot = new Shot(x+3, y);
                        shots.add(shot);
                        shotCd=0;
            		}
            		if (powerlvl>=3){
            		    shot = new Shot(x-6, y);
                        shots.add(shot);
                        shot = new Shot(x+6, y);
                        shots.add(shot);
                        shot = new Shot(x, y);
	                    shots.add(shot);
                        shotCd=0;
            		}
            	}
            }
          }
        }
    }
}
