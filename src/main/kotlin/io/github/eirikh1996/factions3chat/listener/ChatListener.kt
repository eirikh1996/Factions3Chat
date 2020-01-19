package io.github.eirikh1996.factions3chat.listener

import com.massivecraft.factions.Rel
import com.massivecraft.factions.entity.MPlayerColl
import io.github.eirikh1996.factions3chat.ChatMode
import io.github.eirikh1996.factions3chat.ChatPrefixes
import io.github.eirikh1996.factions3chat.Main
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener : Listener {

    @EventHandler
    fun onPlayerChat(event : AsyncPlayerChatEvent) {
        val mSender = MPlayerColl.get().get(event.player)
        val cm = Main.instance.chatModes.getOrDefault(mSender, ChatMode.GLOBAL)
        val notReceiving = HashSet<Player>()
        for (recipient in event.recipients) {
            val essPlugin = Main.instance.essentialsPlugin
            if (essPlugin != null) {
                //Allow users with social spy enabled to see non-global chats
                val user = essPlugin.getUser(recipient)
                if (user.isSocialSpyEnabled)
                    continue
            }
            val mRecipient = MPlayerColl.get().get(recipient)
            if (cm == ChatMode.TRUCE && (mSender.getRelationTo(mRecipient) != Rel.TRUCE && mSender.getRelationTo(mRecipient) != Rel.FACTION) || !recipient.hasPermission("factions3chat.truce")) {
                notReceiving.add(recipient)
            } else if (cm == ChatMode.ALLY && (mSender.getRelationTo(mRecipient) != Rel.ALLY && mSender.getRelationTo(mRecipient) != Rel.FACTION) || !recipient.hasPermission("factions3chat.ally")) {
                notReceiving.add(recipient)
            } else if (cm == ChatMode.FACTION && mSender.getRelationTo(mRecipient) != Rel.FACTION || !recipient.hasPermission("factions3chat.faction")) {
                notReceiving.add(recipient)
            } else if (cm == ChatMode.ENEMY && (mSender.getRelationTo(mRecipient) != Rel.ENEMY && mSender.getRelationTo(mRecipient) != Rel.FACTION) || !recipient.hasPermission("factions3chat.enemy")) {
                notReceiving.add(recipient)
            } else if (cm == ChatMode.NEUTRAL && (mSender.getRelationTo(mRecipient) != Rel.NEUTRAL && mSender.getRelationTo(mRecipient) != Rel.FACTION) || !recipient.hasPermission("factions3chat.neutral")) {
                notReceiving.add(recipient)
            } else if (cm == ChatMode.LOCAL && event.player.location.toVector().subtract(recipient.location.toVector()).length() > Main.instance.localChatRange || !recipient.hasPermission("factions3chat.local")) {
                notReceiving.add(recipient)
            } else if (cm == ChatMode.STAFF && !recipient.hasPermission("factions3chat.staff")) {
                notReceiving.add(recipient)
            }
        }
        event.recipients.removeAll(notReceiving)
        val format = event.format
        event.format = ChatPrefixes.getPrefix(cm) + format


    }
}