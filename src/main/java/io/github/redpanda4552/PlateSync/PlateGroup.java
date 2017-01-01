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

/**
 * A grouping of a Player who acivated a pressure plate,
 * the pressure plate's Block object, a two dimensional array of any neighboring pressure plates,
 * any synthetic weights applied to neighboring pressure plates,
 * and whether this PlateGroup is waiting for the plate to be released.
 */
public class PlateGroup {

    private Player player;
    private Block plate;
    private Block[][] blockArr;
    private boolean waiting;
    
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
    
    public PlateGroup(Player player, Block plate, Block[][] blockArr) {
        this.player = player;
        this.plate = plate;
        this.blockArr = blockArr;
        waiting = true; // Currently unused.
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Block getPlate() {
        return plate;
    }
    
    public Block[][] getBlockArr() {
        return blockArr;
    }
    
    /**
     * Deactivates neighboring pressure plates.
     */
    @SuppressWarnings("deprecation")
    public void deactivatePlates() {
        for (Block[] arr : blockArr) {
            for (Block b : arr) {
                if (isPlate(b)) {
                    b.setData((byte) 0x0);
                }
            }
        }
    }
    
    public boolean isWaiting() {
        return waiting;
    }
    
}
