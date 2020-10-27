package me.ichmagomaskekse.de;

import java.util.Random;

import org.bukkit.plugin.java.JavaPlugin;

public class RS extends JavaPlugin {
	/*
	 * RS steht f�r RegionSystem
	 */
	
	public static String home_path = "/plugins/RS/";
	private static RS rs = null;
	public static RS getInstance() {return rs;}
	private static Random ran;
	
	@Override
	public void onEnable() {
		
		/* Initialisierung */
		preInit();  //Bevor das Plugin interne Initialisierungen durchführt
		init();     //Währenddessen das Plugin interne Initialisierungen durchführt
		postInit(); //Nachdem das Plugin interne Initialisierungen durchgeführt hat
		/* --------------- */
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		
		super.onDisable();
	}
	
	private void preInit() {
		/*
		 * Systemrelevante Dateien werden erstellt.
		 */
	}
	
	private void init() {
		/*
		 * Systmerelevante Einstellungen werden geladen.
		 */
	}
	
	private void postInit() {
		/*
		 * Instanzen werden erstellt.
		 * Events werden registriert.
		 */
		
	}
	
	public static String randomIDGenerator() {
		//TODO: Ein 'Belegete-Id-Atlas' muss erstellt werden
		return "unnamed-"+ran.nextInt(10000);
	}
	
}
