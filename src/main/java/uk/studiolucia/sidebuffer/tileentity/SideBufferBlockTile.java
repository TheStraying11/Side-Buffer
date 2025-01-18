package uk.studiolucia.sidebuffer.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.studiolucia.sidebuffer.SideBuffer;
import uk.studiolucia.sidebuffer.gui.SideBufferMenu;
import uk.studiolucia.sidebuffer.handlers.SidedItemStackHandler;
import uk.studiolucia.sidebuffer.init.Register;

import java.util.*;

import static java.util.Map.entry;

public class SideBufferBlockTile extends BlockEntity implements BlockEntityTicker<SideBufferBlockTile>, MenuProvider {
    private static final Component TITLE = Component.translatable("block.sidebuffer.side_buffer.title");

    private final HashMap<Direction, SidedItemStackHandler> itemStackHandlers = new HashMap<>();
    private final HashMap<Direction, LazyOptional<SidedItemStackHandler>> optionals = new HashMap<>();


    public SideBufferBlockTile(BlockPos blockPos, BlockState blockState) {
        super(Register.SIDE_BUFFER_BLOCK_TILE.get(), blockPos, blockState);

        itemStackHandlers.putAll(Map.ofEntries(
                entry(Direction.UP, new SidedItemStackHandler(this, 9, Direction.UP)),
                entry(Direction.DOWN , new SidedItemStackHandler(this, 9, Direction.DOWN)),
                entry(Direction.NORTH , new SidedItemStackHandler(this, 9, Direction.NORTH)),
                entry(Direction.SOUTH , new SidedItemStackHandler(this, 9, Direction.SOUTH)),
                entry(Direction.EAST , new SidedItemStackHandler(this, 9, Direction.EAST)),
                entry(Direction.WEST , new SidedItemStackHandler(this, 9, Direction.WEST))
        ));

        itemStackHandlers.forEach((direction, sidedItemStackHandler) -> {
            optionals.put(direction, LazyOptional.of(() -> sidedItemStackHandler));
        });
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull SideBufferBlockTile sideBufferBlockTile) {
        if (this.level == null) return;
        if (this.level.isClientSide()) return;

        this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        CompoundTag sideBufferData = nbt.getCompound(SideBuffer.MODID);

        CompoundTag inventory = sideBufferData.getCompound("Inventory");

        this.itemStackHandlers.forEach((side, sidedItemStackHandler) -> {
            sidedItemStackHandler.deserializeNBT(inventory.getCompound(side.toString()));
        });
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        CompoundTag sideBufferData = new CompoundTag();
        CompoundTag inventory = new CompoundTag();

        //for (Map.Entry<> : this.itemStackHandlers.) {
        this.itemStackHandlers.forEach((side, sidedItemStackHandler) -> {
            inventory.put(side.toString(), sidedItemStackHandler.serializeNBT());
        });

        sideBufferData.put("Inventory", inventory);

        nbt.put(SideBuffer.MODID, sideBufferData);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.optionals.get(side).cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<SidedItemStackHandler> optional : this.optionals.values()) {
            optional.invalidate();
        }
    }

    public HashMap<Direction, SidedItemStackHandler> getInventories() {
        return this.itemStackHandlers;
    }

    public HashMap<Direction, LazyOptional<SidedItemStackHandler>> getOptionals() {
        return this.optionals;
    }

    @Nullable
    public ItemStack getStack(int slot, Direction side) {
        return itemStackHandlers.get(side).getStackInSlot(slot);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new SideBufferMenu(i, inventory, this);
    }
}
