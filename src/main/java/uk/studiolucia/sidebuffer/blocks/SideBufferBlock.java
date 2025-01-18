package uk.studiolucia.sidebuffer.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.studiolucia.sidebuffer.handlers.SidedItemStackHandler;
import uk.studiolucia.sidebuffer.init.Register;
import uk.studiolucia.sidebuffer.tileentity.SideBufferBlockTile;

import java.util.HashMap;

public class SideBufferBlock extends Block implements EntityBlock {
    public SideBufferBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return Register.SIDE_BUFFER_BLOCK_TILE.get().create(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;
        return (level0, pos0, state0, blockEntity) -> ((SideBufferBlockTile) blockEntity).tick(level0, pos0, state0, (SideBufferBlockTile) blockEntity);
    }

    @Override
    public InteractionResult use(
        @NotNull BlockState blockState,
        @NotNull Level level,
        @NotNull BlockPos blockPos,
        @NotNull Player player,
        @NotNull InteractionHand interactionHand,
        @NotNull BlockHitResult blockHitResult
    ) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (!(blockEntity instanceof SideBufferBlockTile sideBufferBlockTile)) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;
        //if (player instanceof ServerPlayer sPlayer) sPlayer.openMenu(sideBufferBlockTile);

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public void onRemove(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState1, boolean b) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof SideBufferBlockTile) {
                HashMap<Direction, SidedItemStackHandler> itemStackHandlers = ((SideBufferBlockTile) blockEntity).getInventories();

                itemStackHandlers.forEach((side, sidedItemStackHandler) -> {
                    for (int slot = 0; slot < sidedItemStackHandler.getSlots(); slot++) {
                        ItemStack stack = sidedItemStackHandler.getStackInSlot(slot);
                        ItemEntity entity = new ItemEntity(level, blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D, stack);
                        level.addFreshEntity(entity);
                    }
                });
            }
        }
        super.onRemove(blockState, level, blockPos, blockState1, b);
    }
}
