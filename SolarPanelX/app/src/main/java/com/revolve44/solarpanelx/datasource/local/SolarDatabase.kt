package com.revolve44.solarpanelx.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.revolve44.solarpanelx.datasource.models.db.ForecastCell
import com.revolve44.solarpanelx.datasource.models.db.HistoryOfForecast
import com.revolve44.solarpanelx.datasource.models.db.SolarStation


@Database(
    entities = [
        SolarStation::class,
        ForecastCell::class,
        HistoryOfForecast::class
    ],
    version = 1
)
abstract class SolarDatabase : RoomDatabase() {
    abstract val solarDao: SolarDao

    companion object{

//        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // Room uses an own database hash to uniquely identify the database
//                // Since version 1 does not use Room, it doesn't have the database hash associated.
//                // By implementing a Migration class, we're telling Room that it should use the data
//                // from version 1 to version 2.
//                // If no migration is provided, then the tables will be dropped and recreated.
//                // Since we didn't alter the table, there's nothing else to do here.
//            }
//        }

        @Volatile
        private var INSTANCE : SolarDatabase? = null

        fun getInstance(context: Context): SolarDatabase {
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SolarDatabase::class.java,
                    "spx_modern_db" // massive_db has been here
                )
                    //.addMigrations(MIGRATION_1_2)
                    .build().also { INSTANCE = it }
            }
        }
    }
}