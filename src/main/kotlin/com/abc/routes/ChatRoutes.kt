package com.abc.routes

import com.abc.room.MemberAlreadyExistsException
import com.abc.room.RoomController
import com.abc.session.ChatSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

//This's also be a functions in Ktor, extednign the 'Route' class. and first one will be the chatSocket class
///=>ChatSocket fxn-need an instance of RoomController
fun Route.chatSocket(roomController: RoomController){

    //we want to be able to Recieve a WebSocket connection here at that specific route-
    //so we say-
    webSocket("/chat-socket"){//here- we can define a path at which we wanna connect-(ie.:  /chat-socket ), ->SO this is just a URL u'll connect to.
        //1st thing we wanna do is-get current session by this user-
        val session=call.sessions.get<ChatSession>()//get the current session by this user=
                                                    //type is ChatSession
                                                        //with this session- get the info about specific user- like the User name or Just the Session ID.

    //If this session is actually nulll->then we wanna clpse this websocket connection bcz then we don't wanna connect to the Chat if we don't have a sesssion.
        //so we say-Close()
        if(session ==null){
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY,"No Session."))//Can pass the close reason- why the sessio is closed ->to know for user.
            return@webSocket  //return out of this websocket block
            }
        //else case-
        //if we have a session->and we can join the Chat

        try{
            roomController.onJoin(

                username = session.username, // username will be session.username
                sessionId=session.sessionId,
                socket=this//socket is 'this'


            )
            //after we actually joined the room-say -incoming.consumeEacch
            //this below piece of code is called everysingle time->there is a message sent to this websocket
            //* So in conslusion-means that if this memebr recieves a message->this below (incoming.consume{...) block of code is
                                                                                // is fired off, with the corresponding frame

            incoming.consumeEach{frame->
                //usually- it'll be a Text frame
                if(frame is Frame.Text){
                    roomController.sendMessage(
                        senderUserName = session.username,
                        message=frame.readText()                            //message that wanna send
                                                                          //=>message=frame.readText()   ->so that's how we decode a frame, and actually get the String as text
                        //so for every single message that comes  from this session here->we just broadcast that to the Whole Room


                    )
                }

            }


        }
        catch (e:MemberAlreadyExistsException){

            //if throws this member already exists exception- then we wanna handle that->so say -call.response...
            call.respond(HttpStatusCode.Conflict)//status code is 'Conflict'
            //so if we recieve a conflict code =>then say that Memebr already exists (this is said by us)
        }
        //=>we also wanna catch other exceptions-
        //when the Socket connection is closed ->it'll throw an exception-so in that case- we can print that
        catch(e:Exception){
            e.printStackTrace()
        }

        finally {//Finally block->defines-What we actually wanna do in each and every case
            //no matter if this is Succeeded or Failed.
            //->After we went through all above Try/catch blocks->We just want to make sure-
            //-that Socket is properly closed so that we properly disconnect from the Chat.
            //so ->tryDisconnect
            roomController.tryDisconnect(session.username)//pass the session username

            //cuz we're not done yet->go to try block

        }


    }

    //so this'll be the defualt websocket Service Sesssion.
    //This (chatSocket fxn) will be called every sing
// le time the Client connects to this route via Websockets.
    //So then we actually get the corresponding Socket connection, we can then attach that to the Member to use to send something to that specific member(means text with other memeber)
    //as this could throw an exception- like memberalready exists exception->put this in the try and catch block -as done in  line no:
}


//now set one more route- to get all the messages->
//it'll need a room controller
fun Route.getAllMessges(roomController: RoomController){
    //here we have a GET request
    get ( "/messages" ){//in get define a Route at which we want to access that-'/message' here
        call.respond(HttpStatusCode.OK,
            roomController.getAllMessages())// we wanna respond with HTTPSTSATUS=OK

        }
}