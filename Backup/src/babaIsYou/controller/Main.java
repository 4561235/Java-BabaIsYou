package babaIsYou.controller;

import java.util.Scanner;


import babaIsYou.model.CommandLineParser;
import babaIsYou.view.GraphicalDisplay;

public class Main {

	public static void main(String[] args) {
	
		GraphicalDisplay display = new GraphicalDisplay();
		GameLoop gameloop = new GameLoop(display);
		Scanner scanner = new Scanner(System.in);
		
		//CommandLineParser.parseLevelCommand("--level GameMaps/saveFile.txt", gameloop, scanner);
		CommandLineParser.parseFolderLevelsCommand("--levels GameMaps", gameloop, scanner);
		
		scanner.close();
		
	}
}
