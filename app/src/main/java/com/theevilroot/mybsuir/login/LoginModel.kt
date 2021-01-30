package com.theevilroot.mybsuir.login

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.data.LoginRequest
import com.theevilroot.mybsuir.common.data.LoginResult
import com.theevilroot.mybsuir.login.data.UserCache
import java.io.File

class LoginModel (context: Context, val api: ApiService) {

    private val appContext = context.applicationContext
    private val gson = GsonBuilder().create()

    fun readCacheFile(): UserCache? {
        val file = File(appContext.filesDir, "usercache.dat")
        if (!file.exists()) {
            return null
        }

        val content = file.readText()
        val obj = JsonParser.parseString(content)
        if (!obj.isJsonObject)
            return null

        return gson.fromJson(obj, UserCache::class.java)
    }

    fun writeCacheFile(userCache: UserCache) {
        val file = File(appContext.filesDir, "usercache.dat")
        val cacheObj = gson.toJsonTree(userCache)
        file.writeText(gson.toJson(cacheObj))
    }

    fun checkUserToken(token: String): Boolean {
        val call = api.personalInformation(token).execute()
        return call.code() == 200
    }

    fun login(username: String, password: String, saveToken: Boolean, saveCredentials: Boolean): LoginResult? {
        val call = api.login(LoginRequest(username, password)).execute()
        if (call.code() == 200) {
            val body = call.body() ?:
                    return null
            if (!body.loggedIn) {
                return if (body.message.contains("Invalid Credentials"))
                    LoginResult(body, "Неверные имя пользователя или пароль")
                else LoginResult(body, "Не удалось войти: сервис временно недоступен")
            }

            val cookie = call.headers()["Set-Cookie"]
                ?: return LoginResult(body, "Невозможно получить токен авторизации от сервера")

            if (saveToken) {
                writeCacheFile(if (saveCredentials)
                    UserCache(cookie, username to password)
                else UserCache(cookie))
            }
            return LoginResult(body, "Вход успешен", cookie)
        }
        return null
    }

    fun logout() {
        val file = File(appContext.filesDir, "usercache.dat")
        if (file.exists())
            file.delete()
    }

}