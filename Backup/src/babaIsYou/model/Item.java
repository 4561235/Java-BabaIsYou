package babaIsYou.model;
import java.util.Objects;

/* An item is an object that we put on the map.
 * It have a type like for example: Skull or Water and a physic like Hot or Push.
 */
public class Item {
	
	private ItemType type;
	private ItemPhysics physic;
	public boolean playable; //We can use this field to see if the item can be playable by the player
	private Point point; //The point to give us the information where the item is placed on the map
	
	//To make an item we need the type and physic, the rest of fields will be initialised by setters when added to a map
	/*
	 * @param type item's type like Baba or Water
	 * @param type item's physic like Push or Stop
	 */
	public Item(ItemType type, ItemPhysics physic) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(physic);
		this.type = type;
		this.physic = physic;
		this.point = null;
	}

	
	//Use map's move() method to move himself in a specific direction
	public boolean move(Direction direction, GameMap map) {
		Objects.requireNonNull(direction);
		switch (direction){
			case DOWN:
				return map.move(this, this.getPoint().add(new Point(0,1)));
			case LEFT:
				return map.move(this, this.getPoint().add(new Point(-1,0)));
			case RIGHT:
				return map.move(this, this.getPoint().add(new Point(1,0)));
			case UP:
				return map.move(this, this.getPoint().add(new Point(0,-1)));
			default:
				return false;
		}		
	}
	
	//Instead of using the Direction enum, we can use a point to specify in which direction we want the item to move
	public boolean move(Point point, GameMap map){
		Objects.requireNonNull(point);
		if(point.equals(new Point(0,1))) {
			return this.move(Direction.DOWN,map);
		}
		else if(point.equals(new Point(-1,0))) {
			return this.move(Direction.LEFT,map);
		}
		else if(point.equals(new Point(1,0))) {
			return this.move(Direction.RIGHT,map);
		}
		else if(point.equals(new Point(0,-1))) {
			return this.move(Direction.UP,map);
		}
		return false;
	}
	
	public void setPoint(Point point) {
		Objects.requireNonNull(point);
		this.point = point.clone();
	}
	
	
	public void setType(ItemType type) {
		this.type = type;
	}
	
	public void setPhysic(ItemPhysics physic) {
		this.physic = physic;
	}
	
	public ItemType getType() {
		return this.type;
	}
	
	public ItemPhysics getPhysic() {
		return this.physic;
	}
	
	public Point getPoint() {
		return this.point;
	}
	
	public String toTextFormat() {
		StringBuilder builder = new StringBuilder();
		builder.append(SaveManager.ITEM_IDENTIFIER)
		.append(SaveManager.SEPARATOR)
		.append(this.type.name())
		.append(SaveManager.SEPARATOR)
		.append(this.physic.name())
		.append(SaveManager.SEPARATOR)
		.append(this.point.x())
		.append(SaveManager.SEPARATOR)
		.append(this.point.y());
		return builder.toString();
	}
	
	@Override
	public String toString() {
		if(type == ItemType.None) {
			return "[       ]";
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("[" +type);
		
		for (int i = builder.toString().length(); i <= 7; i++) {
			builder.append(" ");
		}
		return builder.append("]").toString();
	}
	
}
