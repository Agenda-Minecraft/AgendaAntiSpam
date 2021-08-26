package cat.kiwi.minecraft.agenda.antispam

import cat.kiwi.minecraft.agenda.antispam.database.RedisClient
import cat.kiwi.minecraft.agenda.antispam.listener.Events
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin


class AntiSpam : JavaPlugin() {
    companion object {
        lateinit var instance: AntiSpam
    }

    override fun onEnable() {
        instance = this

        try {
            Config.readConfig()
        } catch (e: Exception) {
            logger.info("Cannot read configuration file!")
            Bukkit.getPluginManager().disablePlugin(this)
        }
        saveConfig()

        getCommand("aas")!!.setExecutor(AASCmd())

        logger.info("AgendaAntiSpam is enabled!")
        server.pluginManager.registerEvents(Events(), this)

        RedisClient.init()
    }

    override fun onDisable() {
        logger.info("AgendaAntiSpam is disabled!")
    }
}

