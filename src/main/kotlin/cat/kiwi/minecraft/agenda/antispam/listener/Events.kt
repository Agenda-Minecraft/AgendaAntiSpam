package cat.kiwi.minecraft.agenda.antispam.listener

import cat.kiwi.minecraft.agenda.antispam.utils.addBasicTTL
import cat.kiwi.minecraft.agenda.antispam.utils.antiDuplicate
import cat.kiwi.minecraft.agenda.antispam.utils.checkTTL
import cat.kiwi.minecraft.agenda.antispam.utils.recordInRedis
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class Events : Listener {
    @EventHandler(ignoreCancelled = true)
    fun onAsyncPlayerChatEvent(e: AsyncPlayerChatEvent) {
        // Read the code, no comments here.

        if (e.player.hasPermission("aas.bypass")) return

        e.recordInRedis()

        e.addBasicTTL()

        e.antiDuplicate()

        e.checkTTL()

    }
}