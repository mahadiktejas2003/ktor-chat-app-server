package com.abc.plugins

import com.abc.session.ChatSession
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.ApplicationPhase.Plugins
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSecurity() {
    data class MySession(val count: Int = 0)
    install(Sessions) {//go to Session package-> ChatSession class
        cookie<ChatSession>("SESSION")
    }

    intercept(Plugins){
        if(call.sessions.get<ChatSession>()==null){
            val username=call.parameters["username"]?:"Guest"
            call.sessions.set(ChatSession(username,generateNonce()))

        }
    }

}
