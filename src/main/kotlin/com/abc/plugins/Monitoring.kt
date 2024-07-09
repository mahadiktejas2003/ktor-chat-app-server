package com.abc.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.slf4j.event.Level


import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*


fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

}
