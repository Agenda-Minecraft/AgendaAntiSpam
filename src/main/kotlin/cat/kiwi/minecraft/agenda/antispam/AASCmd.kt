package cat.kiwi.minecraft.agenda.antispam

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class AASCmd : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean =
        with(AntiSpam.instance) {
            try {
                when (args[0]) {
                    "reload" -> Config.readConfig().also {
                        sender.sendMessage(infoPrefix("Config file reloaded."))
                    }
                    else -> sender.sendMessage(helpInfo)
                }
            } catch (e: Exception) {
                sender.sendMessage(errorPrefix("Please check args."))
            }
            true
        }
    private val helpInfo = infoPrefix("/aas reload - reload config (exclude database connection)\n")

    private fun infoPrefix(string: String) =
        with(AntiSpam.instance) {
            "${ChatColor.AQUA} [AgendaAntiSpam] ${ChatColor.GOLD}$string"
        }

    private fun errorPrefix(string: String) =
        with(AntiSpam.instance) {
            "${ChatColor.RED} [AgendaAntiSpam] ${ChatColor.GRAY}$string"
        }
}