package abhishek.mathur.favdish.model.network

import abhishek.mathur.favdish.model.entities.RandomDish
import abhishek.mathur.favdish.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomDishApi {

    @GET(Constants.API_ENDPOINT)
    fun getRandomDish(
        @Query(Constants.API_KEY) apiKey : String,
        @Query(Constants.LIMIT_LICENSE) limitLicense : Boolean,
        @Query(Constants.TAGS) tags : String,
        @Query(Constants.NUMBER) number : Int
    ) : Single<RandomDish.Recipes>
}