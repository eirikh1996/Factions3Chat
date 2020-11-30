package io.github.eirikh1996.factions3chat.listener

import com.massivecraft.factions.entity.MPlayerColl
import github.scarsz.discordsrv.DiscordSRV
import github.scarsz.discordsrv.api.Subscribe
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePostProcessEvent
import github.scarsz.discordsrv.api.events.DiscordReadyEvent
import github.scarsz.discordsrv.api.events.GameChatMessagePreProcessEvent
import io.github.eirikh1996.factions3chat.ChatMode
import io.github.eirikh1996.factions3chat.ChatPrefixes
import io.github.eirikh1996.factions3chat.Main
import org.bukkit.Bukkit
import org.bukkit.Bukkit.broadcastMessage


object DiscordSRVListener {

    @Subscribe
    fun onGameChatMessage(event : GameChatMessagePreProcessEvent) {
        val cm = if (ChatListener.qmPlayers.containsKey(event.player.uniqueId)) {
            ChatListener.qmPlayers.get(event.player.uniqueId)
        } else {
            Main.instance.chatModes.getOrDefault(event.player.uniqueId, ChatMode.GLOBAL)
        }!!
        if (cm == ChatMode.GLOBAL) {
            return
        } else if (cm == ChatMode.STAFF) {
            event.channel = "staff"
            return
        }
        event.isCancelled = true
    }

    @Subscribe
    fun onMessageReceive(event : DiscordGuildMessagePostProcessEvent) {
        val channel = event.channel
        if (DiscordSRV.getPlugin().channels.get("staff") != channel.id) {
            return
        }
        //Cancel event
        event.isCancelled = true
        //Then broadcast in staff channel
        Bukkit.broadcast(ChatPrefixes.STAFF +  event.processedMessage, "factions3chat.staff")

    }

    @Subscribe
    fun onDiscordReady(event : DiscordReadyEvent) {
        DiscordSRV.getPlugin().channels.put("staff", Main.instance.config.getString("DiscordStaffChannel", "000000000000000000")!!)
        Main.instance.logger.info("Registered channel ID " + DiscordSRV.getPlugin().channels.get("staff") + " for staff chat")
    }
}