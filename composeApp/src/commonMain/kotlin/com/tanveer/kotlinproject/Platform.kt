package com.tanveer.kotlinproject

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform