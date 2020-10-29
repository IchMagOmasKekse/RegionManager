package me.ichmagomaskekse.de.session;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.GS;
import me.ichmagomaskekse.de.Region;

public class PlayerSession {
	
	/*  */
	private String id = "";
	private Player host = null;
	private Location pos1, pos2;
	private Location pos1Unprocessed, pos2Unprocessed;
	private Region region = null;
	private Location sign_loc = null; //Verkaufsschild
	private Location spawn = null; //Spawnpunkt des Grundstückes
	private boolean readyToSave = false; //Gibt an, ob alle Bedingungen zum Speichern erfüllt sind
	
	/* Diese GS Instanz ist notwendig für den /gs list Befehl */
	private GS gs = null;
	
	public PlayerSession(String id, Player host) {
		this.id = id;
		this.host = host;
	}
	
	public void setup() {
		region = new Region(pos1Unprocessed, pos2Unprocessed);
		
		pos1 = region.getCorner1();
		pos2 = region.getCorner7();
		
		if(hasBothPositions() && sign_loc != null && spawn != null) readyToSave = true;
		else readyToSave = false;
	}
	
	/* Roh Locations */
	public void setUnprocessedPos1(Location l) {
		this.pos1Unprocessed = l.clone();
		if(hasBothPositions() && sign_loc != null && spawn != null) setup();
	}
	public void setUnprocessedPos2(Location l) {
		this.pos2Unprocessed = l.clone();
		if(hasBothPositions() && sign_loc != null && spawn != null) setup();
	}
	public void setSpawn(Location spawn) {
		spawn.add(0.5, 0, 0.5);
		this.spawn = spawn.clone();
		if(hasBothPositions() && sign_loc != null && spawn != null) setup();
	}
	public void setSignLocation(Location loc) {
		this.sign_loc = loc.clone();
		if(hasBothPositions() && sign_loc != null && spawn != null) setup();
	}
	/* ---------------------------------- */
	
	
	public boolean hasBothPositions() {
		if(pos1Unprocessed != null && pos2Unprocessed != null) return true;
		else return false;
	}
	
	public void setGS(GS gs, boolean overwrite) { if(overwrite) this.gs = gs; else if(this.gs == null) this.gs = gs; }
	
	public Player getHost() { return host; }
	public String getID() { return id; }
	public Location getSignLoc() { return sign_loc.clone(); }
	public Location getPos1() { return pos1.clone(); }
	public Location getPos2() { return pos2.clone(); }
	public Location getUnprocessedPos1() { return pos1Unprocessed.clone(); }
	public Location getUnprocessedPos2() { return pos2Unprocessed.clone(); }
	public Location getSpawn() { return spawn.clone(); }
	public Region getRegion() { return region; }
	public boolean isReadyToSave() { return readyToSave; }
	public GS getGS() { return gs; }

}
