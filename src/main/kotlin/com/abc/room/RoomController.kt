package com.abc.room

import com.abc.MessageDataSource
import com.abc.data.model.Message
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController (
    private val messageDataSource: MessageDataSource
    //pass the interface (MessageDataSource) because in this room controller when we send a messgep we off Course want to insert that in our Datahase

){
    private val members= ConcurrentHashMap<String   ,Member>()
    //we're gonna use the concurrentHashMap ->to save our members that are currently connected to our Chat (Room)
    //Keys are strings  are the Usernames which are unique , and the values are our Members


    //=>fxns needed in room controller- onJoin-so when new members actually joins a room-it'll take the username of the  member, sessionId
    //means just everything to create such a member, and also required Socket- which is a websocketsession
    fun onJoin(
        username: String,
        sessionId: String,
        socket: WebSocketSession
    ) {

        //Now when members actually joins the room-> Check if there's alerady a member with that username in our HashMap, if so then throw the Exception we just have created
        //i.e. throw- MemberAlreadyExistsException
        if (members.containsKey(username)) {
            throw MemberAlreadyExistsException()
        }
        //else case-
        members [username] = Member (
            username =username,
            sessionId=sessionId,
            socket=socket
        )
    }
    //we also need a fxn to actually send a message
    //so when a specific member  sends the message->we wanna broadcast that to all the members in this Room(Chat Room)
    //So it's called as -sendMessage

 suspend fun sendMessage(senderUserName:String, message: String) {
        //here we need to know who sent that messsage-i.e. user->bcz we attach the sender username to that message value-
        //so that everyone is displayed p[roperly who sent that message.
//and we also want to know the message we actually wanna send-so message as paramerter here


        //=>now create the message model for our database.

        members.values.forEach{member->
        //so wwe can take the members hashmap/list=>
        //for every single memebrs in members hashmap->Do the below.
            val messageEntity=Message(
                text=message,
                username=senderUserName,
                timestamp = System.currentTimeMillis()
                //and Id will be auto. crreated
            )
            messageDataSource.insertMessage(messageEntity)//since that's a suspend function-we need to execute that in the coroutine
            //  so make the fun sendMessage -> Suspend fun sendMessage

            //user messargeDataSource and insert that message into the messageEntity
        //till now we just have saved message in the database.

          //  we want to parse that messssage to a json string->and then send that json string to all the memebber in that room
            //so create the val parsedMessage

            val parsedMessage=Json.encodeToString(messageEntity)
            member.socket.send(Frame.Text(parsedMessage)) //, take socket of that member and we send the frame-Frame is something we can send via the Web Sockets
            //we wanna send string frame here-ie. Text. U may also send other types of frames like byte frames,send File data..
            //That's how we send the message using web sockets over the network
        }

    }

    // fxn to access the All messages, and read these messages from the database
    suspend fun getAllMessages():List<Message> {
        //returns the list of Messages
        return messageDataSource.getAllMessages()

    }
    //and if member decides to disconnect -if they leave the app or so-then have the Disconnext fxn
    //calll this fxn as "tryDisconnect" becz if member is not in the room-there's nobody to Disconnect.

    suspend fun tryDisconnect(username: String) {

        members[username]?.socket?.close()
        //then send the username who we wanna disconnect.->ie. members[username],
        //key -username is used here.
        //.socket?.close= means we wanna close the socket connection to that specific member.

        if(members.containsKey(username))//if therre's a key in the memebers list-" Username"  then say
            // that - member.remove that username
        {
            members.remove(username)
            //so here we just removed the member from our hashmap.

        }
    }
     //this's is it for the Room Controller!
}


