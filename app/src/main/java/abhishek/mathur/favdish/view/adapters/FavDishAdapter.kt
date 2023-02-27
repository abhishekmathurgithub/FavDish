package abhishek.mathur.favdish.view.adapters

import abhishek.mathur.favdish.R
import abhishek.mathur.favdish.databinding.ItemDishLayoutBinding
import abhishek.mathur.favdish.model.entities.FavDish
import abhishek.mathur.favdish.utils.Constants
import abhishek.mathur.favdish.view.activities.AddUpdateDishActivity
import abhishek.mathur.favdish.view.fragments.AllDishesFragment
import abhishek.mathur.favdish.view.fragments.FavoriteDishesFragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavDishAdapter(private val fragment: Fragment)
    : RecyclerView.Adapter<FavDishAdapter.ViewHolder>(){

    private var dishes : List<FavDish> = listOf()

        class ViewHolder(view : ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root){
            val ivDishImage = view.ivDishImage
            val tvTitle = view.tvDishTitle
            val ibMore = view.ibMore
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemDishLayoutBinding = ItemDishLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]

        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)

        holder.tvTitle.text = dish.title

        holder.itemView.setOnClickListener {
            if (fragment is AllDishesFragment){
                fragment.dishDetails(dish)
            }else if (fragment is FavoriteDishesFragment){
                fragment.dishDetails(dish)
            }
        }

        if (fragment is AllDishesFragment){
            holder.ibMore.visibility = View.VISIBLE
        }else if (fragment is FavoriteDishesFragment){
            holder.ibMore.visibility = View.GONE
        }

        holder.ibMore.setOnClickListener {

            val popUp = PopupMenu(fragment.context, holder.ibMore)
            popUp.menuInflater.inflate(R.menu.menu_adapter, popUp.menu)

            popUp.setOnMenuItemClickListener {

                if (it.itemId == R.id.action_edit_dish){
                    val intent = Intent(fragment.requireActivity(), AddUpdateDishActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_DETAILS, dish)
                    fragment.requireActivity().startActivity(intent)
                    //Toast.makeText(fragment.requireActivity(), "Edit dish", Toast.LENGTH_SHORT).show()
                } else if (it.itemId == R.id.action_delete_dish){
                    //Toast.makeText(fragment.requireActivity(), "wait", Toast.LENGTH_SHORT).show()
                    if (fragment is AllDishesFragment){
                        fragment.deleteDish(dish)
                    }
                }

                true
            }

            popUp.show()
        }
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun dishesList(list: List<FavDish>){
        dishes = list
        notifyDataSetChanged()
    }
}