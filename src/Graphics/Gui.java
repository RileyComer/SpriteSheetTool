package Graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Gui extends JFrame{ 
	private Display board;
	private Color[][] sheet, copy;
	private int ow, oh, sw, sh, iw, ih,currentSpriteX,currentSpriteY;
	private boolean invert;
	
	public Gui(int ow,int oh,int sw,int sh,int iw,int ih,File edit,String name) {
		super("Sprite Sheet Dev Tool");
		
		this.ow=ow;
		this.oh=oh;
		this.sw=sw;
		this.sh=sh;
		this.iw=iw;
		this.ih=ih;
		
		sheet=new Color[iw*sw][ih*sh];
		
		if(edit!=null) {
			BufferedImage img=null;
			Color c;
			try {
				img=ImageIO.read(edit);
				for(int shY=0;shY<sh;shY++) {
					for(int shX=0;shX<sw;shX++) {
						for(int y=0;y<ih;y++) {
							for(int x=0;x<iw;x++) {
								c=new Color(img.getRGB((ow/iw)*(x+(iw*shX)), (oh/ih)*(y+(ih*shY))));
								if(!(c.getRed()==255&&c.getBlue()==255&&c.getGreen()==0)) {
									sheet[x+(iw*shX)][y+(ih*shY)]=c;
								}
							}
						}
					}
				}
			}catch(IOException e){
			}
		}
		
		board =new Display(iw,ih);
		add(board,BorderLayout.CENTER);
		
		displayAnim();
		copy=new Color[iw][ih];
		load();
		
		//Mouse Handler
		HandlerClass handler=new HandlerClass();
		board.addMouseListener(handler);
		board.addMouseMotionListener(handler);
		
		//Exit key
		addKeyListener(
				new KeyAdapter() {
					public void keyPressed(KeyEvent ke) {  // handler
						if(ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
							System.exit(0);
							
						}else if(ke.getKeyCode() == KeyEvent.VK_S) {
							BufferedImage image=new BufferedImage(ow*sw,oh*sh,BufferedImage.TYPE_INT_RGB);
							Graphics2D g=image.createGraphics();
							save();
							for(int y=0;y<ih*sh;y++) {
								for(int x=0;x<iw*sw;x++) {
									if(sheet[x][y]!=null) {
										g.setColor(sheet[x][y]);
										g.fillRect(x*(ow/iw), y*(oh/ih), (ow/iw),(oh/ih));
									}else {
										g.setColor(new Color(255,0,255));
										g.fillRect(x*(ow/iw), y*(oh/ih), (ow/iw),(oh/ih));
									}
								}
							}
							g.dispose();
							File file=new File("C:\\Users\\Riley\\Desktop\\Sprites\\"+name+".png");
							try {
								ImageIO.write(image,"png",file);
							} catch (IOException e) {
								System.out.println("Not Saved");
							}
							
						}else if(ke.getKeyCode() == KeyEvent.VK_C) {
							copy=copyList(board.getSprite());
							
						}else if(ke.getKeyCode() == KeyEvent.VK_V) {
							Color[][] c=copyList(copy);
							for(int y=0;y<copy[0].length;y++) {
								for(int x=0;x<copy.length;x++) {
									if(c[x][y]==null) {
										c[x][y]=board.getPixle(x, y);
									}
								}
							}
							board.setSprite(c);
							
						}else if(ke.getKeyCode() == KeyEvent.VK_M) {
							Color[][] in=copyList(board.getSprite());
							Color[][] out=new Color[iw][ih];
							for(int y=0;y<ih;y++) {
								for(int x=0;x<ih;x++) {
									out[x][y]=board.copyColor(in[iw-x-1][y]);
								}
							}
							board.setSprite(out);
							
						}else if(ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
							board.setSprite(new Color[iw][ih]);
							
						}else if(ke.getKeyCode() == KeyEvent.VK_SHIFT) {
							invert=true;
							
						}else if(ke.getKeyCode() == KeyEvent.VK_RIGHT) {
							save();
							currentSpriteX++;
							if(currentSpriteX>sw-1) {
								currentSpriteX=0;
							}
							load();
							
						}else if(ke.getKeyCode() == KeyEvent.VK_LEFT) {
							save();
							currentSpriteX--;
							if(currentSpriteX<0) {
								currentSpriteX=sw-1;
							}
							load();
							
						}else if(ke.getKeyCode() == KeyEvent.VK_UP) {
							save();
							currentSpriteY--;
							if(currentSpriteY<0) {
								currentSpriteY=sh-1;
							}
							load();
							
						}else if(ke.getKeyCode() == KeyEvent.VK_DOWN) {
							save();
							currentSpriteY++;
							if(currentSpriteY>sh-1) {
								currentSpriteY=0;
							}
							load();
						}
						
					} public void keyReleased(KeyEvent ke) {  // handler
						if(ke.getKeyCode() == KeyEvent.VK_SHIFT) {
							invert=false;
						}
					} 
				}
		);
	}
	
	public void redraw() {
		board.redraw();
	}
	
	public void save() {
		Color[][] out=copyList(board.getSprite());
		for(int y=0;y<ih;y++) {
			for(int x=0;x<iw;x++) {
				sheet[x+(currentSpriteX*iw)][y+(currentSpriteY*ih)]=board.copyColor(out[x][y]);
			}
		}
	}
	
	public void load() {
		Color [][]out=new Color[iw][ih];
		for(int y=0;y<ih;y++) {
			for(int x=0;x<iw;x++) {
				out[x][y]=board.copyColor(sheet[x+(currentSpriteX*iw)][y+(currentSpriteY*ih)]);
			}
		}
		board.setSpriteXY(currentSpriteX,currentSpriteY);
		displayAnim();
		board.setSprite(copyList(out));
	}
	
	public void displayAnim() {
		Color[][] out=new Color[iw*sw][ih];
		for(int y=currentSpriteY*ih;y<currentSpriteY*ih+ih;y++) {
			for(int x=0*iw;x<iw*sw;x++) {
				out[x][y-(currentSpriteY*ih)]=board.copyColor(sheet[x][y]);
			}
		}
		board.setAnim(out);
	}
	
	public Color[][] copyList(Color[][] c) {
		Color[][] out=new Color[iw][ih];
		for(int y=0;y<ih;y++) {
			for(int x=0;x<iw;x++) {
				out[x][y]=board.copyColor(c[x][y]);
			}
		}
		return out;
	}
	
	private class HandlerClass implements MouseListener,MouseMotionListener{

		public void mouseClicked(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent e) {
			
		}

		public void mouseExited(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			
		}

		public void mouseReleased(MouseEvent e) {
			double x=e.getX();
			double y=e.getY(); 
			int out=board.getLocation(x, y);
			if(out!=-1) {
				int y1=out/iw;
				int x1=out%iw;
				if(e.getButton()==1) {
					if(!invert) {
						board.addPixle(x1,y1);
					}else {
						board.removePixle(x1,y1);
					}
				}else if(e.getButton()==3){
					board.changeColor(board.getPixle(x1,y1));
				}
			}else {
				out=board.getTool(x, y);
				if(out==1) {
					board.changeColor(board.getColorSelector(x,y));
				}else if(out==2) {
					board.changeHue(board.getHueSelector(x,y));
				}
			}
		}
		
		public void mouseDragged(MouseEvent e) {
			double x=e.getX();
			double y=e.getY(); 
			int out=board.getLocation(x, y);
			if(out!=-1) {
				int y1=out/iw;
				int x1=out%iw;
				if(e.getButton()==0) {
					if(!invert) {
						board.addPixle(x1,y1);
					}else {
						board.removePixle(x1,y1);
					}
				}
			}else {
				out=board.getTool(x, y);
				if(out==1) {
					board.changeColor(board.getColorSelector(x,y));
				}else if(out==2) {
					board.changeHue(board.getHueSelector(x,y));
				}
			}
		}

		
		public void mouseMoved(MouseEvent e) {
			
		}
	}		
}
