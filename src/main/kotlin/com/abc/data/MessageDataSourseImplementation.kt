package com.abc.data

import com.abc.MessageDataSource
import com.abc.data.model.Message
import org.litote.kmongo.coroutine.CoroutineDatabase

class MessageDataSourseImplementation(
    //ctor argument:
    //which is our actual database instance-and that will be the coroutine database
    private val db: CoroutineDatabase

): MessageDataSource {
    val messages=db.getCollection<Message>() //here type is Message of collection



    override suspend fun getAllMessages(): List<Message> {
        return messages.find() //don't pass any argurments in find if u wanna get all messages, which we want
            .descendingSort(Message::timestamp)//we just want to sort this descending by a date so we just pass our timestamp and here in k-mongo we can simply do that using the actual entity class double colon specifying the field we
                                                                        //        want to sort by which is the 'timestamp'
             .toList()//Eazy                                                                                                                                                                     it is

    }

    override suspend fun insertMessage(message: Message) {
       messages.insertOne(message)
    }
}