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

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;
import com.comze_instancelabs.minigamesapi.ArenaSetup;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.commands.CommandHandler;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.DefaultConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

/**
 * Sudoku plugin
 * 
 * @author instancelabs
 * @author mepeisen
 */
public class Main extends JavaPlugin implements Listener
{
    
    private static Main m;
    private MinigamesAPI api;
    private PluginInstance pli;
    
    CommandHandler cmdhandler = new CommandHandler();
    
    @Override
    public void onEnable()
    {
        m = this;
        api = MinigamesAPI.getAPI().setupAPI(this, "bedwars", IArena.class, new ArenasConfig(this), new MessagesConfig(this), new IClassesConfig(this), new StatsConfig(this, false), new DefaultConfig(this, false), true);
        PluginInstance pinstance = api.pinstances.get(this);
        pinstance.addLoadedArenas(loadArenas(this, pinstance.getArenasConfig()));
        Bukkit.getPluginManager().registerEvents(this, this);
        // TODO pinstance.scoreboardManager = new IArenaScoreboard(this);
        IArenaListener listener = new IArenaListener(this, pinstance);
        pinstance.setArenaListener(listener);
        MinigamesAPI.getAPI().registerArenaListenerLater(this, listener);
        pinstance.setAchievementGuiEnabled(true);
        pli = pinstance;
        
        this.getConfig().set(ArenaConfigStrings.CONFIG_CLASSES_ENABLED, false);
        this.getConfig().set(ArenaConfigStrings.CONFIG_SHOP_ENABLED, false);
        this.getConfig().set(ArenaConfigStrings.CONFIG_AUTO_ADD_DEFAULT_KIT, false);
    }

    public static ArrayList<Arena> loadArenas(JavaPlugin plugin, ArenasConfig cf) {
        ArrayList<Arena> ret = new ArrayList<Arena>();
        FileConfiguration config = cf.getConfig();
        if (!config.isSet("arenas")) {
            return ret;
        }
        for (String arena : config.getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX).getKeys(false)) {
            if (Validator.isArenaValid(plugin, arena, cf.getConfig())) {
                ret.add(initArena(arena));
            }
        }
        return ret;
    }

    public static IArena initArena(String arena) {
        IArena a = new IArena(m, arena);
        ArenaSetup s = MinigamesAPI.getAPI().pinstances.get(m).arenaSetup;
        a.init(Util.getSignLocationFromArena(m, arena), Util.getAllSpawns(m, arena), Util.getMainLobby(m), Util.getComponentForArena(m, arena, "lobby"), s.getPlayerCount(m, arena, true), s.getPlayerCount(m, arena, false), s.getArenaVIP(m, arena));
        return a;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        boolean ret = cmdhandler.handleArgs(this, MinigamesAPI.getAPI().getPermissionGamePrefix("sudoku"), "/" + cmd.getName(), sender, args);
        return ret;
    }
    
}
