package uk.studiolucia.sidebuffer;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import uk.studiolucia.sidebuffer.init.Register;

@Mod(SideBuffer.MODID)
public class SideBuffer {
    public static final String MODID = "sidebuffer";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SideBuffer() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        Register.BLOCKS.register(bus);
        Register.ITEMS.register(bus);
        Register.TABS.register(bus);
        Register.BLOCK_ENTITY_TYPES.register(bus);
        Register.MENU_TYPES.register(bus);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
