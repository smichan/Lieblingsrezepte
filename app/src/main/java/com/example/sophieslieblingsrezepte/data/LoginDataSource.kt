package com.example.sophieslieblingsrezepte.data

import android.util.Base64
import android.util.JsonReader
import com.example.sophieslieblingsrezepte.data.model.LoggedInUser
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            val future: CompletableFuture<JSONObject>? = CompletableFuture.supplyAsync {
                var body: JSONObject = JSONObject("{}")
                val url = URL("https://auth.norsecorby.de/v2/token")
                val connection = url.openConnection() as HttpURLConnection
                try {
                    val auth: String = username + ":" + password
                    val encodedAuth: ByteArray = Base64.encode(auth.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT)
                    val authHeaderValue = "Basic " + String(encodedAuth)
                    connection.setRequestProperty("Authorization", authHeaderValue);
                    val responseCode = connection.responseCode
                    println(responseCode)
                    val inputString = connection.inputStream.bufferedReader().use { it.readText() }
                    body = JSONObject(inputString)
                    val token = body.get("token")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                finally {
                    connection.disconnect()
                }
                return@supplyAsync body
            }

            var token: String? = null
            if (future != null) {
                val body: JSONObject = future.get()
                token = body.get("token") as String?
            }
            if (token != null && token.length > 0)
            {
                val user = LoggedInUser(userId = token, displayName = username)
                return Result.Success(user)
            }
            else{
                return Result.Error(IOException("Error logging in"))
            }

        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}