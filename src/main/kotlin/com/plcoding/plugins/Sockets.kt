package com.plcoding.plugins


import io.ktor.server.application.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.*


fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}