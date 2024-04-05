package com.abc

import com.abc.di.mainModule
import com.abc.plugins.*
import io.ktor.server.application.*

import org.koin.ktor.plugin.Koin


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module( ){
//Plugin to Configure Koin (in the MainModule in di package)
    install(Koin){

        modules(mainModule)// define the module to the path
    }

    //Now run the Server -if u want ->by clicking play icon in fun main here
    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}
