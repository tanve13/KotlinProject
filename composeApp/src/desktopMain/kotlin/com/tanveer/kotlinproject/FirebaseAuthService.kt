package com.tanveer.kotlinproject

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.* // or Desktop if using Desktop
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import java.sql.DriverManager.println
import java.util.*

object FirebaseAuthService {
    private const val API_KEY = "AIzaSyD5rqCR7rWmllecnwiiZWHkgU1pqLdPJX8"
    private val client = HttpClient(CIO)

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        val json = buildJsonObject {
            put("email", email)
            put("password", password)
            put("returnSecureToken", true)
        }.toString()

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val response: HttpResponse = client.post("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=$API_KEY") {
                    contentType(ContentType.Application.Json)
                    setBody(json)
                }

                val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject

                if (response.status == HttpStatusCode.OK) {
                    println("Login successful: ${body["email"]}")
                    onResult(true, null)
                } else {
                    val error = body["error"]?.jsonObject?.get("message")?.toString()
                    onResult(false, error ?: "Login failed")
                }
            } catch (e: Exception) {
                println("Login failed: ${e.message}")
                onResult(false, e.message)
            }
        }
    }

    fun signup(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        val json = buildJsonObject {
            put("email", email)
            put("password", password)
            put("returnSecureToken", true)
        }.toString()

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val response: HttpResponse = client.post("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=$API_KEY") {
                    contentType(ContentType.Application.Json)
                    setBody(json)
                }

                val body = Json.parseToJsonElement(response.bodyAsText()).jsonObject

                if (response.status == HttpStatusCode.OK) {
                    println("Signup successful: ${body["email"]}")
                    onResult(true, null)
                } else {
                    val error = body["error"]?.jsonObject?.get("message")?.toString()
                    onResult(false, error ?: "Signup failed")
                }
            } catch (e: Exception) {
                println("Signup failed: ${e.message}")
                onResult(false, e.message)
            }
        }
    }
}
