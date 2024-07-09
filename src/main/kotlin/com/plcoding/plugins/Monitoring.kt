package com.plcoding.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.slf4j.event.Level


import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.*


fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

}
