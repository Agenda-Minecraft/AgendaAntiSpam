package cat.kiwi.minecraft.agenda.antispam

object Config {
    var redisAddress = "localhost"
    var redisPrefix = "agenda_antispam"
    var redisPort = 6379
    var messageLengthWeight = 2L
    var messageBias = 10L
    var tolerableTTL = 500L
    var duplicateBias = 20L
    var randomFrom = 5L
    var randomTo = 10L
    var firstTimeCoolDown = 300L
    var firstTimeAction = "kick %player% stop spamming"
    var finalAction = "ban %player% spam"

    fun readConfig() = with(AntiSpam.instance.config) {
        options().copyDefaults(true)
        redisAddress = getString("redis-address")!!
        redisPrefix = getString("redis-prefix")!!
        redisPort = getInt("redis-port")
        messageLengthWeight = getLong("message-length-weight")
        messageBias = getLong("message-bias")
        tolerableTTL = getLong("tolerable-ttl")
        duplicateBias = getLong("duplicate-bias")
        randomFrom = getLong("random-from")
        randomTo = getLong("random-to")
        firstTimeCoolDown = getLong("first-time-cooldown")
        firstTimeAction = getString("first-time-action")!!
        finalAction = getString("final-action")!!
    }
}