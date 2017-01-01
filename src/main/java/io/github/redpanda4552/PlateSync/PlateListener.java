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

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlateListener implements Listener {
    
    private PlateSync plateSync;
    
    public PlateListener(PlateSync plateSync) {
        this.plateSync = plateSync;
    }

    private final Material[] plates = new Material[] {Material.WOOD_PLATE, Material.STONE_PLATE, Material.IRON_PLATE, Material.GOLD_PLATE};
    
    /**
     * Check if a Block is a type of pressure plate.
     * @param block - The Block to test
     * @return True if the Block is a pressure plate, false otherwise.
     */
    private boolean isPlate(Block block) {
        for (Material m : plates) {
            if (m.equals(block.getType())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if a Block is in a PlateGroup.
     * @param block - The Block to test
     * @return True if the Block is in a PlateGroup, false otherwise.
     */
    private boolean isGrouped(Block block) {
        for (PlateGroup plateGroup : plateSync.getPlateGroups()) {
            if (plateGroup.getPlate().equals(block)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if a Player can access a Block.
     * Checks against LWC, if the plugin is present.
     * @param player - The Player to test
     * @param block - The Block being accessed
     * @return True if LWC allows Player access or is not enabled/present on the server, false if LWC is blocking access.
     */
    private boolean canAccess(Player player, Block block) {
        if (plateSync.isLWCHookEnabled()) {
            if (plateSync.lwc.findProtection(block) != null) {
                if (!plateSync.lwc.canAccessProtection(player, block)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    // TODO Take on "stutter" issue. Make PlateGroups "transitionable" so the blockArr moves with the player and won't release "common" plates
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlatePress(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }
        
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        
        if (!isPlate(block)) {
            return;
        }
        
        if (isGrouped(block)) {
            return;
        }
        
        if (!canAccess(player, block)) {
            return;
        }
        
        // blockArr[x][z]
        Block[][] blockArr = new Block[3][3];
        PlateGroup plateGroup = new PlateGroup(player, block, blockArr);
        
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block relativeBlock = block.getRelative(x, 0, z);
                // + 1 because negatives won't fly in an array
                blockArr[x + 1][z + 1] = relativeBlock;
                
                if (isPlate(relativeBlock) && (x != 0 || z != 0) && canAccess(player, relativeBlock)) {
                    relativeBlock.setData((byte) 0x1);
                }
            }
        }
        
        plateSync.addPlateGroup(plateGroup);
    }
    
    @EventHandler
    public void onPlateRelease(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        
        if (!isPlate(block)) {
            return;
        }
        
        if (!isGrouped(block)) {
            return;
        }
        
        // Check that the plate is releasing and not compressing or being partially charged
        if (event.getOldCurrent() != 15 || event.getNewCurrent() != 0) {
            return;
        }
        
        PlateGroup toRemove = null;
        
        for (PlateGroup plateGroup : plateSync.getPlateGroups()) {
            if (plateGroup.getPlate().equals(block)) {
                toRemove = plateGroup;
            }
        }
        
        if (toRemove == null) {
            return;
        }
        
        toRemove.deactivatePlates();
        plateSync.removePlateGroup(toRemove);
    }
}
