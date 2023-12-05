package com.example.myrecipeapp

import LanguageManager
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import java.util.*

class MyApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }

    private fun updateBaseContextLocale(context: Context): Context {
        val language = LanguageManager.currentLanguage(context)
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)

        // Log the default locale
        Log.d("LanguageManager", "Default locale: ${Locale.getDefault().language}")


        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale)
        } else {
            context.createConfigurationContext(configuration)
        }
    }

    @Suppress("deprecation")
    private fun updateResourcesLocale(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}

