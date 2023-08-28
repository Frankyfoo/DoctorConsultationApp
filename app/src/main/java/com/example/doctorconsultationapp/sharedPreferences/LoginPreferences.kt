package com.example.doctorconsultationapp.sharedPreferences

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.doctorconsultationapp.ui.LoginActivity
import com.example.doctorconsultationapp.ui.MainActivity
import com.example.doctorconsultationapp.ui.RegisterActivity

class LoginPreferences {
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var context: Context

    companion object {
        val PREF_NAME = "My_Preferences"
        val IS_LOGIN = "isLoggedIn"
        val KEY_USERID = "userId"
        val KEY_EMAIL = "userEmail"
     }

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    fun createLoginSession(userId: String, email: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_USERID, userId)
        editor.putString(KEY_EMAIL, email)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

    fun checkLogin() {
        if (!this.isLoggedIn()) {
            var i = Intent(context, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }
    }

    fun getUserDetails(): HashMap<String, String> {
        var user: Map<String, String> = HashMap<String, String>()
        (user as HashMap)[KEY_USERID] = pref.getString(KEY_USERID, null)!!
        (user as HashMap)[KEY_EMAIL] = pref.getString(KEY_EMAIL, null)!!
        return user
    }

    fun LogoutUser() {
        editor.clear()
        editor.commit()
        var i = Intent(context, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
    }
}