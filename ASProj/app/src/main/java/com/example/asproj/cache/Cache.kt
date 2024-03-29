package com.example.asproj.cache

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache")
class Cache {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    var key:String = ""

    var data:ByteArray? = null
}