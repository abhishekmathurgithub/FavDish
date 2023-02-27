package abhishek.mathur.favdish.view.adapters

import abhishek.mathur.favdish.databinding.ItemCustomListLayoutBinding
import abhishek.mathur.favdish.view.activities.AddUpdateDishActivity
import abhishek.mathur.favdish.view.fragments.AllDishesFragment
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class CustomListItemAdapter(

    private val activity : Activity,
    private val fragment : Fragment?,
    private val listItem : List<String>,
    private val selection : String
) : RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomListItemAdapter.ViewHolder {
        val binding : ItemCustomListLayoutBinding = ItemCustomListLayoutBinding.inflate(
            LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomListItemAdapter.ViewHolder, position: Int) {
        val item = listItem[position]
        holder.tvText.text = item

        holder.itemView.setOnClickListener {
            if (activity is AddUpdateDishActivity){
                activity.selectedListItem(item, selection)
            }
            if (fragment is AllDishesFragment){
                fragment.filterSelection(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

    class ViewHolder(view : ItemCustomListLayoutBinding) : RecyclerView.ViewHolder(view.root){
        val tvText = view.tvText
    }
}