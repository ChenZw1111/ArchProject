package com.example.asproj.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CacheDao {
    @Insert(entity = Cache::class, onConflict = OnConflictStrategy.REPLACE)
    fun saveCache(cache:Cache):Long

    @Query("select * from cache where `key` = :key")
    fun getCache(key:String):Cache?

    @Delete(entity = Cache::class)
    fun deleteCache(cache: Cache)
}