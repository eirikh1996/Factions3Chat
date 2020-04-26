package io.github.eirikh1996.factions3chat.cmd

import com.massivecraft.factions.Rel
import com.massivecraft.factions.cmd.FactionsCommand
import com.massivecraft.factions.entity.BoardColl
import com.massivecraft.factions.entity.Faction
import com.massivecraft.factions.entity.FactionColl
import com.massivecraft.factions.entity.MPlayer
import com.massivecraft.massivecore.command.type.primitive.TypeString
import io.github.eirikh1996.factions3chat.ChatMode
import io.github.eirikh1996.factions3chat.TypeChatMode
import io.github.eirikh1996.factions3chat.listener.ChatListener
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World
import java.util.function.Predicate

class CmdQuickMessage : FactionsCommand() {
    init {
        addParameter(TypeChatMode)
        addParameter(TypeString(), true)
        addAliases<CmdQuickMessage>("quickmessage", "qm")
    }

    override fun perform() {
        val cm = readArg<ChatMode>()
        val msg = readArg<String>()
        if (cm == null) {
            msender.message("Invalid chat mode: " + cm)
            return
        }

        if (!msender.player.hasPermission("factions3chat." + cm.name.toLowerCase())) {
            msender.message(ChatColor.YELLOW.toString() + "You don't have permission to use " + cm.name)
            return
        }
        ChatListener.qmPlayers.put(msender.player.uniqueId, cm)
        msender.player.chat(msg)
    }

    private fun getByRel(rel : Rel) : Set<Faction> {
        val factions = HashSet<Faction>()
        for (fId in FactionColl.get().ids) {
            val faction = FactionColl.get().get(fId)
            if (faction.getRelationTo(msenderFaction) != rel) {
                continue
            }
            factions.add(faction)
        }
        return factions
    }

    private fun playersInWorld(world : World): Collection<MPlayer> {
        val players = HashSet<MPlayer>()
        for (player in world.players) {
            players.add(MPlayer.get(player))
        }
        return players
    }
}