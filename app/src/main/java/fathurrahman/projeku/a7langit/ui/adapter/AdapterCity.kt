package fathurrahman.projeku.a7langit.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fathurrahman.projeku.a7langit.R
import fathurrahman.projeku.a7langit.databinding.ItemCityBinding
import fathurrahman.projeku.a7langit.db.table.FavoriteTable
import fathurrahman.projeku.a7langit.ext.inflate

class AdapterCity (
    private val onClick: (FavoriteTable) -> Unit,
) : RecyclerView.Adapter<AdapterCity.ViewHolder>() {

    var items: MutableList<FavoriteTable> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_city))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as? ViewHolder)?.bind(items.getOrNull(position)!!)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView){
        private var binding = ItemCityBinding.bind(containerView)

        fun bind(data: FavoriteTable){
            with(binding){

                binding.city.text = data.name

                line.setOnClickListener {
                    onClick(data)
                }
            }
        }
    }

    fun insertData(data : List<FavoriteTable>){
        data.forEach {
            items.add(it)
            notifyDataSetChanged()
        }
    }

    fun clearData() {
        if (items.isNotEmpty()) {
            items.clear()
            notifyDataSetChanged()
        }
    }

    fun setFilter(dataFilter: List<FavoriteTable>) {
        dataFilter.forEach {
            items.add(it)
            notifyDataSetChanged()
        }
    }
}