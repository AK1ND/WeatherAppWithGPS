package com.alex_kind.weatherappwithgps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alex_kind.weatherappwithgps.model.ModelList
import java.util.*

class RecyclerAdapter(private val listAllDateWeather: ModelList):
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

        class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            var date: TextView? = null
            var description: TextView? = null
            var temp: TextView? = null

            init {
                date = itemView.findViewById(R.id.tv_recycler_date)
                description = itemView.findViewById(R.id.tv_recycler_description)
                temp = itemView.findViewById(R.id.tv_recycler_temp)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.date?.text = listAllDateWeather.list[position].dt_txt
        holder.description?.text = listAllDateWeather.list[position].weather[0].description
        holder.temp?.text = listAllDateWeather.list[position].main.temp.toInt().toString() + "°C"
    }

    override fun getItemCount() = listAllDateWeather.list.size


}