package cat.kiwi.minecraft.agenda.antispam.utils

import cat.kiwi.minecraft.agenda.antispam.Config
import cat.kiwi.minecraft.agenda.antispam.Config.finalAction
import cat.kiwi.minecraft.agenda.antispam.Config.firstTimeAction
import cat.kiwi.minecraft.agenda.antispam.Config.firstTimeCoolDown
import cat.kiwi.minecraft.agenda.antispam.database.RedisClient.redisExpire
import cat.kiwi.minecraft.agenda.antispam.database.RedisClient.redisGet
import cat.kiwi.minecraft.agenda.antispam.database.RedisClient.redisTTL
import cat.kiwi.minecraft.agenda.antispam.database.RedisClient.redisSet
import org.bukkit.event.player.AsyncPlayerChatEvent
import kotlin.random.Random

// REDIS KEY TEMPLATE
val AsyncPlayerChatEvent.PREFIX_UUID: String
    get() {
        return "${Config.redisPrefix}_${this.player.uniqueId}"
    }

val AsyncPlayerChatEvent.PREFIX_UUID_MSGLEN: String
    get() {
        return "${Config.redisPrefix}_${this.player.uniqueId}_${this.message.length}"
    }


val AsyncPlayerChatEvent.PREFIX_UUID_TAGGED: String
    get() {
        return "${Config.redisPrefix}_${this.player.uniqueId}_tagged"
    }


fun AsyncPlayerChatEvent.recordInRedis() {
    if (redisGet(this.PREFIX_UUID) == null) {
        redisSet(this.PREFIX_UUID, "1")
    }
}

fun AsyncPlayerChatEvent.addBasicTTL() {
    val lastTTL = redisTTL(this.PREFIX_UUID)

    redisExpire(
        this.PREFIX_UUID,
        lastTTL + this.message.length / Config.messageLengthWeight + Config.messageBias + Random.nextLong(
            Config.randomFrom, Config.randomTo
        )
    )
}

fun AsyncPlayerChatEvent.antiDuplicate() {
    // IF DUPLICATED

    if (redisGet(this.PREFIX_UUID_MSGLEN) != null) {
        val lastDuplicateTTL = redisTTL(this.PREFIX_UUID_MSGLEN)
        redisExpire(
            this.PREFIX_UUID_MSGLEN,
            lastDuplicateTTL + Config.duplicateBias + Random.nextLong(
                Config.randomFrom, Config.randomTo
            )
        )
    }
    // IF NOT DUPLICATED

    if (redisGet(this.PREFIX_UUID_MSGLEN) == null) {
        redisSet(this.PREFIX_UUID_MSGLEN, "1")
        redisExpire(
            this.PREFIX_UUID_MSGLEN, Config.messageBias + Random.nextLong(
                Config.randomFrom,
                Config.randomTo
            )
        )
    }
}

fun AsyncPlayerChatEvent.checkTTL() {
    if (redisTTL(this.PREFIX_UUID) > Config.tolerableTTL) {
        // Check Tagged
        if (redisGet(this.PREFIX_UUID_TAGGED) == null) { // not tagged

            // Tag
            redisSet(this.PREFIX_UUID_TAGGED, "1")

            redisExpire(
                this.PREFIX_UUID_TAGGED, firstTimeCoolDown + Random.nextLong(
                    Config.randomFrom, Config.randomTo
                )
            )
            // Clear TTL
            redisExpire(this.PREFIX_UUID, 0L)

            consoleCMD(firstTimeAction.replace("%player%", this.player.name))
        } else { // Tagged
            consoleCMD(finalAction.replace("%player%", this.player.name))
            // Reset All
            redisExpire(this.PREFIX_UUID, 0L)
            redisExpire(this.PREFIX_UUID_TAGGED, 0L)
        }
    }
}


