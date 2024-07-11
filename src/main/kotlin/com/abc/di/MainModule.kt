package com.abc.di

import com.abc.data.MessageDataSource
import com.abc.data.MessageDataSourceImpl
import com.abc.room.RoomController

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {

    //method 1 ->don't work-gives locahost on linux
//    val clientConnection="mongodb://mongodb:27017" //default without docker is->"mongodb://localhost"
//    single {
//        KMongo.createClient(clientConnection)
//            .coroutine
//            .getDatabase("message_db_y")
//    }


    //method 2 ->don't work-gives locahost on linux
//    single {
//        val settings = MongoClientSettings.builder()
////            .applyConnectionString(ConnectionString("mongodb://mongodb:27017"))
//            .build()
//
//        val client = KMongo.createClient(settings).coroutine
//        client.getDatabase("message_db_y")
//    }

    //works
    single {
        val connectionString = System.getenv("MONGODB_URI") ?: "mongodb://localhost:27017"
        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .build()
        val client = KMongo.createClient(settings).coroutine
        client.getDatabase(System.getenv("DB_NAME") ?: "message_db_y")
    }
    single<MessageDataSource> {
        MessageDataSourceImpl(get())
    }
    single {
        RoomController(get())
    }
}