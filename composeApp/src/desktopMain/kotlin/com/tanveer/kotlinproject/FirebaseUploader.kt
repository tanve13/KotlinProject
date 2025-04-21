package com.tanveer.kotlinproject

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.*
import java.sql.DriverManager.println
import java.time.Instant

val apiKey = "AIzaSyD5rqCR7rWmllecnwiiZWHkgU1pqLdPJX8"
val projectId = "edumark-ai-812f7"

suspend fun saveEvaluationToFireStore(
    studentName: String,
    studentId: String,
    overallScore: String,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    val timestamp = Instant.now().toString()

    val body = buildJsonObject {
        put("fields", buildJsonObject {
            put("studentName", buildJsonObject { put("stringValue", studentName) })
            put("studentId", buildJsonObject { put("stringValue", studentId) })
            put("overallScore", buildJsonObject { put("stringValue", overallScore) })
            put("timestamp", buildJsonObject { put("stringValue", timestamp) })
        })
    }

    val url = "https://firestore.googleapis.com/v1/projects/$projectId/databases/(default)/documents/evaluations?key=$apiKey"

    try {
        val response = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        println("Firebase response: ${response.status}")
        onSuccess()
    } catch (e: Exception) {
        println("Firebase error: ${e.message}")
        onFailure()
    } finally {
        client.close()
    }
}
