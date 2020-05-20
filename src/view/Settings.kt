package view

import java.io.File

data class Config(val name: String, var value: Boolean)

private val preferences = ArrayList<Config>()

private fun setDefaultPreferences() {
    preferences.clear()
    preferences.add(Config("welcome", true))
    preferences.add(Config("dark", false))
    preferences.add(Config("experimental", false))
}

fun setPreferences() {
    val file = File("config.txt")
    var config = ""
    preferences.forEach {
        config += "${it.name}:${it.value}\n"
    }
    file.writeText(config)
}

fun getPreferences() {
    val file = File("config.txt")

    if (file.exists()) {
        file.forEachLine {
            val pref = it.split(":")
            preferences.add(Config(pref[0], pref[1].toBoolean()))
        }
    } else {
        file.createNewFile()
        setDefaultPreferences()
        setPreferences()
    }
}

fun getConfig(preference: String): Config {
    for (config in preferences) {
        if (config.name == preference) {
            return config
        }
    }
    return Config(preference, false)
}
