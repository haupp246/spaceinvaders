import javax.swing.ImageIcon;


public class BBomb extends Sprite {

    private String bbomb = "spacepix/bbomb.png";

    private int dX;
	private int dY;
    public BBomb() {
    }
    
    public BBomb(int bx, int by) {

        ImageIcon ii = new ImageIcon(bbomb);
        setImage(ii.getImage());
        setX(bx + 40);
        setY(by + 70);
       
        
    }
    public void setTarget(int px,int py)
    {
    	dX= px-x;
    	dY= py-y;
    	if (dX>0 && dY>0){
    	dx =  (int) Math.round(Math.sqrt((18*dX*dX)/(dX*dX+dY*dY)));
     	dy = (int) Math.round(Math.sqrt((18*dY*dY)/(dX*dX+dY*dY)));
    	}
    	if (dX>0 && dY<0){
    	dx = (int) Math.round(Math.sqrt((18*dX*dX)/(dX*dX+dY*dY)));
     	dy = -(int) Math.round(Math.sqrt((18*dY*dY)/(dX*dX+dY*dY)));
    	}
    	if (dX<0 && dY>0){
    	dx = -(int) Math.round(Math.sqrt((18*dX*dX)/(dX*dX+dY*dY)));
     	dy = (int) Math.round(Math.sqrt((18*dY*dY)/(dX*dX+dY*dY)));
    	}
    	if (dX<0 && dY<0){
    	dx = -(int) Math.round(Math.sqrt((18*dX*dX)/(dX*dX+dY*dY)));
     	dy = -(int) Math.round(Math.sqrt((18*dY*dY)/(dX*dX+dY*dY)));
    	}
    }
	


	public void setVelocity(int dx, int dy)
	{
		this.dx=dx;
		this.dy=dy;
	}

	public void act(){
    	
    	//System.out.println(dx+"  "+dy);
    	x+=dx;
    	y+=dy;
    }


    
    
}
