package com.abc.plugins

import com.abc.room.RoomController
import com.abc.routes.chatSocket
import com.abc.routes.getAllMessages

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val roomController by inject<RoomController>()
    install(Routing) {
        chatSocket(roomController)
        getAllMessages(roomController)
    }
}
