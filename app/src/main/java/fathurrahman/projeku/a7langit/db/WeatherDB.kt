package fathurrahman.projeku.a7langit.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fathurrahman.projeku.a7langit.db.table.FavoriteTable

@Database(entities = [FavoriteTable::class], version = 1)
abstract class WeatherDB : RoomDatabase(){

    companion object {
        private var INSTANCE: WeatherDB? = null

        fun getDatabase(context: Context): WeatherDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, WeatherDB::class.java, "WeatherDB")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as WeatherDB
        }
    }

    abstract fun daoFavorite() : daoFavorite
}