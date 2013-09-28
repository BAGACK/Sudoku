package com.comze_instancelabs.sudoku;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin implements Listener {
	private static Sudoku sudoku;
	static HashMap<Player, String> creation = new HashMap<Player, String>();

	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

		if(cmd.getName().equalsIgnoreCase("su") || cmd.getName().equalsIgnoreCase("sudoku")){
	 		if(args.length < 1){
	 			sender.sendMessage("§3/sb createsudoku");
	 		}else{
	 			Player p = (Player)sender;
	 			if(args.length > 0){
	 				String action = args[0];
	 				if(action.equalsIgnoreCase("createsudoku") && args.length > 1){
	 					// Create arena
	 					if(p.hasPermission("sudoku.create")){
	 						p.sendMessage("§3Select the two points of the 9x9 field.");
	 						
	 						creation.put(p, args[1]);	
		 					p.getInventory().addItem(new ItemStack(Material.WOOD_SPADE, 1));
		 					p.updateInventory();
		 					p.sendMessage("§3Rightclick first one, leftclick second one.");
	 					}
	 				}
	 			}
	 		}
		}
		return false;
	}
	
	
	@EventHandler
	public void onSignUse(PlayerInteractEvent event)
	{	
	    if (event.hasBlock() && event.getAction() == Action.RIGHT_CLICK_BLOCK)
	    {
	        if (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.WALL_SIGN)
	        {
	            final Sign s = (Sign) event.getClickedBlock().getState();
                if (s.getLine(0).equalsIgnoreCase("[Sudoku]") && !s.getLine(1).equalsIgnoreCase(""))
                {
                	sudoku = new Sudoku();
                	arraysToBlocks(s.getLine(1), sudoku);
                }
	        }
	    }
	}
	
	public void arraysToBlocks(String arenaname, ArrayList<Integer[]> f){
		String aren = arenaname;
		
		World w = Bukkit.getWorld(getConfig().getString(aren + ".gs.world"));
		Location loc1 = new Location(w, getConfig().getInt(aren + ".gs.x1"), getConfig().getInt(aren + ".gs.y1"), getConfig().getInt(aren + ".gs.z1"));
		Location loc2 = new Location(w, getConfig().getInt(aren + ".gs.x2"), getConfig().getInt(aren + ".gs.y2"), getConfig().getInt(aren + ".gs.z2"));
		
		Cuboid c = new Cuboid(loc1, loc2);
		
		Location start = c.getLowLoc();
		Location end = c.getHighLoc();
		
		Location current = start;
		
		for(Integer[] i__ : f){ // each row
			for(Integer i_ : i__){ // each item in row
				// change block
				w.getBlockAt(new Location(w, current.getBlockX(), current.getBlockY(), current.getBlockZ())).setType(Material.WOOL);
				current.setX(current.getBlockX() + 1);
				
			}
			current.setZ(current.getBlockZ() + 1);
		}
	}
	
	
	
	
	
	@EventHandler
    public void onblockbreak(BlockBreakEvent event) {
    	if(creation.containsKey(event.getPlayer()) && event.getPlayer().getItemInHand() != null ){
    		if(event.getPlayer().getItemInHand().getType() == Material.WOOD_SPADE){
                Player p = event.getPlayer();
		        Block bp = event.getBlock();
		        int id = bp.getTypeId();
		
		        Location l = bp.getLocation();
		        int xp = (int)l.getX();
	            int yp = (int)l.getY();
	            int zp = (int)l.getZ();
		        
	            String arenaname = creation.get(event.getPlayer());
	            
	            getConfig().set(arenaname + ".gs.x1", xp);
	            getConfig().set(arenaname + ".gs.y1", yp);
	            getConfig().set(arenaname + ".gs.z1", zp);
	            getConfig().set(arenaname + ".gs.world", p.getWorld().getName());
	            this.saveConfig();
	            
		        event.getPlayer().sendMessage("§2Point registered.");
		        event.setCancelled(true);
           }
    	}
    }

    @EventHandler
    public void onRightclick(PlayerInteractEvent event){
    	if(creation.containsKey(event.getPlayer()) && event.getPlayer().getItemInHand() != null ){
    		if(event.getPlayer().getItemInHand().getType() == Material.WOOD_SPADE){
	            org.bukkit.event.block.Action click = event.getAction();
		        if(click == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK){
			            Block block = event.getClickedBlock();
			            Location l = block.getLocation();
			            
			            int xo = (int)l.getX();
			            int yo = (int)l.getY();
			            int zo = (int)l.getZ();
			            
			            String arenaname = creation.get(event.getPlayer());
			            
			            getConfig().set(arenaname + ".gs.x2", xo);
			            getConfig().set(arenaname + ".gs.y2", yo);
			            getConfig().set(arenaname + ".gs.z2", zo);
			            getConfig().set(arenaname + ".gs.world", event.getPlayer().getWorld().getName());
			            
			            this.saveConfig();
			            
			            if(getConfig().contains(arenaname + ".gs.x1")){
			            	event.getPlayer().sendMessage("§2Sudoku successfully registered.");
			            }else{
			            	event.getPlayer().sendMessage("§2First point registered. Now leftclick the other point.");
			            }
		        }
            }
    	}
        
    }
}
