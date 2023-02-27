package abhishek.mathur.favdish.model.database

import abhishek.mathur.favdish.model.entities.FavDish
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    @Insert
    fun insertFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISH_TABLE ORDER BY ID")
    fun getAllDishesList() : Flow<List<FavDish>>

    @Update
    fun updateFavDishDetails(favDish: FavDish)

    @Delete
    fun deleteFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISH_TABLE WHERE favorite_dish = 1")
    fun getFavouriteDishesList() : Flow<List<FavDish>>

    @Query("SELECT * FROM FAV_DISH_TABLE WHERE TYPE = :filterType")
    fun getFilteredDishesList(filterType : String) : Flow<List<FavDish>>
}