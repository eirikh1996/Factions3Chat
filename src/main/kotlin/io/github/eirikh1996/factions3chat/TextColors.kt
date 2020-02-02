package io.github.eirikh1996.factions3chat

import com.massivecraft.factions.entity.MConf
import org.bukkit.configuration.ConfigurationSection

object TextColors {
    lateinit var ALLY : String
    lateinit var TRUCE : String
    lateinit var FACTION : String
    lateinit var NEUTRAL : String
    lateinit var ENEMY : String
    lateinit var LOCAL : String
    lateinit var GLOBAL : String
    lateinit var STAFF : String
    lateinit var WORLD : String
    fun initialize (cfg : ConfigurationSection) {
        ALLY = cfg.getString("Ally", "<fcolor>")!!.replace("<fcolor>", MConf.get().colorAlly.toString())
        TRUCE = cfg.getString("Truce", "<fcolor>")!!.replace("<fcolor>", MConf.get().colorTruce.toString())
        FACTION = cfg.getString("Faction", "<fcolor>")!!.replace("<fcolor>", MConf.get().colorMember.toString())
        NEUTRAL = cfg.getString("Neutral", "<fcolor>")!!.replace("<fcolor>", MConf.get().colorNeutral.toString())
        ENEMY = cfg.getString("Enemy", "<fcolor>")!!.replace("<fcolor>", MConf.get().colorEnemy.toString())
        LOCAL = cfg.getString("Local", "§r")!!
        GLOBAL = cfg.getString("Global", "§6")!!
        STAFF = cfg.getString("Staff", "§4")!!
        WORLD = cfg.getString("World", "§3")!!
    }

    fun getColor(cm : ChatMode) : String {
        when (cm) {
            ChatMode.ALLY -> return ALLY
            ChatMode.TRUCE -> return TRUCE
            ChatMode.FACTION -> return FACTION
            ChatMode.LOCAL -> return LOCAL
            ChatMode.GLOBAL -> return GLOBAL
            ChatMode.STAFF -> return STAFF
            ChatMode.ENEMY -> return ENEMY
            ChatMode.NEUTRAL -> return NEUTRAL
            ChatMode.WORLD -> return WORLD
        }
    }
}