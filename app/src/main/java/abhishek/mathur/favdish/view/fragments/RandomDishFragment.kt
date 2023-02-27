package abhishek.mathur.favdish.view.fragments

import abhishek.mathur.favdish.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import abhishek.mathur.favdish.databinding.FragmentRandomDishBinding
import abhishek.mathur.favdish.model.entities.FavDish
import abhishek.mathur.favdish.model.entities.RandomDish
import abhishek.mathur.favdish.utils.Constants
import abhishek.mathur.favdish.viewmodel.FavDishViewModel
import abhishek.mathur.favdish.viewmodel.NotificationsViewModel
import abhishek.mathur.favdish.viewmodel.RandomDishViewModel
import android.app.Application
import android.app.Dialog
import android.os.Build
import android.provider.SyncStateContract
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide

class RandomDishFragment : Fragment() {

    private var _binding: FragmentRandomDishBinding? = null
    private lateinit var randomDishViewModel: RandomDishViewModel
    private  var mProgressDialog : Dialog? = null
    private lateinit var viewModel: FavDishViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        randomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)
        randomDishViewModel.getRandomDishFromApi()
        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(Application())).get(
            FavDishViewModel::class.java)


        randomDishViewModelObserver()

        _binding!!.srlRandomDish.setOnRefreshListener {
            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
            randomDishViewModel.getRandomDishFromApi()
        }
    }

    private fun randomDishViewModelObserver(){

        randomDishViewModel.randomDishResponse.observe(
            viewLifecycleOwner,
           Observer { randomDishResponse ->
               randomDishResponse?.let {
                   Log.i("Random Dish Response", "${randomDishResponse.recipes[0]}")

                   if (_binding!!.srlRandomDish.isRefreshing){
                       _binding!!.srlRandomDish.isRefreshing = false
                   }

                   setRandomDishResponseInUI(randomDishResponse.recipes[0])
               }
            }
        )

        randomDishViewModel.randomDishLoadingError.observe(
            viewLifecycleOwner,
            Observer { dataError ->
                dataError?.let {
                    Log.i("Random Dish Api Error", "$dataError")

                    if (_binding!!.srlRandomDish.isRefreshing){
                        _binding!!.srlRandomDish.isRefreshing = false
                    }
                }
            }
        )

        randomDishViewModel.loadRandomDish.observe(
            viewLifecycleOwner,
        Observer { loadRandomDish ->
            loadRandomDish?.let {
                Log.i("Random Dish Loading", "$loadRandomDish")

                if (loadRandomDish && !_binding!!.srlRandomDish.isRefreshing){
                    showCustomProgressDialog()
                }else{
                    hideProgressDialog()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRandomDishResponseInUI(recipe : RandomDish.Recipe){

        Glide.with(requireActivity())
            .load(recipe.image)
            .centerCrop()
            .into(_binding!!.ivDishImage)

        _binding!!.tvTitle.text = recipe.title

        //Default Dish Type
        var dishType : String = "other"

        if (recipe.dishTypes.isNotEmpty()){
            dishType = recipe.dishTypes[0]
            _binding!!.tvType.text = dishType
        }

        //There is no category params present in the response so we will define it as other
        _binding!!.tvCategory.text = "Other"

        var ingredients = ""
        for (value in recipe.extendedIngredients){
            if (ingredients.isEmpty()){
                ingredients = value.original
            }else{
                ingredients = ingredients +", \n" + value.original
            }
        }

        _binding!!.tvIngredients.text = ingredients

        //The instructions or you can say the cooking direction text is in the HTML fomat so we will you the fromHtml to populate it in the textview.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            _binding!!.tvCookingDirection.text = Html.fromHtml(
                recipe.instructions,
                Html.FROM_HTML_MODE_COMPACT
            )
        }else{
            @Suppress("DEPRECIATION")
            _binding!!.tvCookingDirection.text = Html.fromHtml(recipe.instructions)
        }

        _binding!!.tvCookingTime.text =resources.getString(
            R.string.lbl_estimate_cooking_time,
            recipe.readyInMinutes.toString()
        )

        _binding!!.ivFavouriteDish.setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_favorite_unselected
            )
        )

        var addedToFavourite = false

        _binding!!.ivFavouriteDish.setOnClickListener {
            if (addedToFavourite){
                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_already_added_to_favorites),
                    Toast.LENGTH_SHORT
                ).show()
            }else{

                val randomDishDetails = FavDish(
                    recipe.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE,
                    recipe.title,
                    dishType,
                    "Other",
                    ingredients,
                    recipe.readyInMinutes.toString(),
                    recipe.instructions,
                    true
                )

                viewModel.insert(randomDishDetails)

                addedToFavourite = true

                _binding!!.ivFavouriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )

                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_added_to_favorites),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun showCustomProgressDialog(){
        mProgressDialog = Dialog(requireActivity())
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }

    private fun hideProgressDialog(){
        mProgressDialog?.let {
            it.dismiss()
        }
    }
}