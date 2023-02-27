package abhishek.mathur.favdish.model.database

import abhishek.mathur.favdish.model.entities.FavDish
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(FavDish::class), version = 1, exportSchema = false)
abstract class FavDishRoomDatabase : RoomDatabase() {

    abstract fun favDishDao() : FavDishDao

    companion object{


        //Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE : FavDishRoomDatabase? = null

        fun getDatabase(context: Context) : FavDishRoomDatabase{
            //If Instance is not null then return it,
            //if it is, then create the database
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavDishRoomDatabase::class.java,
                    "fav_dish_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                //Return Instance
                instance
            }
        }
    }
}