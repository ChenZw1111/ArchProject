package com.example.asproj.cache

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object HiStorage {
    fun <T> saveCache(key:String,body:T){
        val cache = Cache()
        cache.key = key
        cache.data = toByteArray(body)
    }

    fun <T> getCache(key: String):T?{
        val cache = CacheDatabase.get().cacheDao.getCache(key)
        return (if(cache?.data != null){
            toObject(cache.data)
        } else null)as T
    }

    private fun toObject(data: ByteArray?): Any? {
        var bais:ByteArrayInputStream?= null
        var ois : ObjectInputStream?=null
        try{
            bais = ByteArrayInputStream(data)
            ois = ObjectInputStream(bais)
            return ois.readObject()
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            bais?.close()
            ois?.close()
        }
        return null
    }

    private fun <T> toByteArray(body: T): ByteArray? {
        var baos:ByteArrayOutputStream? = null
        var oos:ObjectOutputStream? = null
        try{
            baos = ByteArrayOutputStream()
            oos = ObjectOutputStream(baos)
            oos.writeObject(body)
            oos.flush()
            return baos.toByteArray()
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            baos?.close()
            oos?.close()
        }

        return ByteArray(0)
    }
}