package com.comze_instancelabs.sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin implements Listener {
	private static Sudoku sudoku;
	static HashMap<Player, String> creation = new HashMap<Player, String>();
	static HashMap<Player, String> arenap = new HashMap<Player, String>(); // playername -> arenaname

	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

		if(cmd.getName().equalsIgnoreCase("su") || cmd.getName().equalsIgnoreCase("sudoku")){
	 		if(args.length < 1){
	 			sender.sendMessage("§3/sb createsudoku [name]");
	 			return true;
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
	 			return true;
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
                if (s.getLine(0).equalsIgnoreCase("§3[sudoku]") && !s.getLine(1).equalsIgnoreCase(""))
                {
                	//generate sudoku, tp player and add to arenap hashmap
                	String aren = s.getLine(1);
                	sudoku = new Sudoku(Integer.parseInt(s.getLine(2)));
                	arraysToBlocks(aren, sudoku.display_());
            		
            		World w = Bukkit.getWorld(getConfig().getString(aren + ".gs.world"));
                	Location loc1 = new Location(w, getConfig().getInt(aren + ".gs.x1"), getConfig().getInt(aren + ".gs.y1"), getConfig().getInt(aren + ".gs.z1"));

                	event.getPlayer().teleport(loc1);
                	
                	arenap.put(event.getPlayer(), aren);
                }
	        }
	    }
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player p = event.getPlayer();
		if (event.getLine(0).toLowerCase().contains("[sudoku]") && p.hasPermission("horseracing.sign")) {
			event.setLine(0, "§3[Sudoku]");
		}
	}
	
	
	public Cuboid getCuboidFromArena(String aren){
		World w = Bukkit.getWorld(getConfig().getString(aren + ".gs.world"));
		Location loc1 = new Location(w, getConfig().getInt(aren + ".gs.x1"), getConfig().getInt(aren + ".gs.y1"), getConfig().getInt(aren + ".gs.z1"));
		Location loc2 = new Location(w, getConfig().getInt(aren + ".gs.x2"), getConfig().getInt(aren + ".gs.y2"), getConfig().getInt(aren + ".gs.z2"));
		
		Cuboid c = new Cuboid(loc1, loc2);
		
		return c;
	}
	
	public void arraysToBlocks(String arenaname, ArrayList<Integer[]> f){
		String aren = arenaname;
		
		World w = Bukkit.getWorld(getConfig().getString(aren + ".gs.world"));
		Cuboid c = getCuboidFromArena(aren);
		
		Location start = c.getLowLoc();
		Location end = c.getHighLoc();
		
		Location current = start;

		
		for(Integer[] i__ : f){ // each row
			for(Integer i_ : i__){ // each item in row
				// change block
				ItemStack carpet = null;
				if(i_.equals(1)){
					 carpet = new ItemStack( Material.CARPET, 1, (byte)1);
				}else if(i_.equals(2)){
					 carpet = new ItemStack( Material.CARPET, 1, (byte)2);
				}else if(i_.equals(3)){
					 carpet = new ItemStack( Material.CARPET, 1, (byte)3);
				}else if(i_.equals(4)){
					 carpet = new ItemStack( Material.CARPET, 1, (byte)4);
				}else if(i_.equals(5)){
					 carpet = new ItemStack( Material.CARPET, 1, (byte)5);
				}else if(i_.equals(6)){
					 carpet = new ItemStack( Material.CARPET, 1, (byte)6);
				}else if(i_.equals(7)){
					 carpet = new ItemStack( Material.CARPET, 1, (byte)7);
				}else if(i_.equals(8)){
					 carpet = new ItemStack( Material.CARPET, 1, (byte)8);
				}else if(i_.equals(9)){
					 carpet = new ItemStack( Material.CARPET, 1, (byte)9);
				}else if(i_.equals(0)){
					 carpet = new ItemStack( Material.AIR, 1);
				}
				
				if(carpet != null){
					Block ch = w.getBlockAt(new Location(w, current.getBlockX(), current.getBlockY(), current.getBlockZ()));
					ch.setType(carpet.getType());
					//ch.setData(DyeColor.RED.getData());
					ch.setData(carpet.getData().getData());
				}else{
					w.getBlockAt(new Location(w, current.getBlockX(), current.getBlockY(), current.getBlockZ())).setType(Material.CARPET);
				}
				current.setX(current.getBlockX() + 1);
			}
			current = start;
			current.setX(current.getBlockX() - 9);
			current.setZ(current.getBlockZ() + 1);
		}
	}
	
	
	
	public ArrayList<Integer[]> blocksToArrays(String arenaname, ArrayList<Location[]> f){
		ArrayList<Integer[]> ret = new ArrayList<Integer[]>();
		
		String aren = arenaname;
		
		World w = Bukkit.getWorld(getConfig().getString(aren + ".gs.world"));
		Cuboid c = getCuboidFromArena(aren);
		
		Location start = c.getLowLoc();
		Location end = c.getHighLoc();
		
		Location current = start;

		
		for(Location[] i__ : f){ // each row
			
			ArrayList<Integer> row = new ArrayList<Integer>();
			
			for(Location i_ : i__){ // each item in row
				Block ch = w.getBlockAt(i_);

				if(ch.getData() == (byte)1){
					 row.add(1);
				}else if(ch.getData() == (byte)2){
					row.add(2);
				}else if(ch.getData() == (byte)3){
					row.add(3);
				}else if(ch.getData() == (byte)4){
					row.add(4);
				}else if(ch.getData() == (byte)5){
					row.add(5);
				}else if(ch.getData() == (byte)6){
					row.add(6);
				}else if(ch.getData() == (byte)7){
					row.add(7);
				}else if(ch.getData() == (byte)8){
					row.add(8);
				}else if(ch.getData() == (byte)9){
					row.add(9);
				}else if(ch.getData() == (byte)0){
					row.add(0);
				}
				
				current.setX(current.getBlockX() + 1);
			}
			current = start;
			current.setX(current.getBlockX() - 9);
			current.setZ(current.getBlockZ() + 1);
			
			Integer[] row_ = new Integer[row.size()];
			ret.add(row.toArray(row_));
		}
		
		return ret;
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
    	
    	//TODO allow removal of carpets
    	if(arenap.containsKey(event.getPlayer())){
    		if(event.getBlock().getType().equals(Material.CARPET)){
    			if(getCuboidFromArena(arenap.get(event.getPlayer())).containsLoc(event.getBlock().getLocation())){
    				event.getBlock().setType(Material.AIR);
    			}
    		}
    	}
    }

	
	public ArrayList<Location[]> getLocs(String aren){
		ArrayList<Location[]> locs = new ArrayList<Location[]>();
		Cuboid c = this.getCuboidFromArena(aren);
		
		int y = c.getLowLoc().getBlockY();
		for(int z = c.getLowLoc().getBlockZ(); z < c.getZSize() + c.getLowLoc().getBlockZ(); z++){	
			ArrayList<Location> row = new ArrayList<Location>();
			for(int x = c.getLowLoc().getBlockX(); x < c.getXSize() + c.getLowLoc().getBlockX(); x++){
				Location l_ = new Location(c.getWorld(), x, y, z);
				row.add(l_);
			}
			Location[] locs_ = new Location[row.size()];
			locs.add(row.toArray(locs_));
		}
		
		return locs;
	}
	
	
	public ArrayList<Integer[]> convertIntegers(ArrayList<int[]> integers)
	{
		ArrayList<Integer[]> ret = new ArrayList<Integer[]>();
		for(int[] i__ : integers){
			Integer[] newArray = new Integer[i__.length];
			int i = 0;
			for (int value : i__){
			    newArray[i++] = Integer.valueOf(value);
			}
			ret.add(newArray);
		}
		return ret;
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
    	}else{
    		if(event.getPlayer().getItemInHand().getType() == Material.CARPET){
            	org.bukkit.event.block.Action click = event.getAction();
		        if(click == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK){
		            Block block = event.getClickedBlock();
		            
		            Location l = new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY() + 1, block.getLocation().getBlockZ());
		            
		            //getLogger().info(l.toString());
			    	//getLogger().info(this.getCuboidFromArena(arenap.get(event.getPlayer())).toString());
        			
		            
				    if(arenap.containsKey(event.getPlayer())){
				    	if(getCuboidFromArena(arenap.get(event.getPlayer())).containsLoc(l)){
	        				block.getWorld().getBlockAt(l).setType(event.getPlayer().getItemInHand().getType());
	    					//ch.setData(DyeColor.RED.getData());
	        				block.getWorld().getBlockAt(l).setData(event.getPlayer().getItemInHand().getData().getData());
	        			
	        				
	        				
	        				// blocks to array
	        				// compare two arrays (solution and this one)
	        				
	        				
	        				ArrayList<Integer[]> current = new ArrayList<Integer[]>(this.blocksToArrays(arenap.get(event.getPlayer()), this.getLocs(arenap.get(event.getPlayer()))));
	        				ArrayList<Integer[]> solution = convertIntegers(sudoku.getAnswer());
	        				
	        				
	        				
	        				//getLogger().info(current.toString());
	        				//getLogger().info(solution.toString());
	        				
	        				String current_ = "";
	        				for(Integer[] t_ : current){
	        					for(Integer t__ : t_){
	        						current_ += "," + Integer.toString(t__);
	        					}
	        				}
	        				//getLogger().info(current_);
	        				
	        				String solution_ = "";
	        				for(Integer[] t_ : solution){
	        					for(Integer t__ : t_){
	        						solution_ += "," + Integer.toString(t__);
	        					}
	        				}
	        				//getLogger().info(solution_);
	        				
	        				if(current_.equals(solution_)){
	        					//TODO TEST OUT
	        					event.getPlayer().sendMessage("§3You made the sudoku, congrats!");
	        					arenap.remove(event.getPlayer());
	        				}
	        				
				    	}
				    }
		        }
            } 
    	}
    }
}
