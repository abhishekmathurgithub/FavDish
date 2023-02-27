package abhishek.mathur.favdish.viewmodel

import abhishek.mathur.favdish.model.database.FavDishRepository
import abhishek.mathur.favdish.model.database.FavDishRoomDatabase
import abhishek.mathur.favdish.model.entities.FavDish
import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavDishViewModel(
    //private val repository: FavDishRepository
      application: Application
     ) : AndroidViewModel(application) {


    val repository : FavDishRepository

    init {
        val dao = FavDishRoomDatabase.getDatabase(application).favDishDao()
        repository = FavDishRepository(dao)
    }

    //Launching a new coroutine to insert the data in non - blocking way
    fun insert(dish : FavDish) = viewModelScope.launch(Dispatchers.IO) {
        //call the repository function and pass the detail
        repository.insertFavDishData(dish)
    }

    val allDishesList : LiveData<List<FavDish>> = repository.allDishesList.asLiveData()

    fun update(dish: FavDish) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateFavDishData(dish)
    }

    fun delete(dish: FavDish) = viewModelScope.launch(Dispatchers.IO)
    {
        repository.deleteFavDishData(dish)
    }

    val favouriteDishes : LiveData<List<FavDish>> = repository.favouriteDishes.asLiveData()

    fun getFilteredList(value : String) : LiveData<List<FavDish>> = repository.filteredListDishes(value).asLiveData()
}

/*
class FavDishViewModelFactory(private val repository: FavDishRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDishViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return FavDishViewModel(repository) as T
        }
        throw IllegalAccessException("Unknown ViewModel Class")
    }
}*/
