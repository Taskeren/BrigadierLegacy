package brigadier_legacy.test

import brigadier_legacy.argument.ColorArgumentType
import com.github.taskeren.brigadier_kt.argument
import com.github.taskeren.brigadier_kt.executesUnit
import com.github.taskeren.brigadier_kt.literal
import com.github.taskeren.brigadier_kt.registerCommand
import com.github.taskeren.brigadier_kt.via
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText

object TestCommands {

    @JvmStatic
    fun apply(dispatcher: CommandDispatcher<ICommandSender>) {
        dispatcher.registerCommand("test") {
            literal("color") {
                argument("colorValue", ColorArgumentType.color()) {
                    executesUnit { ctx ->
                        val colorValue by ctx.via(ColorArgumentType::getColor)
                        ctx.source.sendMessage("The color you choose is: ${colorValue}${colorValue.friendlyName}")
                    }
                }
            }
        }
    }

    private fun ICommandSender.sendMessage(message: String) = addChatMessage(ChatComponentText(message))

}
