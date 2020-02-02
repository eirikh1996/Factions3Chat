package io.github.eirikh1996.factions3chat

import com.massivecraft.massivecore.command.type.TypeAbstract
import org.bukkit.command.CommandSender

object TypeChatMode : TypeAbstract<ChatMode?>(ChatMode::class.java) {
    override fun getTabList(p0: CommandSender?, p1: String?): MutableCollection<String> {
        val args = ArrayList<String>()
        for (cm in ChatMode.values()) {
            val arg = cm.name.toLowerCase()
            if (p1 != null && !arg.startsWith(p1)) {
                continue
            }
            if (p0 == null || !p0.hasPermission("factions3chat." + cm.name.toLowerCase())) {
                continue
            }
            args.add(cm.name.toLowerCase())
        }
        args.add("public")
        args.add("p")
        return args
    }

    override fun read(p0: String?, p1: CommandSender?): ChatMode? {
        if (p0 == null) {
            return null
        } else if (p0.equals("public", true) || p0.equals("p", true)) {
            return ChatMode.GLOBAL
        }
        return ChatMode.getChatMode(p0.toUpperCase())
    }
}