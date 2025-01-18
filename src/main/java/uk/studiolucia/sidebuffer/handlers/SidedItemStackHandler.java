package uk.studiolucia.sidebuffer.handlers;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public class SidedItemStackHandler extends ItemStackHandler {
    private final Direction side;
    private final BlockEntity blockEntity;

    public SidedItemStackHandler(BlockEntity blockEntity, int slot, Direction side) {
        super(slot);
        this.side = side;
        this.blockEntity = blockEntity;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        this.blockEntity.setChanged();
    }
    public Direction getSide() {
        return this.side;
    }
}
