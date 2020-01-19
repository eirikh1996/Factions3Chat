package io.github.eirikh1996.factions3chat

object ChatPrefixes {
    var ALLY = "[ALLY]"
    var TRUCE = "[TRUCE]"
    var FACTION = "[FACTION]"
    var NEUTRAL = "[NEUTRAL]"
    var ENEMY = "[ENEMY]"
    var LOCAL = "[LOCAL]"
    var GLOBAL = "[GLOBAL]"
    var STAFF = "[STAFF]"

    fun getPrefix(cm : ChatMode) : String {
        when (cm) {
            ChatMode.ALLY -> return ALLY
            ChatMode.TRUCE -> return TRUCE
            ChatMode.FACTION -> return FACTION
            ChatMode.LOCAL -> return LOCAL
            ChatMode.GLOBAL -> return GLOBAL
            ChatMode.STAFF -> return STAFF
            ChatMode.ENEMY -> return ENEMY
            ChatMode.NEUTRAL -> return NEUTRAL
        }
    }
}