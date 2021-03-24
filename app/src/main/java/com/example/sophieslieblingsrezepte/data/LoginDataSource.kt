package com.example.sophieslieblingsrezepte.data

import android.util.Base64
import com.example.sophieslieblingsrezepte.data.model.LoggedInUser
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
            val future: CompletableFuture<String>? = CompletableFuture.supplyAsync {
                var body: String = ""
                val url = URL("https://auth.norsecorby.de/v2/token")
                val connection = url.openConnection() as HttpURLConnection
                try {
                    val auth: String = username + ":" + password
                    val encodedAuth: ByteArray = Base64.encode(auth.toByteArray(StandardCharsets.UTF_8), Base64.DEFAULT)
                    val authHeaderValue = "Basic " + String(encodedAuth)
                    connection.setRequestProperty("Authorization", authHeaderValue);
                    val responseCode = connection.responseCode
                    println(responseCode)
                    body = connection.inputStream.bufferedReader().use { it.readText() }
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
                token = future.get();
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