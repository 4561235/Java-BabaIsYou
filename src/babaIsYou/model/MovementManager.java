package babaIsYou.model;

public class MovementManager {

	public static boolean moveOnNone(Item item, Point point, GameMap map) {
		Item[][] grid = map.getGrid();
		grid[point.x()][point.y()] = grid[item.getPoint().x()][item.getPoint().y()];
		map.addToMap(new Item(ItemType.None,ItemPhysics.None), new Point(item.getPoint().x(),item.getPoint().y()));
		item.setPoint(point.clone());

		map.retrieveItemsOnBackground(); //After the move we check if we can put back items on background on the grid
		return true;
	}

	public static boolean moveOnPush(Item item, Item destItem, Point point, GameMap map) {
		Point direction = point.sub(item.getPoint());
		if(destItem.move(direction,map)) {
			item.move(direction,map);
			return true;
		}
		return false;
	}

	public static boolean moveOnSink(Item item, Point point, GameMap map) {
		Item[][] grid = map.getGrid();
		Item destItem = grid[point.x()][point.y()];
		map.getItemsOnMap().remove(destItem);
		map.getItemsOnMap().remove(item);
		map.addToMap(new Item(ItemType.None,ItemPhysics.None), new Point(item.getPoint().x(),item.getPoint().y()));
		map.addToMap(new Item(ItemType.None,ItemPhysics.None), new Point(point.x(),point.y()));
		return true;
	}

	public static boolean moveOnHot(Item item, Item destItem ,Point point, GameMap map) {
		if(item.getPhysic() == ItemPhysics.Melt) {
			map.getItemsOnMap().remove(destItem);
			map.getItemsOnMap().remove(item);
			map.addToMap(new Item(ItemType.None,ItemPhysics.None), new Point(item.getPoint().x(),item.getPoint().y()));
			map.addToMap(new Item(ItemType.None,ItemPhysics.None), new Point(point.x(),point.y()));
		}
		else {
			map.getItemsOnBackground().add(destItem);
			map.addToMap(new Item(ItemType.None,ItemPhysics.None), new Point(destItem.getPoint().x(),destItem.getPoint().y()));
			Point direction = point.sub(item.getPoint());
			item.move(direction,map);
		}

		return true;
	}

	public static boolean moveOnBackgroud(Item item, Item destItem, Point point, GameMap map) {
		map.getItemsOnBackground().add(destItem);
		map.addToMap(new Item(ItemType.None,ItemPhysics.None), new Point(destItem.getPoint().x(),destItem.getPoint().y()));
		Point direction = point.sub(item.getPoint());
		item.move(direction,map);
		return true;
	}

	public static boolean moveOnWin(Item item, Item destItem, Point point, GameMap map) {
		if(item.playable) {
			return true;
		}
		else {
			map.getItemsOnBackground().add(destItem);
			map.addToMap(new Item(ItemType.None,ItemPhysics.None), new Point(destItem.getPoint().x(),destItem.getPoint().y()));
			Point direction = point.sub(item.getPoint());
			item.move(direction,map);
		}
		return false;
	}

	public static boolean moveOnMelt(Item item, Item destItem, Point point, GameMap map) {

		map.getItemsOnBackground().add(destItem);
		map.addToMap(new Item(ItemType.None,ItemPhysics.None), new Point(destItem.getPoint().x(),destItem.getPoint().y()));
		Point direction = point.sub(item.getPoint());
		item.move(direction,map);

		return true;
	}

	public static boolean moveOnDefeat(Item item, Item destItem, Point point, GameMap map) {
		if(item.playable) {
			map.getItemsOnMap().remove(item);
			map.addToMap(new Item(ItemType.None,ItemPhysics.None), new Point(item.getPoint().x(),item.getPoint().y()));
			return true;
		}
		else {
			map.getItemsOnBackground().add(destItem);
			map.addToMap(new Item(ItemType.None,ItemPhysics.None), new Point(destItem.getPoint().x(),destItem.getPoint().y()));
			Point direction = point.sub(item.getPoint());
			item.move(direction,map);
		}
		return false;
	}

	public static boolean moveOnJump(Item item, Item destItem, Point point, GameMap map) {
		Point direction = point.sub(item.getPoint());
		if(!map.move(item,direction.add(destItem.getPoint()).add(direction).add(direction))) {
			map.move(item,direction.add(destItem.getPoint()).add(direction));
		}
		return false;
	}
}
