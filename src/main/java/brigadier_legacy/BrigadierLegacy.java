package brigadier_legacy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BrigadierLegacy.MODID, version = Tags.VERSION, name = "BrigadierLegacy", acceptedMinecraftVersions = "[1.7.10]")
public class BrigadierLegacy {

    public static final String MODID = "brigadier_legacy";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG.info("Brigadier Legacy preInit");
    }

}
