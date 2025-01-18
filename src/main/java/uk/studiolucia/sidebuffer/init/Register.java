package uk.studiolucia.sidebuffer.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import oshi.hardware.Display;
import uk.studiolucia.sidebuffer.SideBuffer;
import uk.studiolucia.sidebuffer.blocks.SideBufferBlock;
import uk.studiolucia.sidebuffer.gui.SideBufferMenu;
import uk.studiolucia.sidebuffer.tileentity.SideBufferBlockTile;

public class Register {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            ForgeRegistries.BLOCKS,
            SideBuffer.MODID
    );

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            SideBuffer.MODID
    );

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            ForgeRegistries.ITEMS,
            SideBuffer.MODID
    );

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES,
            SideBuffer.MODID
    );

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(
            ForgeRegistries.MENU_TYPES,
            SideBuffer.MODID
    );

    public static final RegistryObject<Block> SIDE_BUFFER_BLOCK = BLOCKS.register(
        "side_buffer",
        () -> new SideBufferBlock(
            BlockBehaviour.Properties.copy(Blocks.CHEST)
        )
    );

    public static final RegistryObject<BlockItem> SIDE_BUFFER_BLOCK_ITEM = ITEMS.register("side_buffer", () ->
        new BlockItem(
            SIDE_BUFFER_BLOCK.get(), new Item.Properties()
        )
    );

    public static final RegistryObject<CreativeModeTab> SIDE_BUFFER_TAB = TABS.register("side_buffer",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.side_buffer"))
            .icon(SIDE_BUFFER_BLOCK_ITEM.get()::getDefaultInstance)
            .displayItems((DisplayItemsGenerator, DisplayItem) -> {
                DisplayItem.accept(SIDE_BUFFER_BLOCK_ITEM.get());
            })
            .withTabsBefore(CreativeModeTabs.REDSTONE_BLOCKS)
            .build()
    );

    public static final RegistryObject<BlockEntityType<SideBufferBlockTile>> SIDE_BUFFER_BLOCK_TILE =
        BLOCK_ENTITY_TYPES.register("side_buffer",
            () -> BlockEntityType.Builder.of(SideBufferBlockTile::new, SIDE_BUFFER_BLOCK.get()).build(null)
        );

    public static final RegistryObject<MenuType<SideBufferMenu>> SIDE_BUFFER_MENU = MENU_TYPES.register(
            "side_buffer_menu",
            () -> IForgeMenuType.create(SideBufferMenu::new)
    );
}
