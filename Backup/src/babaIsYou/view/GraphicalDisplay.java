package babaIsYou.view;
import java.awt.Color;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import babaIsYou.model.GameMap;
import babaIsYou.model.Item;
import babaIsYou.model.ItemType;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.ScreenInfo;

public class GraphicalDisplay {
	
	private  ApplicationContext context;
	private float screenWidth;
	private float screenHeight;
	private float gridWidth;
	private float gridHeight;
	
	private final Hashtable<ItemType, BufferedImage> itemImages = new Hashtable<ItemType, BufferedImage>();
	
	public GraphicalDisplay(){
		Application.run(Color.BLACK, context -> {
			this.context = context;
		    ScreenInfo screenInfo = context.getScreenInfo();
		    this.screenWidth = screenInfo.getWidth();
		    this.screenHeight = screenInfo.getHeight();
		    System.out.println("size of the screen (" + screenWidth + " x " + screenHeight + ")");
		});
		
		itemImages.put(ItemType.Baba, this.loadImage("Images/baba.png"));
		itemImages.put(ItemType.Flag,this.loadImage("Images/flag.png"));
		itemImages.put(ItemType.Rock,this.loadImage("Images/rock.png"));
		itemImages.put(ItemType.Wall,this.loadImage("Images/wall.png"));
		itemImages.put(ItemType.Water,this.loadImage("Images/water.png"));
		itemImages.put(ItemType.Skull,this.loadImage("Images/skull.png"));
		itemImages.put(ItemType.Lava,this.loadImage("Images/lava.png"));
		itemImages.put(ItemType.Spring,this.loadImage("Images/spring.png"));
		itemImages.put(ItemType.WDBaba,this.loadImage("Images/babaWord.png"));
		itemImages.put(ItemType.WDFlag,this.loadImage("Images/flagWord.png"));
		itemImages.put(ItemType.WDWall,this.loadImage("Images/wallWord.png"));
		itemImages.put(ItemType.WDWater,this.loadImage("Images/waterWord.png"));
		itemImages.put(ItemType.WDSkull,this.loadImage("Images/skullWord.png"));
		itemImages.put(ItemType.WDLava,this.loadImage("Images/lavaWord.png"));
		itemImages.put(ItemType.WDRock,this.loadImage("Images/rockWord.png"));
		itemImages.put(ItemType.WDYou,this.loadImage("Images/youWord.png"));
		itemImages.put(ItemType.WDWin,this.loadImage("Images/winWord.png"));
		itemImages.put(ItemType.WDStop,this.loadImage("Images/stopWord.png"));
		itemImages.put(ItemType.WDPush,this.loadImage("Images/pushWord.png"));
		itemImages.put(ItemType.WDMelt,this.loadImage("Images/meltWord.png"));
		itemImages.put(ItemType.WDHot,this.loadImage("Images/hotWord.png"));
		itemImages.put(ItemType.WDDefeat,this.loadImage("Images/defeatWord.png"));
		itemImages.put(ItemType.WDSink,this.loadImage("Images/sinkWord.png"));
		itemImages.put(ItemType.WDIs,this.loadImage("Images/isWord.png"));
		itemImages.put(ItemType.WDJump,this.loadImage("Images/jumpWord.png"));
		itemImages.put(ItemType.WDSpring,this.loadImage("Images/springWord.png"));
		
	}
	
	public void displayMap(GameMap map) {
		this.cleanScreen();
		this.gridWidth = this.screenWidth / GameMap.X_MAX;
		this.gridHeight = this.screenHeight / GameMap.Y_MAX;
		this.renderGameMapBackground(map);
		this.renderGameMapGrid(map);
	}
	
	
	private void renderGameMapBackground(GameMap map) {
		ArrayList<Item> background = map.getItemsOnBackground();
		for (Item item : background) {
			
			final int yCopy = item.getPoint().y();
			final int xCopy = item.getPoint().x();
			if(item.getType() != ItemType.None) {
				context.renderFrame(graphics -> {
					BufferedImage img = this.itemImages.get(item.getType());
					Image scaledImg = img.getScaledInstance((int) this.gridWidth, (int) this.gridHeight, Image.SCALE_SMOOTH);
					graphics.drawImage(scaledImg, new AffineTransform(1f,0f,0f,1f,(this.gridWidth*xCopy),(this.gridHeight*yCopy)), null);
					
			    });
			}
		}
	}
	private void renderGameMapGrid(GameMap map) {
		Item[][] grid = map.getGrid();
		for(int y = 0; y < GameMap.Y_MAX; y++) {
			for(int x = 0; x < GameMap.X_MAX; x++) {
				Item item = grid[x][y];
				final int yCopy = y;
				final int xCopy = x;
				
				if(item.getType() != ItemType.None) {
					context.renderFrame(graphics -> {
						BufferedImage img = this.itemImages.get(item.getType());
						Image scaledImg = img.getScaledInstance((int) this.gridWidth, (int) this.gridHeight, Image.SCALE_SMOOTH);
						graphics.drawImage(scaledImg, new AffineTransform(1f,0f,0f,1f,(this.gridWidth*xCopy),(this.gridHeight*yCopy)), null);
						
				    });
				}	
			}
		}
	}
	
	private void cleanScreen(){
		context.renderFrame(graphics -> {
	        graphics.setColor(Color.black);
	        graphics.fill(new  Rectangle2D.Float(0, 0, this.screenWidth, this.screenHeight));
	      });
	}
	
	public Event getEvent() {
		Event event = context.pollOrWaitEvent(10);
		return event;
	}
	
	private BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
