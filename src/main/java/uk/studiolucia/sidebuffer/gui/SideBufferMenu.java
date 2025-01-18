package uk.studiolucia.sidebuffer.gui;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import uk.studiolucia.sidebuffer.init.Register;
import uk.studiolucia.sidebuffer.tileentity.SideBufferBlockTile;

import java.util.Map;
import java.util.Objects;

import static java.util.Map.entry;

public class SideBufferMenu extends AbstractContainerMenu {
    private final SideBufferBlockTile sideBufferBlockTile;
    private final ContainerLevelAccess levelAccess;

    public SideBufferMenu(int i, @NotNull Inventory inventory, BlockEntity blockEntity) {
        super(Register.SIDE_BUFFER_MENU.get(), i);
        if (blockEntity instanceof SideBufferBlockTile sideBufferBlockTile1) {
            this.sideBufferBlockTile = sideBufferBlockTile1;
        }
        else {
            throw new IllegalStateException(
                    "Incorrect block entity class (%s) passed into SideBufferMenu".formatted(
                            blockEntity.getClass().getCanonicalName()
                    )
            );
        }

        this.levelAccess = ContainerLevelAccess.create(
                Objects.requireNonNull(sideBufferBlockTile.getLevel()),
                sideBufferBlockTile.getBlockPos()
        );

        createPlayerHotBar(inventory);
        createPlayerInventory(inventory);
        createSideBufferInventory(sideBufferBlockTile);
    }

    public SideBufferMenu(int i, Inventory inventory, FriendlyByteBuf additionalData) {
        this(i, inventory, inventory.player.level().getBlockEntity(additionalData.readBlockPos()));
    }

    private void createPlayerHotBar(@NotNull Inventory inventory) {
        int yPos = 217;
        int xPos = 20;
        int width = 18;
        int columns = 9;

        for (int column = 0; column < columns; column++) {
            addSlot(new Slot(inventory, column, xPos + (column * width), yPos));
        }
    }

    private void createPlayerInventory(@NotNull Inventory inventory) {
        int yPos = 159;
        int xPos = 20;
        int width = 18;
        int columns = 9;
        int rows = 3;
        int startingSlot = 9;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                addSlot(new Slot(inventory, startingSlot + column + (row * columns), xPos + (column * width), yPos));
            }
        }
    }

    private void createSideBufferInventory(SideBufferBlockTile sideBufferBlockTile) {
        Map<Direction, Integer> yPositions = Map.ofEntries(
                entry(Direction.UP, 18),
                entry(Direction.DOWN, 40),
                entry(Direction.NORTH, 62),
                entry(Direction.SOUTH, 84),
                entry(Direction.EAST, 106),
                entry(Direction.WEST, 128)
        );
        int xPos = 20;
        int width = 18;
        int columns = 9;

        sideBufferBlockTile.getOptionals().forEach((side, optional) -> optional.ifPresent(inventory -> {
            for (int column = 0; column < columns; column++) {
                addSlot(new SlotItemHandler(inventory, column, xPos + (column * width), yPositions.get(side)));
            }
        }));
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int pIndex) {
        int globalInvSize = 35 + (9*6);
        Slot fromSlot = getSlot(pIndex);
        ItemStack fromStack = fromSlot.getItem();

        if (fromStack.getCount() <= 0) {
            fromSlot.set(ItemStack.EMPTY);
        }

        if (!fromSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack copy = fromStack.copy();

        if (pIndex <= 35) {
            if (!moveItemStackTo(fromStack, 36, globalInvSize, false)) {
                return ItemStack.EMPTY;
            }
        }
        else if (pIndex <= globalInvSize) {
            if (!moveItemStackTo(fromStack, 0, 35, false)) {
                return ItemStack.EMPTY;
            }
        }
        else {
            System.err.printf("Invalid slot index in quickMoveStack: %d%n", pIndex);
            return ItemStack.EMPTY;
        }

        fromSlot.setChanged();
        fromSlot.onTake(player, fromStack);

        return copy;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.levelAccess, player, Register.SIDE_BUFFER_BLOCK.get());
    }

    @SuppressWarnings("unused")
    public SideBufferBlockTile getBlockEntity() {
        return sideBufferBlockTile;
    }
}
