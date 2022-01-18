package babaIsYou.model;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import babaIsYou.controller.GameLoop;

public class CommandLineParser {
	public static String COMMAND_EXECUTE = "--execute";
	public static String COMMAND_LEVEL = "--level";
	public static String COMMAND_FOLDER_LEVELS = "--levels";


	public static boolean parseLevelCommand(String str, GameLoop gameLoop, Scanner scanner) {
		String[] array = str.split(" ");
		if(array.length < 2 || array.length > 2) {
			System.out.println("This command takes 1 argument");
			return false;
		}
		Path saveFilePath = Paths.get(array[1]);
		try {
			GameMap map = SaveManager.loadMapFromFile(saveFilePath);
			gameLoop.play(map,scanner);
			return true;
		} catch (IOException e) {
			System.out.println("Map don't exist");
		}
		return false;
	}

	public static boolean parseFolderLevelsCommand(String str, GameLoop gameLoop, Scanner scanner) {
		//GameLoop test = new GameLoop();
		String[] array = str.split(" ");
		if(array.length < 2 || array.length > 2) {
			System.out.println("This command takes 1 argument");
			return false;
		}
		File directoryPath = new File(array[1]);
		String contents[] = directoryPath.list();

		if(contents == null) {
			System.out.println("Folder don't exist");
			return false;
		}

		for(int i=0; i < contents.length; i++) {
	         Path filePath = Paths.get(array[1] +"/" +contents[i]);
	         try {
				GameMap map = SaveManager.loadMapFromFile(filePath);
				System.out.println("MAPNB: " +(i+1));
				gameLoop.play(map,scanner);

			} catch (IOException e) {
				e.printStackTrace();
			}
	      }
		return true;
	}

	public static boolean parseExecuteCommand(String str, RulesChanger rulesChanger) {
		String[] array = str.split(" ");
		if(array.length < 4 || array.length > 4) {
			System.out.println("This command takes 3 argument");
			return false;
		}
		String lower1 = array[1].toLowerCase();
		String lower2 = array[3].toLowerCase();
		String cap1 = lower1.substring(0, 1).toUpperCase() + lower1.substring(1);
		String cap2 = lower2.substring(0, 1).toUpperCase() + lower2.substring(1);
		cap1 = cap1 + "Word";
		cap2 = cap2 + "Word";
		ItemType type1 = ItemType.valueOf(cap1);
		ItemType type2 = ItemType.valueOf(cap2);

		System.out.println(type1);
		System.out.println(type2);
		rulesChanger.changeRuleTypeToType(type1, type2);


		return true;
	}

}
