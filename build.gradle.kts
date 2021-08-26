import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.4.32"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "cat.kiwi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url="https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
    implementation("redis.clients:jedis:3.6.3")
}
tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("AgendaAntiSpam")
        dependencies {
            exclude(dependency("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT"))
        }
    }
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}