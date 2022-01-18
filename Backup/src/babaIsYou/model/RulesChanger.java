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
	private static final ArrayList<ItemType> wordList = new ArrayList<ItemType>();
	
	//Here we add the translation of a word to a type
	private static final Hashtable<ItemType, ItemType> wordToType = new Hashtable<ItemType, ItemType>();
	
	//Here we add the translation of a word to a physic
	private static final Hashtable<ItemType, ItemPhysics> wordToPhysic = new Hashtable<ItemType, ItemPhysics>();
	
	public RulesChanger(GameMap map) {
		Objects.requireNonNull(map);
		this.map = map;
		
		wordList.add(ItemType.WDBaba);
		wordList.add(ItemType.WDFlag);
		wordList.add(ItemType.WDWall);
		wordList.add(ItemType.WDWater);
		wordList.add(ItemType.WDSkull);
		wordList.add(ItemType.WDLava);
		wordList.add(ItemType.WDRock);
		wordList.add(ItemType.WDYou);
		wordList.add(ItemType.WDWin);
		wordList.add(ItemType.WDStop);
		wordList.add(ItemType.WDPush);
		wordList.add(ItemType.WDMelt);
		wordList.add(ItemType.WDHot);
		wordList.add(ItemType.WDDefeat);
		wordList.add(ItemType.WDSink);
		wordList.add(ItemType.WDIs);
		wordList.add(ItemType.WDJump);
		wordList.add(ItemType.WDSpring);
		
		wordToType.put(ItemType.WDBaba,ItemType.Baba);
		wordToType.put(ItemType.WDFlag,ItemType.Flag);
		wordToType.put(ItemType.WDWall,ItemType.Wall);
		wordToType.put(ItemType.WDWater,ItemType.Water);
		wordToType.put(ItemType.WDSkull,ItemType.Skull);
		wordToType.put(ItemType.WDLava,ItemType.Lava);
		wordToType.put(ItemType.WDRock,ItemType.Rock);
		wordToType.put(ItemType.WDSpring,ItemType.Spring);
		
		wordToPhysic.put(ItemType.WDWin,ItemPhysics.Win);
		wordToPhysic.put(ItemType.WDStop,ItemPhysics.Stop);
		wordToPhysic.put(ItemType.WDPush,ItemPhysics.Push);
		wordToPhysic.put(ItemType.WDMelt,ItemPhysics.Melt);
		wordToPhysic.put(ItemType.WDSink,ItemPhysics.Sink);
		wordToPhysic.put(ItemType.WDHot,ItemPhysics.Hot);
		wordToPhysic.put(ItemType.WDDefeat,ItemPhysics.Defeat);
		wordToPhysic.put(ItemType.WDJump,ItemPhysics.Jump);
	}
	
	//Return all "Is" operators
	private ArrayList<Item> findIsOperators(){
		ArrayList<Item> result = new ArrayList<Item>();
		ArrayList<Item> items = map.getItemsOnMap();
		
		for (int i = 0; i < items.size(); i++) {
			if(items.get(i).getType() == ItemType.WDIs) {
				result.add(items.get(i));
			}
		}
		return result;
	}
	
	//Here we can change an item into another for example first: BABA second: WALL, the result will be BABA IS WALL
	public void changeRuleTypeToType(ItemType wdfirst, ItemType wdsecond) {
		if(wdfirst == null || wdsecond == null) return;
		if(!wordList.contains(wdfirst) || !wordList.contains(wdsecond)) return; //Check if the words are in the words list
		if(wdsecond == ItemType.WDYou) this.setItemsAsPlayable(wordToType.get(wdfirst));//If the second word is "YOU" then we set first type as playable
		
		if(wordToType.get(wdsecond) == null) { //We check if the second word represent a physic and no a type
			this.changeRuleTypeToPhysics(wdfirst, wordToPhysic.get(wdsecond));
			return;
		}
		
		ArrayList<Item> items = map.getItemsOnMap();
		for (int i = 0; i < items.size(); i++) {
			if(items.get(i).getType() == wordToType.get(wdfirst)) {
				items.get(i).setType(wordToType.get(wdsecond));
			}
		}
	}
	
	//Here we can change an item's physic for example: first: WALL second: PUSH, the result will be WALL IS PUSH
	public void changeRuleTypeToPhysics(ItemType first, ItemPhysics second) {
		if(first == null || second == null) return;
		if(!wordList.contains(first)) return;
		ArrayList<Item> items = map.getItemsOnMap();
		for (int i = 0; i < items.size(); i++) {
			if(items.get(i).getType() == wordToType.get(first)) {
				items.get(i).setPhysic(second);
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
		
		for (int i = 0; i < operatorsIs.size(); i++) {
			Item is = operatorsIs.get(i);
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
		ArrayList<Item> playableItems = new ArrayList<Item>();
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
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			item.playable = false;
		}
	}
	
	private void setItemsAsPlayable(ItemType type) {
		ArrayList<Item> items = map.getItemsOnMap();
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			if(item.getType() == type) {
				item.playable = true;
				item.setPhysic(ItemPhysics.Stop);
			}
		}
	}
	
}
