package com.parrosz.carecarb.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.parrosz.carecarb.data.local.entity.CareCarbEntity

@Database(
    entities = [CareCarbEntity::class] ,
    version = 3 ,
    exportSchema = false
)
abstract class CareCarbDatabase : RoomDatabase() {
    abstract fun careCarbDao(): CareCarbDao

    companion object {
        @Volatile
        private var INSTANCE: CareCarbDatabase? = null

        @JvmStatic
        fun getInstance(context : Context): CareCarbDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext ,
                    CareCarbDatabase::class.java , "carecarb.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}