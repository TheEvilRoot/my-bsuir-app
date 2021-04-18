package com.theevilroot.mybsuir.login

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.theevilroot.mybsuir.common.ApiService
import com.theevilroot.mybsuir.common.cache.ICacheManager
import com.theevilroot.mybsuir.common.data.LoginRequest
import com.theevilroot.mybsuir.common.data.LoginResult
import com.theevilroot.mybsuir.common.encryption.base.IEncryptionLayer
import com.theevilroot.mybsuir.login.data.UserCache
import java.io.File

class LoginModel (
    context: Context,
    val api: ApiService,
    val encryptionStack: List<IEncryptionLayer>,
    val cacheManager: ICacheManager
) {

    private val appContext = context.applicationContext
    private val gson = GsonBuilder().create()

    fun readCacheFile(): UserCache? {
        val file = File(appContext.filesDir, "usercache.dat")
        if (!file.exists()) {
            return null
        }

        val encryptedContent = file.readBytes()

        val obj = try {
            val content = encryptionStack.fold(encryptedContent) { acc, layer -> layer.decrypt(acc) }
            JsonParser.parseString(String(content, Charsets.UTF_8))
        } catch (e: Exception) {
            Log.e("Login", "readCacheFile/decryption failure with ${encryptionStack.size} long stack", e)
            return null
        }

        if (!obj.isJsonObject)
            return null

        return gson.fromJson(obj, UserCache::class.java)
    }

    fun writeCacheFile(userCache: UserCache) {
        val file = File(appContext.filesDir, "usercache.dat")
        val cacheObj = gson.toJsonTree(userCache)
        val content = gson.toJson(cacheObj).toByteArray()

        val encryptedContent =  try {
            encryptionStack.fold(content) { acc, layer -> layer.encrypt(acc) }
        } catch (e: Exception) {
            Log.e("Login", "writeCacheFile/encryption failure with ${encryptionStack.size} long stack", e)
            return
        }
        file.writeBytes(encryptedContent)
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
        cacheManager.markDirty()
    }

}