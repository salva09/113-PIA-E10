package view

import java.io.File
import kotlin.collections.ArrayList

data class Config(val name: String, var value: Boolean)

private val preferences = ArrayList<Config>()

private fun setDefaultPreferences() {
    preferences.clear()
    preferences.add(Config("welcome", true))
    preferences.add(Config("dark", false))
    preferences.add(Config("experimental", false))
}

fun setPreferences(preferences: ArrayList<Config>) {
    val file = File("config.txt")
    var config = ""
    preferences.forEach {
        config += "${it.name}:${it.value}\n"
    }
    file.writeText(config)
}

fun getPreferences(): ArrayList<Config> {
    val file = File("config.txt")

    if (file.exists()) {
        file.forEachLine {
            val pref = it.split(":")
            preferences.add(Config(pref[0], pref[1].toBoolean()))
        }
    } else {
        file.createNewFile()
        setDefaultPreferences()
        setPreferences(preferences)
    }
    return preferences
}
