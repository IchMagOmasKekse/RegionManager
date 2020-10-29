package me.ichmagomaskekse.de.events;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.ichmagomaskekse.de.GS;
import me.ichmagomaskekse.de.GSManager;
import me.ichmagomaskekse.de.RS;
import me.ichmagomaskekse.de.money.AccountManager;
import me.ichmagomaskekse.de.session.SessionManager;

public class PlayerInteract implements Listener {
		
	public PlayerInteract() {
		RS.getInstance().getServer().getPluginManager().registerEvents(this, RS.getInstance());
	}
	
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e) {
		if(e.getHand() == EquipmentSlot.HAND) {			
			if(e.getPlayer().getInventory().getItemInMainHand() != null) {
				ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
				
				if(item.getType() == SessionManager.wand) {
					if(e.getPlayer().hasPermission("region.selection.create")) {
						if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
							Location pos = e.getClickedBlock().getLocation();
							if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
								if(e.getPlayer().isSneaking()) {
									
									pos.setYaw(e.getPlayer().getLocation().getYaw());
									pos.setPitch(0);
									
									SessionManager.getSession(e.getPlayer()).setSpawn(pos);
									
									e.getPlayer().sendMessage("§bSpawn wurde gesetzt(§7"
											+((int)SessionManager.newSession(e.getPlayer()).getSpawn().getX())+"/"
											+((int)SessionManager.newSession(e.getPlayer()).getSpawn().getY())+"/"
											+((int)SessionManager.newSession(e.getPlayer()).getSpawn().getZ())+"§b)");
									
									e.setCancelled(true);
									return;
								}else {									
									SessionManager.getSession(e.getPlayer()).setUnprocessedPos1(pos);
									
									e.getPlayer().sendMessage("§bPOS1 wurde gesetzt(§7"
											+((int)SessionManager.newSession(e.getPlayer()).getUnprocessedPos1().getX())+"/"
											+((int)SessionManager.newSession(e.getPlayer()).getUnprocessedPos1().getY())+"/"
											+((int)SessionManager.newSession(e.getPlayer()).getUnprocessedPos1().getZ())+"§b)");
									
									e.setCancelled(true);
									return;
								}
							}else if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
								if(e.getPlayer().isSneaking()) {
									if(SessionManager.hasSession(e.getPlayer()) == false) return;
									SessionManager.getSession(e.getPlayer()).setSignLocation(e.getClickedBlock().getLocation());
									
									e.getPlayer().sendMessage("§bDas Verkaufsschild wurde erfasst(§7"
											+((int)SessionManager.newSession(e.getPlayer()).getSignLoc().getX())+"/"
											+((int)SessionManager.newSession(e.getPlayer()).getSignLoc().getY())+"/"
											+((int)SessionManager.newSession(e.getPlayer()).getSignLoc().getZ())+"§b)");
									e.setCancelled(true);
									return;
								}else {
									SessionManager.getSession(e.getPlayer()).setUnprocessedPos2(pos);
									
									e.getPlayer().sendMessage("§bPOS2 wurde gesetzt(§7"
											+((int)SessionManager.newSession(e.getPlayer()).getUnprocessedPos2().getX())+"/"
											+((int)SessionManager.newSession(e.getPlayer()).getUnprocessedPos2().getY())+"/"
											+((int)SessionManager.newSession(e.getPlayer()).getUnprocessedPos2().getZ())+"§b)");
									e.setCancelled(true);
									return;
								}
							}
							
							if(e.getClickedBlock().getState() instanceof Sign) {
								Sign sign = (Sign) e.getClickedBlock().getState();
								String line0 = sign.getLine(0);
								String line1 = sign.getLine(1);
								String line2 = sign.getLine(2);
								String line3 = sign.getLine(3);
								String id = line0.substring(2, line0.length());
								
								if(line0.contains("plot_") && line1.equals("") == false && line2.equals("") == false && line3.equals("") == false) {
									/* Es wurde ein vermeintliches Verkaufsschild erkannt. */
									
									/* Checken, ob die Location von dem geklicktem Plot auch der SignLcoation
									 * des registrierten Grundstückes entspricht */
									GS gs = GSManager.getGS(id);
									
									/* Temporäre API-Einbindung.
									 * Muss später durch offizielle Money-API ersetzt werden! */
									if(AccountManager.getMoney(e.getPlayer().getUniqueId()) >= gs.getPrice1() ||
											AccountManager.getMoney(e.getPlayer().getUniqueId()) >= gs.getPrice2()) {
										
										/* GELD-Check und Geld-Removal */
										if(AccountManager.getMoney(e.getPlayer().getUniqueId()) >= ((int)gs.getPrice1()))
											AccountManager.removeMoney(e.getPlayer().getUniqueId(), (int)gs.getPrice1());
										else if(AccountManager.getMoney(e.getPlayer().getUniqueId()) >= ((int)gs.getPrice2()))
											AccountManager.removeMoney(e.getPlayer().getUniqueId(), (int)gs.getPrice2());
										
										
										/* Gültig auch für offizielle API */
										gs.setOwner(e.getPlayer().getUniqueId());
										gs.setOwned(true);
										gs.setNickname("§bGS von §f"+e.getPlayer().getName());
										gs.setup();
										GSManager.saveGS(gs);
										/* ENDE Nichtmehr gültig für offizielle API */
										
									}else e.getPlayer().sendMessage("§cDu hast nicht genug Geld dafür!");
									/* ENDE API-Einbindung */
								}
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent e) {
//		if(SessionManager.hasSession(e.getPlayer()) && SessionManager.getSession(e.getPlayer()).getRegion().isIn(e.getPlayer().getLocation())) RS.broadcastMessage(true, "Bist drin!");
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent e) {
		if(RS.hasPermission(e.getPlayer(), "region.build.everywhere") == false && SessionManager.hasSession(e.getPlayer()) &&
				SessionManager.getSession(e.getPlayer()).getRegion() != null &&
				SessionManager.getSession(e.getPlayer()).getRegion().isIn(e.getBlockPlaced().getLocation()) == false)
			e.setCancelled(true);
	}
	
}
