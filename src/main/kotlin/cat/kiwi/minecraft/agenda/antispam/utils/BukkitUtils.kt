package cat.kiwi.minecraft.agenda.antispam.utils

import cat.kiwi.minecraft.agenda.antispam.AntiSpam
import org.bukkit.Bukkit


fun consoleCMD(string: String) {

    Bukkit.getServer().scheduler.scheduleSyncDelayedTask(
        AntiSpam.instance
    ) {
        AntiSpam.instance.server.dispatchCommand(
            AntiSpam.instance.server.consoleSender,
            string
        )
    }
}
