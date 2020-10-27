package me.ichmagomaskekse.de.atlas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ichmagomaskekse.de.RS;

public class IDAtlas {
	
	private static File file = null;
	private static FileConfiguration cfg = null;
	private static ArrayList<String> ids = new ArrayList<String>();
	
	public IDAtlas() {
		RS.getInstance().saveResource("/id_atlas.yml", false);
		loadIDs();
	}
	
	/* IDs laden.
	 * Wird beim Start des Plugins einmal ausgeführt */
	private static void loadIDs() {
		file = new File(RS.home_path);
		cfg = YamlConfiguration.loadConfiguration(file);
		
		if(ids.size() != cfg.getStringList("IDs").size()) saveIDs();
		ids = (ArrayList<String>) cfg.getStringList("IDs");
		
	}
	
	/*
	 * Speichert IDs um sie in den Atlas aufzunehmen
	 */
	public static boolean saveID(String id) {
		ArrayList<String> list = new ArrayList<String>();
		file = new File(RS.home_path);
		cfg = YamlConfiguration.loadConfiguration(file);
		
		list = (ArrayList<String>) cfg.getStringList("IDs");
		if(idIsRegistered(id) == false) {
			list.add(id);
			try {
				cfg.save(file);
				
				/*
				 * zum Schluss werden alle IDs nocheinmal geladen,
				 * um die ArrayList namens 'ids' auf dem neusten Stand zu halten
				 */
				loadIDs();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * Speichert alle IDs die in der 'ids' ArrayList vorhanden sind.
	 * Dies wird beim Abschalten des Plugins verwendet,
	 * um keine Datenverluste zu erhalten.
	 */
	public static boolean saveIDs() {
		file = new File(RS.home_path);
		cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set("IDs", ids);
		try {
			cfg.save(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/*
	 * Überprüft, ob eine ID bereits verwendet wird
	 */
	public static boolean idIsRegistered(String id) {
		return ids.contains(id);
	}

}
