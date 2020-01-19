package io.github.eirikh1996.factions3chat.listener

import com.massivecraft.factions.entity.MPlayerColl
import github.scarsz.discordsrv.DiscordSRV
import github.scarsz.discordsrv.api.Subscribe
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePreProcessEvent
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent
import github.scarsz.discordsrv.util.DiscordUtil
import github.scarsz.discordsrv.util.LangUtil
import io.github.eirikh1996.factions3chat.ChatMode
import io.github.eirikh1996.factions3chat.ChatPrefixes
import io.github.eirikh1996.factions3chat.Main
import org.bukkit.Bukkit
import org.bukkit.ChatColor


class DiscordSRVListener {

    @Subscribe
    fun onGameChatMessage(event : GameChatMessagePreProcessEvent) {
        val mp = MPlayerColl.get().get(event.player)
        val cm = Main.instance.chatModes.getOrDefault(mp, ChatMode.GLOBAL)
        if (cm == ChatMode.GLOBAL) {
            return
        } else if (cm == ChatMode.STAFF) {
            event.channel = Main.instance.discordStaffChannel
            return
        }
        event.isCancelled = true
    }

    @Subscribe
    fun onMessageReceive(event : DiscordGuildMessagePreProcessEvent) {
        val user = event.message.author
        val dsrv = Main.instance.discordSrvPlugin
        val pid = dsrv!!.accountLinkManager.getUuid(user.id)
        val channel = event.channel
        if (DiscordSRV.getPlugin().channels.get(Main.instance.discordStaffChannel) != channel.id) {
            return
        }
        event.isCancelled = true
        if (pid == null) {
            return
        }
        val player = Bukkit.getPlayer(pid)
        if (player == null) {
            return
        }
        var nick : String
        if (event.member.nickname == null) {
            nick = event.member.effectiveName
        } else {
            nick = event.member.nickname!!
        }
        Bukkit.broadcast(ChatPrefixes.STAFF + " [ §bDiscord§r | " + event.member.roles[0].name+ "] " + nick + " > " +  event.message.contentDisplay, "factions3chat.staff")

    }
}