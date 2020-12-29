package com.example.play2win

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.fatboyindustrial.gsonjodatime.Converters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

 abstract class LocalBaseRepository(private val sharedPreferences: SharedPreferences) : CoroutineScope {

    private val gson by lazy { buildGson() }
    override val coroutineContext: CoroutineContext = Dispatchers.Default

    protected fun <T : Any> getData(key: String, aClass: KClass<T>): T? {
        val dataAsString = sharedPreferences.getString(key, null) ?: return null
        return try {
            val result = gson.fromJson(dataAsString, aClass.java)
            checkClassResult(result, aClass)
            result
        } catch (e: Throwable) {
            Log.e("DEBUG", "can not parse saved data")
            sharedPreferences.edit { putString(key, null) }
            null
        }
    }

    protected fun <T> putData(key: String, newObject: T) {
        try {
            sharedPreferences.edit { putString(key, gson.toJson(newObject)) }
        } catch (e: Throwable) {
            Log.e(e.toString(), "can not save data")
        }
    }

    protected open fun buildGson(): Gson =
        Converters.registerDateTime(GsonBuilder()).create()

    private fun <T : Any> checkClassResult(result: T, aClass: KClass<T>) {
        aClass.java.declaredFields.forEach {
            it.isAccessible = true
            checkNotNull(it.get(result)) { "can not be null" }
        }
    }
}