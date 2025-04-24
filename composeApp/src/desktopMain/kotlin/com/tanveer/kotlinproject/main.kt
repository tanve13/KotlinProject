package com.tanveer.kotlinproject


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import okhttp3.*
import org.apache.poi.ss.usermodel.*
import java.io.*


fun main() = application {
    val answer = "Student's Answer Here"
    val rubric = "Rubric Criteria Here"
    val selectedFiles = mapOf("Rubric" to "path/to/rubric.xlsx")
    Window(
        onCloseRequest = ::exitApplication,
        title = "KotlinProject"
    ) {
        var isLoggedIn by remember { mutableStateOf(false) }

        if (!isLoggedIn) {
            AuthScreen(onLoginSuccess = { isLoggedIn = true })
        } else {
            App(answer, rubric, selectedFiles)
        }
    }
}
