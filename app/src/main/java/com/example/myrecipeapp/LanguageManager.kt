
import android.content.Context
import android.os.LocaleList
import android.preference.PreferenceManager
import android.util.Log
import java.util.Locale

object LanguageManager {
    private const val LANGUAGE_KEY = "language"

    fun currentLanguage(context: Context): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val currentLanguage = sharedPreferences.getString(LANGUAGE_KEY, "es") ?: "es"
        Log.d("LanguageManager", "Current language: $currentLanguage")
        return currentLanguage
    }

    fun updateLanguage(language: String, context: Context) {
        Log.d("LanguageManager", "Updating language to: $language")

        // Determine the resource folder based on the selected language
        val lang = when (language) {
            "Urdu" -> "ur"
            "Spanish" -> "es"
            "French" -> "fr"
            else -> "en" // Default to English if the language is not recognized
        }
        Log.d("LanguageManager", " language code to: $lang")

        val resources = context.resources
        val configuration = resources.configuration
        val locale = Locale(lang)
        Locale.setDefault(locale)
        configuration.setLocale(locale)
        configuration.setLocales(LocaleList(locale))

        // Update the configuration with the appropriate resource folder
        configuration.setLocale(Locale(lang))
        configuration.setLocales(LocaleList(locale))

        resources.updateConfiguration(configuration, resources.displayMetrics)

        // Save the selected language in SharedPreferences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putString(LANGUAGE_KEY, lang).apply()
    }
}
