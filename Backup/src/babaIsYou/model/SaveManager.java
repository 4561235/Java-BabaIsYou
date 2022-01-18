package babaIsYou.model;
import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class SaveManager {
	
	static final String SEPARATOR = "#";
	static final String ITEM_IDENTIFIER = "I";
	static final String MAP_INFO_IDENTIFIER = "M";
	
	public static void saveMapToFile(GameMap map) {
		Path saveFilePath = Paths.get("saveFile.txt"); // nom du fichier de sauvegarde: "saveFile.txt"  
	    try(BufferedWriter writer = Files.newBufferedWriter(saveFilePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE)){
	    	
	      writer.write(map.toTextFormat());
	    } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static GameMap loadMapFromFile(Path path) throws IOException{
		BufferedReader reader = Files.newBufferedReader(path);
		
		String line = null;
		GameMap map = null;
		while((line = reader.readLine()) != null) {
			String[] array = line.split(SEPARATOR);
			if(array[0].equals(MAP_INFO_IDENTIFIER)) {
				map = new GameMap();
			}
			else if(array[0].equals(ITEM_IDENTIFIER)) {
				ItemType type = ItemType.valueOf(array[1]);
				ItemPhysics physic = ItemPhysics.valueOf(array[2]);
				Point point = new Point(Integer.parseInt(array[3]),Integer.parseInt(array[4]));
				Item item = new Item(type,physic);
				map.addToMap(item, point);
			}
		}
		return map;
	}
}
