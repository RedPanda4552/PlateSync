/**
 * This file is part of PlateSync, licensed under the MIT License (MIT)
 * 
 * Copyright (c) 2017 Brian Wood
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.redpanda4552.PlateSync;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.griefcraft.lwc.LWC;

public class PlateSync extends JavaPlugin {

    public Logger log;
    private static PlateSync plateSync;
    private boolean LWCHookEnabled;
    public LWC lwc;
    private ArrayList<PlateGroup> plateGroups = new ArrayList<PlateGroup>();
    
    /**
     * Get the current instance of PlateSync.
     */
    public static PlateSync getPlateSync() {
        return plateSync;
    }
    
    /**
     * Get an ArrayList of all active PlateGroup objects.
     */
    public ArrayList<PlateGroup> getPlateGroups() {
        return plateGroups;
    }
    
    /**
     * Add a new {@link PlateGroup PlateGroup} to the list of active PlateGroups.
     * @param plateGroup - The PlateGroup to add.
     * @return The return value from the ArrayList.add() operation
     */
    public boolean addPlateGroup(PlateGroup plateGroup) {
        return plateGroups.add(plateGroup);
    }
    
    /**
     * Remove a {@link PlateGroup PlateGroup} from the list of active PlateGroups.
     * @param plateGroup - The PlateGroup to remove.
     * @return The return value from the ArrayList.remove() operation
     */
    public boolean removePlateGroup(PlateGroup plateGroup) {
        return plateGroups.remove(plateGroup);
    }
    
    /**
     * Indicates if LWC is present and enabled, and if the LWC object in PlateSync is instantiated.
     * @return True if LWC is present and enabled, false otherwise
     */
    public boolean isLWCHookEnabled() {
        return LWCHookEnabled;
    }
    
    public void onEnable() {
        plateSync = this;
        log = getLogger();
        Plugin lwcPlugin = getServer().getPluginManager().getPlugin("LWC");
        
        if (lwcPlugin != null && lwcPlugin.isEnabled()) {
            lwc = LWC.getInstance();
            LWCHookEnabled = true;
            log.info("LWC soft dependency found and initialized successfully.");
        } else {
            log.info("LWC is either disabled or not installed on this server! LWC integration will be disabled.");
        }
        
        getServer().getPluginManager().registerEvents(new PlateListener(this), this);
    }
    
    public void onDisable() {
        for (PlateGroup plateGroup : getPlateGroups()) {
            plateGroup.deactivatePlates();
        }
        
        plateGroups.clear();
    }
}
