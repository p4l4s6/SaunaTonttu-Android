package tech.cognix.sauntatonttu.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPreferencesApp(context: Context) {

    private var preferences: SharedPreferences =
        context.getSharedPreferences("saunatonttu", MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()


    fun saveText(Key: String?, Value: String?) {
        editor.putString(Key, Value).apply()
    }

    fun saveToken(value: String?) {
        editor.putString(Constants.TOKEN, "token $value").apply()
    }

    fun saveNumber(Key: String?, Value: Int) {
        editor.putInt(Key, Value).apply()
    }

    fun saveBoolean(Key: String?, Value: Boolean) {
        editor.putBoolean(Key, Value).apply()
    }

    fun getToken(): String? {
        return preferences.getString(Constants.TOKEN, null)
    }

    fun getNumber(Key: String?, defaultValue: Int): Int {
        return preferences.getInt(Key, defaultValue)
    }

    fun getText(key: String): String? {
        return preferences.getString(key, "")
    }

    fun getBoolean(Key: String?, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(Key, defaultValue)
    }

    fun clear() {
        editor.clear()
        editor.remove(Constants.TOKEN)
        editor.remove(Constants.USER)
        editor.commit()
    }

    companion object {
        fun getInstance(context: Context): SharedPreferencesApp {
            return SharedPreferencesApp(context)
        }
    }


}
