package fathurrahman.projeku.a7langit.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fathurrahman.projeku.a7langit.R
import fathurrahman.projeku.a7langit.databinding.ItemHomeBinding
import fathurrahman.projeku.a7langit.ext.inflate
import fathurrahman.projeku.a7langit.services.entity.ResponseWeatherHourItem

class AdapterMain (
    private val onClick: (ResponseWeatherHourItem) -> Unit
) : RecyclerView.Adapter<AdapterMain.ViewHolder>() {

    var items: MutableList<ResponseWeatherHourItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_home))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as? ViewHolder)?.bind(items.getOrNull(position)!!)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView){
        private var binding = ItemHomeBinding.bind(containerView)

        fun bind(data: ResponseWeatherHourItem){
            with(binding){

                val suhu = data.main.temp.toInt()

                binding.date.text = data.dt_txt
                binding.info.text = data.weather[0].description
                binding.tvSuhu.text = "$suhu \u2103"
                binding.tvKelembapan.text = data.main.humidity+" %"
                binding.tvAngin.text = data.wind.speed+" mtr/dtk"

                line.setOnClickListener {
                    onClick(data)
                }
            }
        }
    }

    fun insertData(data : List<ResponseWeatherHourItem>){
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
}