package abhishek.mathur.favdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import abhishek.mathur.favdish.databinding.FragmentFavoriteDishesBinding
import abhishek.mathur.favdish.model.entities.FavDish
import abhishek.mathur.favdish.view.activities.MainActivity
import abhishek.mathur.favdish.view.adapters.FavDishAdapter
import abhishek.mathur.favdish.viewmodel.DashboardViewModel
import abhishek.mathur.favdish.viewmodel.FavDishViewModel
import android.app.Application
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

class FavoriteDishesFragment : Fragment() {

    private var _binding: FragmentFavoriteDishesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel : FavDishViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(Application())).get(
            FavDishViewModel::class.java)

        viewModel.favouriteDishes.observe(viewLifecycleOwner){ dishes ->
            dishes.let {

                _binding!!.rvFavouriteDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
                val adapter = FavDishAdapter(this@FavoriteDishesFragment)
                _binding!!.rvFavouriteDishesList.adapter = adapter

                if (it.isNotEmpty()){
                    _binding!!.rvFavouriteDishesList.visibility = View.VISIBLE
                    _binding!!.tvNoFaouriteDishesAvailable.visibility = View.GONE

                    adapter.dishesList(it)
                }else{
                    _binding!!.rvFavouriteDishesList.visibility = View.GONE
                    _binding!!.tvNoFaouriteDishesAvailable.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity){
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun dishDetails(favDish : FavDish){

        if (requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }

        findNavController().navigate(FavoriteDishesFragmentDirections.actionFavoriteDishesToDishDetails(favDish))
    }
}