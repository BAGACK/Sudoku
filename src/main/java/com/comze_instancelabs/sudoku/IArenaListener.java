/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.comze_instancelabs.sudoku;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.comze_instancelabs.minigamesapi.ArenaListener;
import com.comze_instancelabs.minigamesapi.ArenaPlayer;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.Cuboid;

/**
 * Arena listener for sudoku
 * 
 * @author mepeisen
 */
public class IArenaListener extends ArenaListener
{

    /**
     * main plugin.
     */
    private Main plugin;
    
    /** the plugin instance. */
    private PluginInstance pli;

    /**
     * Constructor
     * @param plugin
     * @param pinstance
     */
    public IArenaListener(Main plugin, PluginInstance pinstance)
    {
        super(plugin, pinstance, "sudoku", new ArrayList<>(Arrays.asList("/su"))); //$NON-NLS-1$ //$NON-NLS-2$
        this.pli = pinstance;
        this.plugin = plugin;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event)
    {
        final Player p = event.getPlayer();
        if (this.pli.containsGlobalPlayer(p.getName()))
        {
            if (event.getBlock().getType().equals(Material.CARPET))
            {
                final Cuboid cuboidForPlayer = ((IArena)this.pli.global_players.get(p.getName())).getCuboidForPlayer(p.getName());
                if (cuboidForPlayer.containsLoc(event.getBlock().getLocation()))
                {
                    event.getBlock().setType(Material.AIR);
                }
            }
            else
            {
                event.setCancelled(true);
                return;
            }
        }
        super.onBlockBreak(event);
    }
    
    @EventHandler
    public void onRightclick(PlayerInteractEvent event)
    {
        final Player p = event.getPlayer();
        if (this.pli.containsGlobalPlayer(p.getName()))
        {
            if (p.getItemInHand().getType() == Material.CARPET)
            {
                org.bukkit.event.block.Action click = event.getAction();
                if (click == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK)
                {
                    // reset inv
                    final ArenaPlayer apl = ArenaPlayer.getPlayerInstance(p.getName());
                    for (byte i = 0; i < 9; i++)
                    {
                        apl.getPlayer().getInventory().setItem(i, new ItemStack(Material.CARPET, 1, (byte) (i + 1)));
                    }
                    apl.getPlayer().updateInventory();
                    
                    Block block = event.getClickedBlock();
                    Location l = new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY() + 1, block.getLocation().getBlockZ());
                    final IArena iArena = (IArena)this.pli.global_players.get(p.getName());
                    final Cuboid cuboidForPlayer = iArena.getCuboidForPlayer(p.getName());
                    if (cuboidForPlayer.containsLoc(l))
                    {
                        block.getWorld().getBlockAt(l).setType(event.getPlayer().getItemInHand().getType());
                        // ch.setData(DyeColor.RED.getData());
                        block.getWorld().getBlockAt(l).setData(event.getPlayer().getItemInHand().getData().getData());
                        
                        // blocks to array
                        // compare two arrays (solution and this one)
                        
                        ArrayList<Integer[]> current = new ArrayList<Integer[]>(this.blocksToArrays(this.getLocs(cuboidForPlayer), cuboidForPlayer));
                        ArrayList<Integer[]> solution = convertIntegers(iArena.getSudoku(p.getName()).getAnswer());
                        
                        String current_ = "";
                        for (Integer[] t_ : current)
                        {
                            for (Integer t__ : t_)
                            {
                                current_ += "," + Integer.toString(t__);
                            }
                        }
                        if (MinigamesAPI.debug)
                        {
                            this.pli.getPlugin().getLogger().info(current_);
                        }
                        
                        String solution_ = "";
                        for (Integer[] t_ : solution)
                        {
                            for (Integer t__ : t_)
                            {
                                solution_ += "," + Integer.toString(t__);
                            }
                        }

                        if (MinigamesAPI.debug)
                        {
                            this.pli.getPlugin().getLogger().info(solution_);
                        }
                        
                        if (current_.equals(solution_))
                        {
                            for (String p_ : iArena.getAllPlayers())
                            {
                                if (!p_.equals(p.getName()))
                                {
                                    iArena.leavePlayer(p_, false, false);
                                }
                            }
                            iArena.stop();
                        }
                        
                    }
                }
            }
        }
    }
    
    public ArrayList<Integer[]> convertIntegers(ArrayList<int[]> integers)
    {
        ArrayList<Integer[]> ret = new ArrayList<Integer[]>();
        for (int[] i__ : integers)
        {
            Integer[] newArray = new Integer[i__.length];
            int i = 0;
            for (int value : i__)
            {
                newArray[i++] = Integer.valueOf(value);
            }
            ret.add(newArray);
        }
        return ret;
    }
    
    public ArrayList<Location[]> getLocs(Cuboid c)
    {
        ArrayList<Location[]> locs = new ArrayList<Location[]>();
        int y = c.getLowLoc().getBlockY();
        for (int z = c.getLowLoc().getBlockZ(); z < c.getZSize() + c.getLowLoc().getBlockZ(); z++)
        {
            ArrayList<Location> row = new ArrayList<Location>();
            for (int x = c.getLowLoc().getBlockX(); x < c.getXSize() + c.getLowLoc().getBlockX(); x++)
            {
                Location l_ = new Location(c.getWorld(), x, y, z);
                row.add(l_);
            }
            Location[] locs_ = new Location[row.size()];
            locs.add(row.toArray(locs_));
        }
        
        return locs;
    }
    
    private ArrayList<Integer[]> blocksToArrays(ArrayList<Location[]> f, final Cuboid c)
    {
        ArrayList<Integer[]> ret = new ArrayList<Integer[]>();
        
        Location start = c.getLowLoc();
        Location end = c.getHighLoc();
        
        Location current = start;
        
        for (Location[] i__ : f)
        { // each row
            
            ArrayList<Integer> row = new ArrayList<Integer>();
            
            for (Location i_ : i__)
            { // each item in row
                Block ch = start.getWorld().getBlockAt(i_);
                
                if (ch.getData() == (byte) 1)
                {
                    row.add(1);
                }
                else if (ch.getData() == (byte) 2)
                {
                    row.add(2);
                }
                else if (ch.getData() == (byte) 3)
                {
                    row.add(3);
                }
                else if (ch.getData() == (byte) 4)
                {
                    row.add(4);
                }
                else if (ch.getData() == (byte) 5)
                {
                    row.add(5);
                }
                else if (ch.getData() == (byte) 6)
                {
                    row.add(6);
                }
                else if (ch.getData() == (byte) 7)
                {
                    row.add(7);
                }
                else if (ch.getData() == (byte) 8)
                {
                    row.add(8);
                }
                else if (ch.getData() == (byte) 9)
                {
                    row.add(9);
                }
                else if (ch.getData() == (byte) 0)
                {
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
    
}
