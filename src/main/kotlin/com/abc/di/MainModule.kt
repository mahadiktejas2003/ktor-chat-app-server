package com.abc.di

import com.abc.MessageDataSource
import com.abc.data.MessageDataSourseImplementation
import com.abc.room.RoomController
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

//define the mainmodule - that's what we do with Koin.
val mainModule=module{

    //here define the dependedency that we actually provide
    //we have our Database instance- so we can say-single->so we can define the singleton as single{}
    single{
        val mongoUri = System.getenv("MONGODB_URI") ?: "mongodb://mongodb:27017/message_db_y"
        KMongo.createClient(mongoUri)//kmongo-from reactiveStreams
            .coroutine//this'll create such a Coroutine Client
            .getDatabase("message_db_y")//then we can say- getDatabase to get this  Coroutine Database we talked about when we provide a name
                                    //so we can say- message_db_yt->because we already have a message db->(we her say as-message_db_y only )
                                        //so that's how we provide a Database instance

    }

    //Now provide a message datasource-so another Single->
    //return the messageDataSourceImplementation

    single<MessageDataSource>{
        MessageDataSourseImplementation(get())

        //Now how to get the coroutine database here
        //becuase the Koin knows how to   crate that->so simply say->GET(/get())->and it'll auto. insert that
    }

    //now we need a Roomcontroller  is needed

    single{
        RoomController(get())//we know how to create that-GET/get

    }

}

//Now let's have a plugin to Configure Koin ->go to the Application.kt class
