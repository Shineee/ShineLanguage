package com.shine.language.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.shine.language.MainApplication
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

object DataStore {
    const val isCreateDB = "is_create_db"

    @OptIn(DelicateCoroutinesApi::class)
    fun putBoolean(key: String, value: Boolean) {
        val context = MainApplication.instance.applicationContext
        GlobalScope.launch {
            context.dataStore.edit {
                it[booleanPreferencesKey(key)] = value
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getBoolean(key: String, callback: ((Boolean) -> Unit)): Flow<Boolean> {
        val context = MainApplication.instance.applicationContext
        val exampleCounterFlow: Flow<Boolean> = context.dataStore.data.map {
            // 无类型安全
            it[booleanPreferencesKey(key)] ?: false
        }
        GlobalScope.launch {
            exampleCounterFlow.collect {
                callback.invoke(it)
            }
        }
        return exampleCounterFlow
    }
}