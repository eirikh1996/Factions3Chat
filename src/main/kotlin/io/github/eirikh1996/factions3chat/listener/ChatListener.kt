package io.github.eirikh1996.factions3chat.listener

import com.massivecraft.factions.Rel
import com.massivecraft.factions.entity.MPlayerColl
import io.github.eirikh1996.factions3chat.ChatMode
import io.github.eirikh1996.factions3chat.ChatPrefixes
import io.github.eirikh1996.factions3chat.Main
import io.github.eirikh1996.factions3chat.TextColors
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

object ChatListener : Listener {
    val qmPlayers = HashMap<UUID, ChatMode>()

    @EventHandler
    fun onPlayerChat(event : AsyncPlayerChatEvent) {
        val mSender = MPlayerColl.get().get(event.player)
        val cm = if (qmPlayers.containsKey(event.player.uniqueId)) {
            qmPlayers.remove(event.player.uniqueId)
        } else {
            Main.instance.chatModes.getOrDefault(event.player.uniqueId, ChatMode.GLOBAL)
        }!!

        val notReceiving = HashSet<Player>()
        for (recipient in event.recipients) {
            if (recipient.equals(event.player))
                continue
            val essPlugin = Main.instance.essentialsPlugin
            if (essPlugin != null) {
                //Allow users with social spy enabled to see non-global chats
                val user = essPlugin.getUser(recipient)
                if (user.isSocialSpyEnabled)
                    continue
            }
            val mRecipient = MPlayerColl.get().get(recipient)
            //For truce chat, view to players in own and truced factions
            if (cm == ChatMode.TRUCE && (mSender.getRelationTo(mRecipient) != Rel.TRUCE && mSender.getRelationTo(mRecipient) != Rel.FACTION) || !recipient.hasPermission("factions3chat.truce")) {
                notReceiving.add(recipient)
            }
            //For ally chat, view to players in own and allied factions
            else if (cm == ChatMode.ALLY && (mSender.getRelationTo(mRecipient) != Rel.ALLY && mSender.getRelationTo(mRecipient) != Rel.FACTION) || !recipient.hasPermission("factions3chat.ally")) {
                notReceiving.add(recipient)
            }
            //For faction chat, view to players in own faction
            else if (cm == ChatMode.FACTION && mSender.getRelationTo(mRecipient) != Rel.FACTION || !recipient.hasPermission("factions3chat.faction")) {
                notReceiving.add(recipient)
            }
            //For enemy chat, view to players in own and enemy factions
            else if (cm == ChatMode.ENEMY && (mSender.getRelationTo(mRecipient) != Rel.ENEMY && mSender.getRelationTo(mRecipient) != Rel.FACTION) || !recipient.hasPermission("factions3chat.enemy")) {
                notReceiving.add(recipient)
            }
            //For neutral chat, view to players in own and neutral factions
            else if (cm == ChatMode.NEUTRAL && (mSender.getRelationTo(mRecipient) != Rel.NEUTRAL && mSender.getRelationTo(mRecipient) != Rel.FACTION) || !recipient.hasPermission("factions3chat.neutral")) {
                notReceiving.add(recipient)
            }
            //For local chat, view to players within a configured distance (default: 1000 blocks) of the sending player
            else if (cm == ChatMode.LOCAL && event.player.location.toVector().subtract(recipient.location.toVector()).length() > Main.instance.localChatRange || !recipient.hasPermission("factions3chat.local")) {
                notReceiving.add(recipient)
            }
            //For staff chat, view only to players with "factions3chat.staff" permission node
            else if (cm == ChatMode.STAFF && !recipient.hasPermission("factions3chat.staff")) {
                notReceiving.add(recipient)
            }
            //For world chat, view only to players in the same world as the sender
            else if (cm == ChatMode.WORLD && !recipient.world.equals(event.player.world) || !recipient.hasPermission("factions3chat.world")) {
                notReceiving.add(recipient)
            }
        }
        event.recipients.removeAll(notReceiving)
        val format = event.format
        event.format = ChatPrefixes.getPrefix(cm) + format

        val message = event.message
        event.message = TextColors.getColor(cm) + message

        if (qmPlayers.containsKey(event.player.uniqueId)) {
            qmPlayers.remove(event.player.uniqueId)
        }


    }
}