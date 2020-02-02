package io.github.eirikh1996.factions3chat

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

object UpdateManager : BukkitRunnable(), Listener {
    override fun run() {
        val currentVersion = Main.instance.description.version.toDouble()
        val newVersion = getNewVersion(currentVersion)
        GlobalScope.async {
            delay(5000)
            if (newVersion <= currentVersion) {
                Main.instance.logger.info("You are up to date")
                return@async
            }
            Main.instance.logger.warning("There is a new update available at https://dev.bukkit.org/projects/factions3chat/files")
        }
    }

    @EventHandler
    fun onPlayerJoin(event : PlayerJoinEvent) {
        if (!event.player.hasPermission("factions3chat.update")) {
            return
        }
        if (getNewVersion(Main.instance.description.version.toDouble()) <= Main.instance.description.version.toDouble()) {
            return
        }
        event.player.sendMessage("There is a new update of Factions3Chat available at https://dev.bukkit.org/projects/factions3chat/files")

    }

    fun getNewVersion (currentVersion : Double) : Double {
        val url = URL("https://servermods.forgesvc.net/servermods/files?projectids=359203")
        val conn = url.openConnection()
        conn.connectTimeout = 5000
        conn.addRequestProperty("User-Agent", "Factions3Chat Update Checker")
        conn.doOutput = true
        val reader = BufferedReader(InputStreamReader(conn.getInputStream()))
        val response = reader.readLine()
        val gson = Gson()
        val jsonArray = gson.fromJson<JsonArray>(response, JsonArray::class.java)
        if (jsonArray.size() == 0) {
            Main.instance.logger.warning("No files found, or feed URL is bad")
            return currentVersion
        }
        val jsObj = jsonArray.get(jsonArray.size() - 1) as JsonObject
        var version = jsObj.get("name").asString
        version = version.substring(version.lastIndexOf("v") + 1)
        return version.toDouble()
    }

}