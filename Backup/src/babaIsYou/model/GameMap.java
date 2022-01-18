package babaIsYou.model;
import java.util.ArrayList;
import java.util.Objects;

/*
 * A map is a grid of MapBox. We can put items on the grid to make a level.
 * To add rules on the map go check the RulesChanger class.
 */

public class GameMap {

	final public static int X_MAX = 15; //Grid's width
	final public  static int Y_MAX = 15; //Grid's heigth
	
	private Item[][] grid = new Item[X_MAX][Y_MAX];
	
	private ArrayList<Item> itemsOnMap = new ArrayList<Item>(); //Here we put all items that we put on the grid. 
	//This reduce the complexity because instead of scanning all the grid to find an item, just iterate over this list
	
	private ArrayList<Item> itemsOnBackground = new ArrayList<Item>(); //Here we have all items that are on background
	//The grid can have 1 items per box so when a second item walks on the first, the first will be added to this list
	
	private GameState gameState = GameState.NONE; //Here we inform if the player walked in WIN or DEFEAT item.
	
	
	public GameMap() {
		this.initialiseMap();
	}
	
	//We initialise the map with items that have no type and no physics
	private void initialiseMap() {
		for(int y = 0; y < GameMap.Y_MAX; y++) {
			for(int x = 0; x < GameMap.X_MAX; x++) {
				Item item = new Item(ItemType.None, ItemPhysics.None);
				this.addToMap(item, new Point(x,y));
			}
		}
	}
	
	private boolean isPointValid(Point point) {
		return (point.x() < GameMap.X_MAX && point.x() >= 0) && (point.y() < GameMap.Y_MAX && point.y() >= 0);
	}
	
	//Here we add an item to the grid and to the itemsOnMap list
	public void addToMap(Item item, Point point) {
		Objects.requireNonNull(item);
		if (!isPointValid(point)) throw new IllegalArgumentException("The point is out of bound\n");
		
		item.setPoint(point); //Update item's point
		
		this.grid[point.x()][point.y()] = item; //We put the MapBox on the map
		
		if(item.getType() != ItemType.None) {
			itemsOnMap.add(item);
		}
	}
	
	/*The most complex method in this project, it is recursive.
	 * Returns a boolean to continue or stop a recursive call. This is needed for example to push multiple items.
	 * 
	*/
	
	public boolean move(Item item, Point point) {
		if(!this.isPointValid(point)) return false;
		//System.out.println(grid[3][1].getItem().getPhysic());
		Item destItem = grid[point.x()][point.y()];
		
		switch (destItem.getPhysic()) {
		case None:
			return MovementManager.moveOnNone(item, point, this);
		case Push:
			return MovementManager.moveOnPush(item, destItem, point, this);
		case Stop:
			return false;
		case Sink:
			return MovementManager.moveOnSink(item, point, this);
		case Hot:
			return MovementManager.moveOnHot(item, destItem, point, this);
		case Win:
			if(MovementManager.moveOnWin(item, destItem, point, this)) {
				this.gameState = GameState.WIN;
			}
			return true;
		case Defeat:
			MovementManager.moveOnDefeat(item, destItem, point, this);
			return true;
		case Background:
			return MovementManager.moveOnBackgroud(item, destItem, point, this);
		case Melt:
			return MovementManager.moveOnMelt(item, destItem, point, this);
		case Jump:
			return MovementManager.moveOnJump(item, destItem, point, this);
		default:
			return false;
		}
	}
	
	//Put items on background on the map if possible using the item point field
	public void retrieveItemsOnBackground() {
		for (int i = 0; i < itemsOnBackground.size(); i++) {
			Item item = itemsOnBackground.get(i);
			if(this.grid[item.getPoint().x()][item.getPoint().y()].getPhysic() == ItemPhysics.None) {
				this.addToMap(item, new Point(item.getPoint().x(),item.getPoint().y()));
				itemsOnBackground.remove(i);
			}
		}
	}
	
	public Item getItem(Point point){
		Objects.requireNonNull(point);
		if (!isPointValid(point)) return null;
		return this.grid[point.x()][point.y()];
	}
	
	public ArrayList<Item> getItemsOnMap(){
		return this.itemsOnMap;
	}
	
	public ArrayList<Item> getItemsOnBackground(){
		return this.itemsOnBackground;
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	/*
	public void setGameState(GameState state) {
		Objects.requireNonNull(state);
		this.gameState = state;
	}
	*/
	
	public Item[][] getGrid(){
		return grid;
	}
	
	public String toTextFormat() {
		StringBuilder builder = new StringBuilder();
		builder.append(SaveManager.MAP_INFO_IDENTIFIER)
		.append(SaveManager.SEPARATOR).append(X_MAX)
		.append(SaveManager.SEPARATOR).append(Y_MAX)
		.append("\n");
		
		for(int y = 0; y < GameMap.Y_MAX; y++) {
			for(int x = 0; x < GameMap.X_MAX; x++) {
				Item item = grid[x][y];
				if(item.getType() != ItemType.None) {
					builder.append(item.toTextFormat()).append("\n");
				}
			}
		}
		return builder.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(int y = 0; y < GameMap.Y_MAX; y++) {
			for(int x = 0; x < GameMap.X_MAX; x++) {
				stringBuilder.append(grid[x][y].toString()).append("  ");
			}
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}
}
