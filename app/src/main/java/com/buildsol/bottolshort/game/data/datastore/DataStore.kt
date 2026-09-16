package com.buildsol.bottolshort.game.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath

fun DataStore(context: Context): DataStore<Preferences>{
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath.toPath()}
    )

}
internal const val DATA_STORE_FILE_NAME = "setting_bottol_short.preferences_pb"

