package brigadier_legacy.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import lombok.extern.log4j.Log4j2;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@Log4j2
@Mod(modid = "brigadier_legacy_test")
public class BrigadierLegacyTest {

    public static final CommandDispatcher<ICommandSender> DISPATCHER = new CommandDispatcher<>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log.info("|=====================================|");
        log.info("|                                     |");
        log.info("|          BRIGADIER LEGACY           |");
        log.info("|                                     |");
        log.info("|=====================================|");

        try {
            TestCommands.apply(DISPATCHER);
        } catch (Throwable e) {
            log.error("Failed to register testing commands!", e);
        }
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        // hijack the command manager, replacing it to my customized one
        MinecraftServer server = event.getServer();
        try {
            FieldUtils.writeDeclaredField(
                server,
                "commandManager",
                new MyCommandHandler(server.getCommandManager()),
                true);
        } catch (Throwable e) {
            log.error("Failed to hijack the commandManager of server!", e);
        }
        log.info("The command manager is successfully hijacked!");
    }

    public static class MyCommandHandler extends CommandHandler {

        private final ICommandManager delegate;

        public MyCommandHandler(ICommandManager delegate) {
            this.delegate = delegate;

            if(!(delegate instanceof CommandHandler)) {
                throw new IllegalArgumentException("Delegator must be CommandHandler, but got " + delegate.getClass());
            }
        }

        @Override
        public int executeCommand(ICommandSender sender, String fullCommand) {
            try {
                return DISPATCHER.execute(fullCommand, sender);
            } catch (CommandSyntaxException e) {
                // fallback
                String command = tryGetCommand(fullCommand);
                if (command != null && delegate.getCommands()
                    .containsKey(command)) {
                    return delegate.executeCommand(sender, fullCommand);
                } else {
                    throw new CommandException(e.getMessage());
                }
            } catch (RuntimeException e) {
                log.warn("Unhandled exception thrown when executing command '{}'", fullCommand, e);
                throw new CommandException(e.getMessage());
            }
        }

        @Override
        public ICommand registerCommand(ICommand p_71560_1_) {
            return ((CommandHandler) delegate).registerCommand(p_71560_1_);
        }

        @Override
        public List<String> getPossibleCommands(ICommandSender p_71558_1_, String p_71558_2_) {
            return delegate.getPossibleCommands(p_71558_1_, p_71558_2_);
        }

        @Override
        public List<ICommand> getPossibleCommands(ICommandSender p_71557_1_) {
            return delegate.getPossibleCommands(p_71557_1_);
        }

        @Override
        public Map<String, ICommand> getCommands() {
            return delegate.getCommands();
        }

        @Nullable
        private String tryGetCommand(String fullCommand) {
            String[] split = fullCommand.split(" ", -1);
            return split.length > 0 ? split[0] : null;
        }
    }

}
