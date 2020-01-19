package io.github.eirikh1996.factions3chat.cmd

import com.massivecraft.factions.cmd.FactionsCommand
import com.massivecraft.factions.entity.BoardColl
import com.massivecraft.factions.entity.FactionColl
import com.massivecraft.massivecore.command.type.TypeNullable
import com.massivecraft.massivecore.command.type.primitive.TypeString
import io.github.eirikh1996.factions3chat.ChatMode
import io.github.eirikh1996.factions3chat.Main
import io.github.eirikh1996.factions3chat.TypeChatMode
import org.bukkit.Bukkit
import org.bukkit.ChatColor

class CmdFactionsChat : FactionsCommand() {
    init {
        addParameter(TypeChatMode)
        this.addAliases<CmdFactionsChat>("chat", "c")
    }

    override fun perform() {
        val cm = this.readArg<ChatMode>()
        if (msender.faction.equals(FactionColl.get().none) && (cm == ChatMode.FACTION || cm == ChatMode.ALLY || cm == ChatMode.TRUCE || cm == ChatMode.ENEMY)) {
            msender.message(ChatColor.YELLOW.toString() + "You are not in a faction")
            return
        }
        if (cm == null) {
            msender.message(ChatColor.YELLOW.toString() + "Invalid argument: " + arg())
            return
        }
        if (!msender.player.hasPermission("factions3chat." + cm.name.toLowerCase())) {
            msender.message(ChatColor.YELLOW.toString() + "You don't have permission for the following chat mode: " + cm.name.toLowerCase())
            return
        }
        Main.instance.chatModes.put(msender, cm)
        msender.message(ChatColor.YELLOW.toString() + "Chatmode set : " + cm.name)
        Main.instance.saveChatModesFile()
    }
}
