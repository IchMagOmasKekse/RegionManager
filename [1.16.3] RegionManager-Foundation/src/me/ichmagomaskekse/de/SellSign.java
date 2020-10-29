package me.ichmagomaskekse.de;

import org.bukkit.block.Sign;

public class SellSign {
	
	private static String line0 = "§f{ID}";
	private static String line1 = "§b{ISOWNED}";
	private static String line2 = "§b{PRICE1}";
	private static String line3 = "§b{PRICE2}";
	
	public static boolean update(GS gs) {
		if(gs.getSignLocation().getBlock().getState() instanceof Sign) {
			Sign sign = (Sign) gs.getSignLocation().getBlock().getState();
			
			sign.setLine(0, line0.replace("{ID}", gs.getId()));
			if(gs.isOwned()) sign.setLine(1, line1.replace("{ISOWNED}", "§cVergeben"));
			else sign.setLine(1, line1.replace("{ISOWNED}", "§aVerfügbar"));
			sign.setLine(2, line2.replace("{PRICE1}", ""+gs.getPrice1()));
			sign.setLine(3, line3.replace("{PRICE2}", ""+gs.getPrice2()));
			
			sign.update();
			return true;
		}else return false;
	}
	
}
