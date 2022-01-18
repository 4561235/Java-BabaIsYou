package babaIsYou.controller;
import java.util.ArrayList;

import java.util.Objects;
import java.util.Scanner;

import babaIsYou.model.CommandLineParser;
import babaIsYou.model.Direction;
import babaIsYou.model.GameMap;
import babaIsYou.model.GameState;
import babaIsYou.model.Item;
import babaIsYou.model.RulesChanger;
import babaIsYou.view.GraphicalDisplay;

import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.KeyboardKey;

public class GameLoop {

	private GraphicalDisplay display;

	public GameLoop(GraphicalDisplay display) {
		Objects.requireNonNull(display);
		this.display = display;
	}

	public void play(GameMap map, Scanner scanner) {
		RulesChanger ruleschanger = new RulesChanger(map);
		ruleschanger.updateRules();

		display.displayMap(map);
		while(map.getGameState() == GameState.NONE) {

			ArrayList<Item> playableItems = ruleschanger.getPlayableItems();
			Event event = display.getEvent();

			if(event != null && event.getAction() == Action.KEY_PRESSED) {
				this.handleInput(playableItems, event, ruleschanger, map, scanner);
				display.displayMap(map);
			}

			if(playableItems.size() == 0) {
				System.out.println("YOU CAN'T PLAY ANYTHING");
				break;
			}
		}
		if(map.getGameState() == GameState.WIN) {
			System.out.println("YOU WIN!");
		}
		else {
			System.out.println("GAME OVER");
		}
	}

	private void handleInput(ArrayList<Item> playableItems, Event event, RulesChanger ruleschanger, GameMap map, Scanner scanner) {
		for (int i = 0; i < playableItems.size(); i++) {
			Item item = playableItems.get(i);

			if(event != null && event.getKey() == KeyboardKey.LEFT) {
				item.move(Direction.LEFT,map);
			}
			else if(event != null && event.getKey() == KeyboardKey.RIGHT) {
				item.move(Direction.RIGHT,map);
			}
			else if(event != null && event.getKey() == KeyboardKey.UP){
				item.move(Direction.UP,map);
			}
			else if(event != null && event.getKey() == KeyboardKey.DOWN) {
				item.move(Direction.DOWN,map);
			}
			else if(event != null && event.getKey() == KeyboardKey.C) {
				if(this.expectConsoleInput(scanner,ruleschanger)) return;
				break;
			}
			ruleschanger.updateRules();
		}
	}

	private boolean expectConsoleInput(Scanner scanner, RulesChanger ruleschanger) {
		System.out.println("Write your cheatcode here");
		String str = scanner.next();
		if(str.equals(CommandLineParser.COMMAND_LEVEL)) {
			if(CommandLineParser.parseLevelCommand(scanner.nextLine(), this, scanner))
				return true;
		}
		else if(str.equals(CommandLineParser.COMMAND_FOLDER_LEVELS)) {
			if(CommandLineParser.parseFolderLevelsCommand(scanner.nextLine(), this, scanner))
				return true;
		}
		else if(str.equals(CommandLineParser.COMMAND_EXECUTE)) {
			CommandLineParser.parseExecuteCommand(scanner.nextLine(), ruleschanger);
		}
		else {
			System.out.println("Command doesn't exist");
		}
		return false;
	}
}
