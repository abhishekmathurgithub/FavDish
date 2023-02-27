package abhishek.mathur.favdish.view.fragments

import abhishek.mathur.favdish.R
import abhishek.mathur.favdish.databinding.DialogCustomListBinding
import abhishek.mathur.favdish.databinding.FragmentAllDishesBinding
import abhishek.mathur.favdish.model.entities.FavDish
import abhishek.mathur.favdish.utils.Constants
import abhishek.mathur.favdish.view.activities.AddUpdateDishActivity
import abhishek.mathur.favdish.view.activities.MainActivity
import abhishek.mathur.favdish.view.adapters.CustomListItemAdapter
import abhishek.mathur.favdish.view.adapters.FavDishAdapter
import abhishek.mathur.favdish.viewmodel.FavDishViewModel
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import abhishek.mathur.favdish.viewmodel.HomeViewModel
import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.GridLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

class AllDishesFragment : Fragment() {

    private lateinit var _binding: FragmentAllDishesBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel : FavDishViewModel
    private lateinit var mFavDishAdapter: FavDishAdapter
    private lateinit var mCustomListDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentAllDishesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application = Application())).get(FavDishViewModel::class.java)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.rvDishesList?.layoutManager = GridLayoutManager(requireActivity(), 2)
        mFavDishAdapter = FavDishAdapter(this@AllDishesFragment)
        _binding?.rvDishesList?.adapter = mFavDishAdapter

        viewModel.allDishesList.observe(viewLifecycleOwner){ dishes ->
            dishes.let {

                if (it.isNotEmpty()){

                    //Log.i("all_dish", "" + it[0].cookingTime)
                    _binding.rvDishesList.visibility = View.VISIBLE
                    _binding.tvNoDishesAddedYet.visibility = View.GONE
                    mFavDishAdapter.dishesList(it)

                }else{

                   // Log.i("all_dish", "no data")
                    _binding.rvDishesList.visibility = View.GONE
                    _binding.tvNoDishesAddedYet.visibility = View.VISIBLE
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
        //_binding = null
    }

    fun dishDetails(favDish : FavDish){

        if (requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }

        findNavController()
            .navigate(AllDishesFragmentDirections.actionAllDishesToDishDetails(favDish))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.action_filter_dishes -> {
                filterDishesListDialog()
                return true
            }

            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(), AddUpdateDishActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun deleteDish(dish: FavDish){

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_dish))
        builder.setMessage(resources.getString(R.string.msg_delete_dish_dialog, dish.title))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            viewModel.delete(dish)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.lbl_no)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun filterDishesListDialog(){

        mCustomListDialog = Dialog(requireActivity())
        val binding : DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = resources.getString(R.string.title_select_item_to_filter)

        val dishTypes = Constants.dishType()
        dishTypes.add(0, Constants.ALL_ITEMS)

        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = CustomListItemAdapter(
            requireActivity(),
            this@AllDishesFragment,
            dishTypes,
            Constants.FILTER_SELECTION
        )
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }

    fun filterSelection(filterItemSelection : String){

        mCustomListDialog.dismiss()

        Log.i("Filter Selection", filterItemSelection)

        if (filterItemSelection == Constants.ALL_ITEMS){
            viewModel.allDishesList.observe(viewLifecycleOwner){ dishes ->
                dishes.let {
                    if (it.isNotEmpty()){

                        _binding.rvDishesList.visibility = View.VISIBLE
                        _binding.tvNoDishesAddedYet.visibility = View.GONE

                        mFavDishAdapter.dishesList(it)
                    }else{

                        _binding.rvDishesList.visibility = View.GONE
                        _binding.rvDishesList.visibility = View.VISIBLE
                    }
                }
            }
        }else{


            viewModel.getFilteredList(filterItemSelection).observe(viewLifecycleOwner){ dishes ->
                dishes.let {
                    if (it.isNotEmpty()){
                        _binding.rvDishesList.visibility = View.VISIBLE
                        _binding.tvNoDishesAddedYet.visibility = View.GONE

                        mFavDishAdapter.dishesList(it)
                    }else{

                        _binding.rvDishesList.visibility = View.GONE
                        _binding.tvNoDishesAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

}