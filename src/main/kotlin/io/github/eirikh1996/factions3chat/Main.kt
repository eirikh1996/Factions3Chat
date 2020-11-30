package io.github.eirikh1996.factions3chat

import com.earth2me.essentials.Essentials
import com.massivecraft.factions.Factions
import com.massivecraft.factions.entity.MConf
import com.massivecraft.massivecore.command.MassiveCommand
import com.massivecraft.factions.cmd.CmdFactions
import github.scarsz.discordsrv.DiscordSRV
import io.github.eirikh1996.factions3chat.cmd.CmdFactionsChat
import io.github.eirikh1996.factions3chat.cmd.CmdQuickMessage
import io.github.eirikh1996.factions3chat.listener.ChatListener
import io.github.eirikh1996.factions3chat.listener.DiscordSRVListener
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.yaml.snakeyaml.Yaml
import java.io.*
import java.util.*
import kotlin.collections.HashMap


class Main : JavaPlugin() {

    val chatModes = HashMap<UUID, ChatMode>()
    var localChatRange = 1000
    var discordSrvPlugin : DiscordSRV? = null
    var factionsPlugin : Factions? = null
    var essentialsPlugin : Essentials? = null

    override fun onEnable() {
        val chatmodes = File(dataFolder, "chatmodes.yml")
        if (chatmodes.exists()) {
            val yaml = Yaml()
            val data = yaml.load<Map<String, String>>(FileInputStream(chatmodes))
            if (data != null) {
                for (entry in data) {
                    val id = UUID.fromString(entry.key)
                    chatModes.put(id, ChatMode.getChatMode(entry.value)!!)
                }
            }

        }
        saveDefaultConfig()
        updateConfig()
        CmdFactions.get().addChild<MassiveCommand>(CmdFactionsChat())
        CmdFactions.get().addChild<MassiveCommand>(CmdQuickMessage())
        localChatRange = config.getInt("LocalChatRange", 1000)
        val chatPrefix = config.getConfigurationSection("ChatPrefixes")!!
        ChatPrefixes.ALLY = chatPrefix.getString("Ally", "§e[<fcolor>ALLY§e]§r ")!!.replace("<fcolor>", MConf.get().colorAlly.toString(), false)
        ChatPrefixes.TRUCE = chatPrefix.getString("Truce", "§e[<fcolor>TRUCE§e]§r ")!!.replace("<fcolor>", MConf.get().colorTruce.toString(), false)
        ChatPrefixes.FACTION = chatPrefix.getString("Faction", "§e[<fcolor>FACTION§e]§r ")!!.replace("<fcolor>", MConf.get().colorMember.toString(), false)
        ChatPrefixes.ENEMY = chatPrefix.getString("Enemy", "§e[<fcolor>ENEMY§e]§r ")!!.replace("<fcolor>", MConf.get().colorEnemy.toString(), false)
        ChatPrefixes.NEUTRAL = chatPrefix.getString("Neutral", "§e[<fcolor>NEUTRAL§e]§r ")!!.replace("<fcolor>", MConf.get().colorNeutral.toString(), false)
        ChatPrefixes.LOCAL = chatPrefix.getString("Local", "§e[§rLOCAL§e]§r ")!!
        ChatPrefixes.GLOBAL = chatPrefix.getString("Global", "§e[§6GLOBAL§e]§r ")!!
        ChatPrefixes.STAFF = chatPrefix.getString("Staff", "§e[§4STAFF§e]§r ")!!
        ChatPrefixes.WORLD = chatPrefix.getString("World", "§e[§3WORLD§e]§r ")!!
        TextColors.initialize(config.getConfigurationSection("TextColors")!!)
        //Check for Factions
        val f = server.pluginManager.getPlugin("Factions")
        if (f is Factions) {
            logger.info("Factions detected")

            factionsPlugin = f
        }
        //If factions were either not installed or is disabled, disable this plugin
        if (factionsPlugin == null || !factionsPlugin!!.isEnabled) {
            logger.severe("Factions is required, but was neither found or is disabled")
            logger.severe("Factions3Chat will therefore be disabled")
            server.pluginManager.disablePlugin(this)
            return
        }
        //Check for DiscordSRV
        val dsrv = server.pluginManager.getPlugin("DiscordSRV")
        if (dsrv is DiscordSRV) {
            logger.info("DiscordSRV detected")
            discordSrvPlugin = dsrv
            DiscordSRV.api.subscribe(DiscordSRVListener)
        }
        //Check for Essentials
        val ess = server.pluginManager.getPlugin("Essentials")
        if (ess is Essentials) {
            logger.info("Essentials detected")
            essentialsPlugin = ess
        }
        server.pluginManager.registerEvents(ChatListener, this)
        server.pluginManager.registerEvents(UpdateManager, this)
        UpdateManager.runTaskTimerAsynchronously(this, 0, 10000000000)
    }

    override fun onLoad() {
        instance = this
    }

    fun saveChatModesFile() {
        val chatmodes = File(dataFolder, "chatmodes.yml")
        if (!chatmodes.exists()) {
            chatmodes.createNewFile()
        }
        val writer = PrintWriter(FileOutputStream(chatmodes))
        for (entry in chatModes) {
            writer.println(entry.key.toString() + ": " + entry.value.name)
        }
        writer.close()
    }

    fun updateConfig() {
        val cfg = YamlConfiguration()
        cfg.load(getResource("config.yml")!!.reader())
        try {
            if (File(getDataFolder().toString() + "/config.yml").exists()) {
                var changesMade = false
                val tmp = YamlConfiguration()
                tmp.load(getDataFolder().toString() + "/config.yml")
                for (str in cfg.getKeys(true)) {
                    if (!tmp.getKeys(true).contains(str)) {
                        tmp[str!!] = cfg.get(str)
                        changesMade = true
                    }
                }
                if (changesMade) tmp.save(getDataFolder().toString() + "/config.yml")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
    }


    companion object {
        lateinit var instance : Main
    }
}