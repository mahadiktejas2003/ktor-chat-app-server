package com.abc.room

import io.ktor.websocket.*

//the packege is room for the chat room
data class Member(

    //member is a single user who is currenyly in our chat room
    //this member needs 3 fields: username, sessionId-it's a unique identifier for a specific session, as long as the member is connected
    //and it'll need websocketsession-This socket is what we useto actually send something to that membernd in the android app we'll then receive that message and display it corresponsdingly in our Chat List
    val username: String,
    val sessionId: String,
    val socket: WebSocketSession

    //but before this Chat/Room controller is CustomExceptions class.->define it in Room package as:-MemberAlreadyExistsExcpetion
    //Go into that file now


)
