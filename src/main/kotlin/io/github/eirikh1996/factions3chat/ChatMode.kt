package io.github.eirikh1996.factions3chat

import java.lang.IllegalArgumentException

enum class ChatMode {
    FACTION, ALLY, TRUCE, NEUTRAL, ENEMY, LOCAL, GLOBAL, STAFF;


    companion object {
        val BY_NAME = HashMap<String, ChatMode>()
        init {
            for (cm in values()) {
                BY_NAME.put(cm.name, cm)
                BY_NAME.put(cm.name[0].toString(), cm)
            }
        }

        /**
         * Returns the chat mode by name or first letter
         * @return the chat mode by name or first letter
         * @throws IllegalArgumentException if invalid chat mode is supplied
         */
        fun getChatMode(value : String) : ChatMode? {
            return BY_NAME.get(value)
        }
    }

}