package com.alex_kind.weatherappwithgps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alex_kind.weatherappwithgps.model.ModelList
import com.squareup.picasso.Picasso
import java.text.DateFormatSymbols
import java.util.*

class RecyclerAdapter(private val listAllDateWeather: ModelList) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView? = null
        var description: TextView? = null
        var temp: TextView? = null
        var icon: ImageView? = null
        var calendar: Calendar = Calendar.getInstance()
        var year = 0
        var month = 0
        var day = 0
        var dayOfWeek: Int = 0
        var weekday: String = ""
        var iconID = ""
        val picasso: Picasso = Picasso.get()


        init {
            date = itemView.findViewById(R.id.tv_recycler_date)
            description = itemView.findViewById(R.id.tv_recycler_description)
            temp = itemView.findViewById(R.id.tv_recycler_temp)
            icon = itemView.findViewById(R.id.icon_weather)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.date?.text = listAllDateWeather.list[position].dt_txt
        holder.description?.text = listAllDateWeather.list[position].weather[0].description
        holder.temp?.text = listAllDateWeather.list[position].main.temp.toInt().toString() + "°C"

        //Icon
        holder.iconID = listAllDateWeather.list[position].weather[0].icon
        holder.picasso.load("https://openweathermap.org/img/wn/${holder.iconID}@2x.png")
            .into(holder.icon)

        //DayOfWeek
        holder.year = listAllDateWeather.list[position].dt_txt.substring(0, 4).toInt()
        holder.month = listAllDateWeather.list[position].dt_txt.substring(5, 7).toInt() - 1
        holder.day = listAllDateWeather.list[position].dt_txt.substring(8, 10).toInt()
        holder.calendar.set(holder.year, holder.month, holder.day)
        holder.dayOfWeek = holder.calendar.get(Calendar.DAY_OF_WEEK)
        holder.weekday = DateFormatSymbols().weekdays[holder.dayOfWeek]
        holder.date?.text =
            holder.weekday + " " + listAllDateWeather.list[position].dt_txt.substring(10)


    }

    override fun getItemCount() = listAllDateWeather.list.size


}