package abhishek.mathur.favdish.application

import abhishek.mathur.favdish.model.database.FavDishRepository
import abhishek.mathur.favdish.model.database.FavDishRoomDatabase
import android.app.Application

class FavDishApplication : Application() {

    private val database by lazy {
        FavDishRoomDatabase.getDatabase(this@FavDishApplication)
    }

    //A variable for repository
    val repository by lazy {
        FavDishRepository(database.favDishDao())
    }
}