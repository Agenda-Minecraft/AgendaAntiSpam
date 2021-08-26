package cat.kiwi.minecraft.agenda.antispam.database

import cat.kiwi.minecraft.agenda.antispam.AntiSpam
import cat.kiwi.minecraft.agenda.antispam.Config.redisAddress
import cat.kiwi.minecraft.agenda.antispam.Config.redisPort
import org.bukkit.Bukkit
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

object RedisClient {
    lateinit var jedis: Jedis
    lateinit var pool: JedisPool
    var pingTimes = 0
    fun init() {
        val jedisPoolConfig = JedisPoolConfig()
        jedisPoolConfig.maxIdle = 8
        jedisPoolConfig.minIdle = 0
        pool = JedisPool(jedisPoolConfig, redisAddress, redisPort, 1000)

        redisPing()
    }

    private fun redisPing() {
        try {
            jedis = pool.resource
        } catch (e: Exception) {
            AntiSpam.instance.logger.info("Could not connect to redis")
            Bukkit.getPluginManager().disablePlugin(AntiSpam.instance)
        }
    }

    fun redisSet(k: String, v: String) {
        try {
            jedis = pool.resource
            jedis.set(k, v)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            jedis.close()
        }
    }

    fun redisGet(k: String): String? {
        try {
            jedis = pool.resource
            return jedis.get(k)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            jedis.close()
        }
        return null
    }

    fun redisTTL(k: String): Long {
        try {
            jedis = pool.resource
            return jedis.ttl(k)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            jedis.close()
        }
        return -1
    }

    fun redisExpire(k: String, t: Long) {
        try {
            jedis = pool.resource
            jedis.expire(k, t)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            jedis.close()
        }
    }
}

