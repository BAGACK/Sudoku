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
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaPlayer;
import com.comze_instancelabs.minigamesapi.util.Cuboid;

/**
 * Sodoku arena.
 * 
 * @author mepeisen
 */
public class IArena extends Arena
{
    
    /** Map from players to sudoku. */
    private Map<String, Sudoku> playerToSudokus = new HashMap<>();
    
    /** Main plugin. */
    private Main                m;
    
    /**
     * Constructor
     * 
     * @param m
     * @param arena
     */
    public IArena(Main m, String arena)
    {
        super(m, arena);
        this.m = m;
    }
    
    @Override
    public void started()
    {
        super.started();
        
        for (Map.Entry<String, Location> entry : this.getPSpawnLocs().entrySet())
        {
            final ArenaPlayer apl = ArenaPlayer.getPlayerInstance(entry.getKey());
            for (byte i = 0; i < 9; i++)
            {
                apl.getPlayer().getInventory().setItem(i, new ItemStack(Material.CARPET, 1, (byte) (i + 1)));
            }
            apl.getPlayer().updateInventory();
        }
    }

    @Override
    public void start(boolean tp)
    {
        super.start(tp);
        this.playerToSudokus.clear();
        int dif = 2; // TODO set difficulty by config
        final Sudoku sudoku = new Sudoku(dif);
        for (Map.Entry<String, Location> entry : this.getPSpawnLocs().entrySet())
        {
            this.playerToSudokus.put(entry.getKey(), sudoku.clone());
            this.arraysToBlocks(entry.getValue(), sudoku.display_());
        }
    }
    
    @Override
    public void stop()
    {
        super.stop();
        this.playerToSudokus.clear();
    }
    
    public Sudoku getSudoku(String player)
    {
        return this.playerToSudokus.get(player);
    }
    
    /**
     * Get sudoku cuboid for player
     * @param player
     * @return sudoku coboid
     */
    public Cuboid getCuboidForPlayer(String player)
    {
        final Location l = this.getPSpawnLocs().get(player);
        if (l != null)
        {
            return createCuboidFromSpawn(l);
        }
        return null;
    }

    /**
     * Create cuboid from spawn loc.
     * @param l
     * @return cuboid for spawn.
     */
    private Cuboid createCuboidFromSpawn(final Location l)
    {
        return new Cuboid(l.clone().add(new Vector(-4, 0, -4)), l.clone().add(new Vector(4, 0, 4)));
    }
    

    /**
     * Sets the blocks on start
     * @param l
     * @param f
     */
    private void arraysToBlocks(Location l, ArrayList<Integer[]> f)
    {
        Cuboid c = this.createCuboidFromSpawn(l);
        
        Location start = c.getLowLoc();
        Location end = c.getHighLoc();
        
        Location current = start;
        
        for (Integer[] i__ : f)
        { // each row
            for (Integer i_ : i__)
            { // each item in row
                // change block
                ItemStack carpet = null;
                if (i_.equals(1))
                {
                    carpet = new ItemStack(Material.CARPET, 1, (byte) 1);
                }
                else if (i_.equals(2))
                {
                    carpet = new ItemStack(Material.CARPET, 1, (byte) 2);
                }
                else if (i_.equals(3))
                {
                    carpet = new ItemStack(Material.CARPET, 1, (byte) 3);
                }
                else if (i_.equals(4))
                {
                    carpet = new ItemStack(Material.CARPET, 1, (byte) 4);
                }
                else if (i_.equals(5))
                {
                    carpet = new ItemStack(Material.CARPET, 1, (byte) 5);
                }
                else if (i_.equals(6))
                {
                    carpet = new ItemStack(Material.CARPET, 1, (byte) 6);
                }
                else if (i_.equals(7))
                {
                    carpet = new ItemStack(Material.CARPET, 1, (byte) 7);
                }
                else if (i_.equals(8))
                {
                    carpet = new ItemStack(Material.CARPET, 1, (byte) 8);
                }
                else if (i_.equals(9))
                {
                    carpet = new ItemStack(Material.CARPET, 1, (byte) 9);
                }
                else if (i_.equals(0))
                {
                    carpet = new ItemStack(Material.AIR, 1);
                }
                
                if (carpet != null)
                {
                    Block ch = l.getWorld().getBlockAt(new Location(l.getWorld(), current.getBlockX(), current.getBlockY(), current.getBlockZ()));
                    ch.setType(carpet.getType());
                    // ch.setData(DyeColor.RED.getData());
                    ch.setData(carpet.getData().getData());
                }
                else
                {
                    l.getWorld().getBlockAt(new Location(l.getWorld(), current.getBlockX(), current.getBlockY(), current.getBlockZ())).setType(Material.CARPET);
                }
                current.setX(current.getBlockX() + 1);
            }
            current = start;
            current.setX(current.getBlockX() - 9);
            current.setZ(current.getBlockZ() + 1);
        }
    }
    
}
