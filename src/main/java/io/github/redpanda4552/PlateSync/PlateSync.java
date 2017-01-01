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
        PlateGroup toRemove = null;
        
        for (PlateGroup plateGroup : getPlateGroups()) {
            plateGroup.deactivatePlates();
            toRemove = plateGroup;
        }
        
        removePlateGroup(toRemove);
    }
}
