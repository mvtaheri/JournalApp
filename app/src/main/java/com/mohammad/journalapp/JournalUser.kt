package com.mohammad.journalapp

import android.app.Application

class JournalUser:Application() {
    var userName:String?=null
    var userId:String?=null

    companion object{
        var instance:JournalUser?=null
            get() {
                if (field ==null) {
                    field = JournalUser()
                }
                return field
            }
            private set
    }
}