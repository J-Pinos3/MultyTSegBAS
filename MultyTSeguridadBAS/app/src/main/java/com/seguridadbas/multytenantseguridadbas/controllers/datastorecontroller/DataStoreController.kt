package com.seguridadbas.multytenantseguridadbas.controllers.datastorecontroller

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.seguridadbas.multytenantseguridadbas.model.UserDataStore
import kotlinx.coroutines.flow.map

const val USER_DATASTORE = "USER_LOGIN_DATA"


val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)

class DataStoreController(val context: Context) {

    companion object{
        val TOKEN = stringPreferencesKey("TOKEN")
        val ID = stringPreferencesKey("ID")
        val EMAIL = stringPreferencesKey("EMAIL")
        val FIRSTNAME = stringPreferencesKey("FIRSTNAME")
        val LASTNAME = stringPreferencesKey("LASTNAME")
    }


    suspend fun saveToDataStore(userDataStore: UserDataStore){
        context.preferencesDataStore.edit { preferences ->

            preferences[TOKEN] = userDataStore.token
            preferences[ID] = userDataStore.id
            preferences[EMAIL] = userDataStore.email
            preferences[FIRSTNAME] = userDataStore.firstName
            preferences[LASTNAME] = userDataStore.lastName
        }
    }


    fun getDataFromStore() = context.preferencesDataStore.data.map {
        UserDataStore(
            token = it[TOKEN] ?: "",
            id = it[ID] ?: "",
            email = it[EMAIL] ?: "",
            firstName = it[FIRSTNAME] ?: "",
            lastName = it[LASTNAME] ?: ""

        )
    }


    suspend fun clearDataStore() = context.preferencesDataStore.edit { preferences ->
        preferences.clear()
    }
}