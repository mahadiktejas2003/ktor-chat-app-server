package com.abc

import com.abc.di.mainModule
import com.abc.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    // Plugin to Configure Koin (in the MainModule in di package)
    install(Koin) {
        modules(mainModule) // Define the module to the path
    }

    // Set up MongoDB URI
    val mongoUri = System.getenv("MONGODB_URI") ?: "mongodb://mongodb:27017/message_db_y"

    // Now run the Server - if you want -> by clicking the play icon in fun main here
    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}
