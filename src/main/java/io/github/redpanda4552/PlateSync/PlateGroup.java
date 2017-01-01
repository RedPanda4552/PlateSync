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
