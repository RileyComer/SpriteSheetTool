package Graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class Display extends JPanel{
	private Color[][] sprite,anim;
	private Color selected;
	private int iw, ih,sx,sy;
	private int topToolGap=100;
	private int rightToolGap=400;
	private int cx=100;
	private int cy=100;
	private int hx=10;
	private int hy=100;
	private int ax=10;
	private int ay=480;
	private int gridGap=1;
	private int pixleSize;
	private int startX;
	private int startY;
	private Color hue;
	private int cpx;
	private int cpy;
	private int hpx;
	private int hpy;
	private boolean running;
	
	public Display(int iw,int ih){
		this.iw=iw;
		this.ih=ih;
		sx=0;
		sy=0;
		selected=new Color(255,0,0);
		hue=new Color(255,0,0);
		sprite=new Color[iw][ih];
		running=false;
	}
	
	public void paintComponent(Graphics g) {
		if(!running) {
			if(((this.getHeight()-topToolGap)*1.0)/ih>=((this.getWidth()-rightToolGap)*1.0)/iw) {
				pixleSize=(int) ((this.getWidth()-rightToolGap)*1.0)/iw;
			}else {
				pixleSize=(int)((this.getHeight()-topToolGap)*1.0)/ih;
			}
			startX=(int)(((this.getWidth()-rightToolGap)/2.0)-((pixleSize*iw)/2.0))+gridGap;
			startY=(int)(((this.getHeight()-topToolGap)/2.0)-((pixleSize*ih)/2.0))+topToolGap;
			cpx=(iw*pixleSize)+startX+cx+255;
			cpy=startY+cy;
			hpx=(iw*pixleSize)+startX+hx;
			hpy=startY+hy;
			running=true;
		}
		
		super.paintComponent(g);
		this.setBackground(new Color(20,20,20));
		g.setColor(new Color(100,100,100));
		g.fillRect(0,0,this.getWidth(),startY-gridGap);
		g.setColor(new Color(50,50,50));
		g.fillRect(startX,startY,iw*pixleSize,ih*pixleSize);
		for(int y=0;y<ih;y++) {
			for(int x=0;x<iw;x++) {
				if(sprite[x][y]!=null) {
					g.setColor(sprite[x][y]);
					g.fillRect(x*(pixleSize)+startX, y*(pixleSize)+startY, (pixleSize)-(gridGap*2), (pixleSize)-(gridGap*2));
				}else {
					g.setColor(new Color(40,40,40));
					g.fillRect(x*(pixleSize)+startX, y*(pixleSize)+startY, (pixleSize)-(gridGap*2), (pixleSize)-(gridGap*2));
				}
			}
		}
		//ColorSelector
		
		for(int y=0;y<255;y+=2) {
			for(int x=0;x<255;x+=2) {
				g.setColor(new Color((int) ((((x/255.0)*hue.getRed())*(y/255.0))+((y/255.0)*(255-((x/255.0)*hue.getRed())))*((255-x)/255.0)),(int) ((((x/255.0)*hue.getGreen())*(y/255.0))+((y/255.0)*(255-((x/255.0)*hue.getGreen())))*((255-x)/255.0)),(int) ((((x/255.0)*hue.getBlue())*(y/255.0))+((y/255.0)*(255-((x/255.0)*hue.getBlue())))*((255-x)/255.0))));
				g.fillRect(x+(iw*pixleSize)+startX+cx, 255+startY+cy-y, 2, 2);
			}
		}
		g.setColor(selected);
		g.fillRect((iw*pixleSize)+startX+400, startY+100, 150, 150);
		
		//HueSelector
		for(int i2=0;i2<6;i2++) {
			for(int i1=0;i1<255;i1+=2) {
				if(i2==0) {
					g.setColor(new Color(255,i1,0));
				}else if(i2==1) {
					g.setColor(new Color(255-i1,255,0));
				}else if(i2==2) {
					g.setColor(new Color(0,255,i1));
				}else if(i2==3) {
					g.setColor(new Color(10,255-i1,255));
				}else if(i2==4) {
					g.setColor(new Color(i1,0,255));
				}else if(i2==5) {
					g.setColor(new Color(255,0,255-i1));
				}
				g.fillRect((iw*pixleSize)+startX+hx, (int) ((i1/6)+(i2*(42.5))+startY+hy), 80, 2);
			}
		}
		//Color picker cursor
		g.setColor(new Color(255,255,255));
		g.fillRect(cpx+2, cpy, 2, 2);
		g.fillRect(cpx-2, cpy, 2, 2);
		g.fillRect(cpx, cpy-2, 2, 2);
		g.fillRect(cpx, cpy+2, 2, 2);
		
		//Hue picker cursor
		g.fillRect(hpx+2, hpy, 2, 2);
		g.fillRect(hpx-2, hpy, 2, 2);
		g.fillRect(hpx, hpy-2, 2, 2);
		g.fillRect(hpx, hpy+2, 2, 2);
		
		//Animation screen
		for(int y=0;y<ih;y++) {
			for(int x=0;x<anim.length;x++) {
				if(anim[x][y]!=null) {
					g.setColor(anim[x][y]);
					g.fillRect(((iw*pixleSize)+startX+ax)+x*(100/iw), ay+y*(100/ih),(100/iw), (100/ih));
				}else {
					g.setColor(new Color(40,40,40));
					g.fillRect(((iw*pixleSize)+startX+ax)+x*(100/iw), ay+y*(100/ih),(100/iw), (100/ih));
				}
			}
		}
		for(int y=0;y<ih;y++) {
			for(int x=0;x<iw;x++) {
				if(sprite[x][y]!=null) {
					g.setColor(sprite[x][y]);
					g.fillRect(((iw*pixleSize)+startX+ax)+x*(100/iw)+(sx*(100/iw)*iw), ay+y*(100/ih),(100/iw), (100/ih));
				}else {
					g.setColor(new Color(50,50,50));
					g.fillRect(((iw*pixleSize)+startX+ax)+x*(100/iw)+(sx*(100/iw)*iw), ay+y*(100/ih),(100/iw), (100/ih));
				}
			}
		}
	}
	
	public void redraw() {
		this.repaint();
	}
	
	public void addPixle(int x, int y) {
		sprite[x][y]=copyColor(selected);
	}
	
	public void removePixle(int x, int y) {
		sprite[x][y]=null;
	}
	
	public Color getPixle(int x, int y) {
		return sprite[x][y];
	}
	
	public Color[][] getSprite() {
		return sprite;
	}
	
	public void setSprite(Color[][] c) {
		for(int y=0;y<ih;y++) {
			for(int x=0;x<iw;x++) {
				if(c[x][y]!=null) {
					sprite[x][y]=copyColor(c[x][y]);
				}else {
					sprite[x][y]=null;
				}
			}
		}
	}
	
	public Color copyColor(Color c) {
		if(c!=null) {
			Color out=new Color(c.getRed(),c.getGreen(),c.getBlue());
			return out;
		}
		return null;
	}
	
	public int getLocation(double x,double y) {
		if(x>startX&&y>startY&&x<startX+(pixleSize*iw)&&y<startY+(pixleSize*ih)) {
			return (int) (((x-startX)/(pixleSize))+((((int)(y-startY))/(pixleSize)))*iw);
		}
		return-1;
		
	}
	
	public int getTool(double x,double y) {
		if(x>(iw*pixleSize)+startX+cx&&x<255+(iw*pixleSize)+startX+cx&&y>startY+cy&&y<255+startY+cy) {
			return 1;
		}else if(x>(iw*pixleSize)+startX+hx&&x<(iw*pixleSize)+startX+hx+80&&y>startY+hy&&y<(255)+startY+hy) {
			return 2;
		}
		return 0;
	}
	
	public void setAnim(Color[][] in) {
		anim=in;
	}
	
	public void setSpriteXY(int x, int y) {
		sx=x;
		sy=y;
	}
	
	public Color getColorSelector(double x,double y) {
		int x1=(int) x-((iw*pixleSize)+startX+cx), y1=(int) y-(startY+cy);
		y1=255-y1;
		Color out=new Color((int) ((((x1/255.0)*hue.getRed())*(y1/255.0))+((y1/255.0)*(255-((x1/255.0)*hue.getRed())))*((255-x1)/255.0)),(int) ((((x1/255.0)*hue.getGreen())*(y1/255.0))+((y1/255.0)*(255-((x1/255.0)*hue.getGreen())))*((255-x1)/255.0)),(int) ((((x1/255.0)*hue.getBlue())*(y1/255.0))+((y1/255.0)*(255-((x1/255.0)*hue.getBlue())))*((255-x1)/255.0)));
		cpx=(int) x;
		cpy=(int) y;
		return out;
	}
	
	public Color getHueSelector(double x,double y) {
		Color out=new Color(0,0,0);
		int y1=(int) (y-(startY+hy));
		y1=y1*6;
		int i2=(int) (y1/255.0);
		y1=y1-(255*i2);
		if(i2==0) {
			out=(new Color(255,y1,0));
		}else if(i2==1) {
			out=(new Color(255-y1,255,0));
		}else if(i2==2) {
			out=(new Color(0,255,y1));
		}else if(i2==3) {
			out=(new Color(0,255-y1,255));
		}else if(i2==4) {
			out=(new Color(y1,0,255));
		}else if(i2==5) {
			out=(new Color(255,0,255-y1));
		}
		hpx=(int) x;
		hpy=(int) y;
		return out;
	}
	
	public void changeHue(Color c) {
		hue=c;
		Color out=getColorSelector(cpx,cpy);
		changeColor(out);
	}
	
	public void changeColor(Color c) {
		selected=c;
	}
}
