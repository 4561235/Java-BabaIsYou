package babaIsYou.model;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;

/*
 * Here we can add rules to a map.
 * This class will make all the magic happen.
 */

public class RulesChanger {
	private final GameMap map;

	//Here we add word to the wordlist
	private static final ArrayList<ItemType> wordList = new ArrayList<>();

	//Here we add the translation of a word to a type
	private static final Hashtable<ItemType, ItemType> wordToType = new Hashtable<>();

	//Here we add the translation of a word to a physic
	private static final Hashtable<ItemType, ItemPhysics> wordToPhysic = new Hashtable<>();

	public RulesChanger(GameMap map) {
		Objects.requireNonNull(map);
		this.map = map;

		wordList.add(ItemType.BabaWord);
		wordList.add(ItemType.FlagWord);
		wordList.add(ItemType.WallWord);
		wordList.add(ItemType.WaterWord);
		wordList.add(ItemType.SkullWord);
		wordList.add(ItemType.LavaWord);
		wordList.add(ItemType.RockWord);
		wordList.add(ItemType.YouWord);
		wordList.add(ItemType.WinWord);
		wordList.add(ItemType.StopWord);
		wordList.add(ItemType.PushWord);
		wordList.add(ItemType.MeltWord);
		wordList.add(ItemType.HotWord);
		wordList.add(ItemType.DefeatWord);
		wordList.add(ItemType.SinkWord);
		wordList.add(ItemType.IsWord);
		wordList.add(ItemType.JumpWord);
		wordList.add(ItemType.SpringWord);

		wordToType.put(ItemType.BabaWord,ItemType.Baba);
		wordToType.put(ItemType.FlagWord,ItemType.Flag);
		wordToType.put(ItemType.WallWord,ItemType.Wall);
		wordToType.put(ItemType.WaterWord,ItemType.Water);
		wordToType.put(ItemType.SkullWord,ItemType.Skull);
		wordToType.put(ItemType.LavaWord,ItemType.Lava);
		wordToType.put(ItemType.RockWord,ItemType.Rock);
		wordToType.put(ItemType.SpringWord,ItemType.Spring);

		wordToPhysic.put(ItemType.WinWord,ItemPhysics.Win);
		wordToPhysic.put(ItemType.StopWord,ItemPhysics.Stop);
		wordToPhysic.put(ItemType.PushWord,ItemPhysics.Push);
		wordToPhysic.put(ItemType.MeltWord,ItemPhysics.Melt);
		wordToPhysic.put(ItemType.SinkWord,ItemPhysics.Sink);
		wordToPhysic.put(ItemType.HotWord,ItemPhysics.Hot);
		wordToPhysic.put(ItemType.DefeatWord,ItemPhysics.Defeat);
		wordToPhysic.put(ItemType.JumpWord,ItemPhysics.Jump);
	}

	//Return all "Is" operators
	private ArrayList<Item> findIsOperators(){
		ArrayList<Item> result = new ArrayList<>();
		ArrayList<Item> items = map.getItemsOnMap();

		for (int i = 0; i < items.size(); i++) {
			if(items.get(i).getType() == ItemType.IsWord) {
				result.add(items.get(i));
			}
		}
		return result;
	}

	//Here we can change an item into another for example first: BABA second: WALL, the result will be BABA IS WALL
	public void changeRuleTypeToType(ItemType wdfirst, ItemType wdsecond) {
		if(wdfirst == null || wdsecond == null || !wordList.contains(wdfirst) || !wordList.contains(wdsecond)) return; //Check if the words are in the words list
		if(wdsecond == ItemType.YouWord) this.setItemsAsPlayable(wordToType.get(wdfirst));//If the second word is "YOU" then we set first type as playable

		if(wordToType.get(wdsecond) == null) { //We check if the second word represent a physic and no a type
			this.changeRuleTypeToPhysics(wdfirst, wordToPhysic.get(wdsecond));
			return;
		}

		ArrayList<Item> items = map.getItemsOnMap();
		for (Item item : items) {
			if(item.getType() == wordToType.get(wdfirst)) {
				item.setType(wordToType.get(wdsecond));
			}
		}
	}

	//Here we can change an item's physic for example: first: WALL second: PUSH, the result will be WALL IS PUSH
	public void changeRuleTypeToPhysics(ItemType first, ItemPhysics second) {
		if(first == null || second == null || !wordList.contains(first)) return;
		ArrayList<Item> items = map.getItemsOnMap();
		for (Item item : items) {
			if(item.getType() == wordToType.get(first)) {
				item.setPhysic(second);
			}
		}
	}

	/*Method that we call every move in the main game loop
	 * This will set all items on the map to background and no playable
	 * After this, it will update all the items according to words on the map
	*/
	public void updateRules() {

		this.setEverythingAsBackground();
		this.setEverythingAsNotPlayable();

		ArrayList<Item> operatorsIs = this.findIsOperators();

		for (Item is : operatorsIs) {
			Item nord = map.getItem(is.getPoint().add(new Point(0,-1)));
			Item south = map.getItem(is.getPoint().add(new Point(0,1)));
			Item west = map.getItem(is.getPoint().add(new Point(-1,0)));
			Item east = map.getItem(is.getPoint().add(new Point(1,0)));
			if(nord != null && south !=null) changeRuleTypeToType(nord.getType(), south.getType());
			if(west != null && east !=null) changeRuleTypeToType(west.getType(), east.getType());
		}
		/*
		if(this.checkIfPlayableIsWin()) {
			this.map.setGameState(GameState.WIN);
			return;
		}
		*/
	}
	/*
	public boolean checkIfPlayableIsWin() {
		ArrayList<Item> playableItems = new ArrayList<Item>();
		for (Item item : playableItems) {
			if(item.getPhysic() == ItemPhysics.Win && item.playable) {
				return true;
			}
		}
		return false;
	}
	*/

	public ArrayList<Item> getPlayableItems(){
		ArrayList<Item> playableItems = new ArrayList<>();
		ArrayList<Item> items = map.getItemsOnMap();
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			if(item.playable) {
				playableItems.add(item);
			}
		}
		return playableItems;
	}

	private void setEverythingAsBackground() {
		ArrayList<Item> items = map.getItemsOnMap();
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			if(!wordList.contains(item.getType())) {
				item.setPhysic(ItemPhysics.Background);
			}
		}
	}

	private void setEverythingAsNotPlayable() {
		ArrayList<Item> items = map.getItemsOnMap();
		for (Item item2 : items) {
			Item item = item2;
			item.playable = false;
		}
	}

	private void setItemsAsPlayable(ItemType type) {
		ArrayList<Item> items = map.getItemsOnMap();
		for (Item item2 : items) {
			Item item = item2;
			if(item.getType() == type) {
				item.playable = true;
				item.setPhysic(ItemPhysics.Stop);
			}
		}
	}

}
