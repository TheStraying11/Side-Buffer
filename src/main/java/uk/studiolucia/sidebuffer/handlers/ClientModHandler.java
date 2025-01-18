package uk.studiolucia.sidebuffer.handlers;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import uk.studiolucia.sidebuffer.SideBuffer;
import uk.studiolucia.sidebuffer.gui.SideBufferScreen;
import uk.studiolucia.sidebuffer.init.Register;

@Mod.EventBusSubscriber(modid = SideBuffer.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(Register.SIDE_BUFFER_MENU.get(), SideBufferScreen::new);
            // TODO: More Later
        });
    }

}
