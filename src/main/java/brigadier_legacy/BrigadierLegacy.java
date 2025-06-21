package brigadier_legacy;

import brigadier_legacy.test.TestBootstrap;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BrigadierLegacy.MODID, version = Tags.VERSION, name = "BrigadierLegacy", acceptedMinecraftVersions = "[1.7.10]")
public class BrigadierLegacy {

    public static final String MODID = "brigadier_legacy";
    public static final Logger LOG = LogManager.getLogger(MODID);

    public static boolean isDeobfuscated() {
        return (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG.info("Brigadier Legacy preInit");
        if(isDeobfuscated()) {
            TestBootstrap.init();
        }
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        if(isDeobfuscated()) {
            TestBootstrap.serverAboutToStart(event);
        }
    }

}
