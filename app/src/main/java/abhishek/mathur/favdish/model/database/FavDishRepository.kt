package abhishek.mathur.favdish.model.database

import abhishek.mathur.favdish.model.entities.FavDish
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class FavDishRepository(private val favDishDao : FavDishDao) {

    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish){
        favDishDao.insertFavDishDetails(favDish)
    }

    val allDishesList : Flow<List<FavDish>> = favDishDao.getAllDishesList()

    @WorkerThread
    suspend fun updateFavDishData(favDish: FavDish){
        favDishDao.updateFavDishDetails(favDish)
    }

    @WorkerThread
    suspend fun deleteFavDishData(favDish: FavDish){
        favDishDao.deleteFavDishDetails(favDish)
    }

    val favouriteDishes : Flow<List<FavDish>> = favDishDao.getFavouriteDishesList()

    fun filteredListDishes(value : String) : Flow<List<FavDish>> =
        favDishDao.getFilteredDishesList(value)
}