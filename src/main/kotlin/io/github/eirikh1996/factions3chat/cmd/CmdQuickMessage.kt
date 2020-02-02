package io.github.eirikh1996.factions3chat.cmd

import com.massivecraft.factions.cmd.FactionsCommand
import com.massivecraft.massivecore.command.type.primitive.TypeString
import io.github.eirikh1996.factions3chat.TypeChatMode

object CmdQuickMessage : FactionsCommand() {
    init {
        addParameter(TypeChatMode)
        addAliases<CmdQuickMessage>("quickmessage", "qm")
    }
}