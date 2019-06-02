package com.pwr.sailapp.ui.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pwr.sailapp.R
import com.pwr.sailapp.data.sail.Rental
import com.pwr.sailapp.data.weather.Currently.Companion.CLEAR_DAY
import com.pwr.sailapp.data.weather.Currently.Companion.CLEAR_NIGHT
import com.pwr.sailapp.data.weather.Currently.Companion.CLOUDY
import com.pwr.sailapp.data.weather.Currently.Companion.FOG
import com.pwr.sailapp.data.weather.Currently.Companion.PARTLY_CLOUDY_DAY
import com.pwr.sailapp.data.weather.Currently.Companion.PARTLY_CLOUDY_NIGHT
import com.pwr.sailapp.data.weather.Currently.Companion.RAIN
import com.pwr.sailapp.data.weather.Currently.Companion.SLEET
import com.pwr.sailapp.data.weather.Currently.Companion.SNOW
import com.pwr.sailapp.data.weather.Currently.Companion.WIND
import com.pwr.sailapp.utils.DateUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.handleCoroutineException
import org.w3c.dom.Text

// https://www.andreasjakl.com/kotlin-recyclerview-for-high-performance-lists-in-android/

// Adapter adapts the individual list item to the main container layout (eg. list layout)
class RentalAdapter(
    private val context: Context,
    val phoneListener: (Rental) -> Unit = {}, // default value is {}
    val locationListener: (Rental) -> Unit = {},
    val cancelListener: (Rental) -> Unit
) : RecyclerView.Adapter<RentalAdapter.ViewHolder>() {

    private var rentals = ArrayList<Rental>()

    // Creating a new view (view holder) - only a few times
    // The onCreateViewHolder() method is similar to the onCreate() method. It inflates the item layout, and returns a ViewHolder with the layout and the adapter.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rental_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = rentals.size

    // Fill the view with data from one list element - multiple times (recycler)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentRental = rentals[position]

        holder.textViewName.text = currentRental.centreName

        holder.textViewRentalStartDate.text = currentRental.rentStartDateFormatted
        holder.textViewRentalStartTime.text = currentRental.rentStartTimeFormatted

        holder.textViewRentalEndDate.text = currentRental.rentEndDateFormatted
        holder.textViewRentalEndTime.text = currentRental.rentEndTimeFormatted

        val gearAndQuantity = "${currentRental.equipmentName}   × ${currentRental.rentQuantity}"
        holder.textViewGearAndQuantity.text = gearAndQuantity

        // Expand the view and hide down arrow when clicked
        holder.arrowDownImageView.setOnClickListener {
            holder.extrasLinearLayout.visibility = View.VISIBLE
            it.visibility = View.GONE
        }

        // Collapse the view and show up arrow when clicked
        holder.arrowUpImageView.setOnClickListener {
            // holder.imageView.visibility = View.GONE
            holder.arrowDownImageView.visibility = View.VISIBLE
            holder.extrasLinearLayout.visibility = View.GONE
        }

        if(currentRental.currently != null){
            holder.linearLayoutWeatherSection.visibility = View.VISIBLE
            holder.textViewTemperature.text = currentRental.currently!!.temperatureFormatted
            holder.textViewWind.text = currentRental.currently!!.windSpeedFormatted
            val iconID = when(currentRental.currently!!.icon){
                CLEAR_DAY -> R.drawable.clear_day
                CLEAR_NIGHT -> R.drawable.clear_night
                RAIN -> R.drawable.rain
                SNOW -> R.drawable.snow
                SLEET -> R.drawable.sleet
                WIND -> R.drawable.wind
                FOG -> R.drawable.fog
                CLOUDY -> R.drawable.cloudy
                PARTLY_CLOUDY_DAY -> R.drawable.partly_cloudy_day
                PARTLY_CLOUDY_NIGHT -> R.drawable.partly_cloudy_night
                else -> R.drawable.unknown
            }
            holder.imageViewWeather.setImageResource(iconID)
        }
        else holder.linearLayoutWeatherSection.visibility = View.GONE

        // Handlers for image buttons
        holder.phoneImageButton.setOnClickListener { phoneListener(currentRental) }
        holder.locationImageButton.setOnClickListener { locationListener(currentRental) }
        holder.cancelImageButton.setOnClickListener { cancelListener(currentRental) }

    }

    fun setRentals(rentals: ArrayList<Rental> ){
        this.rentals = rentals
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val textViewName: TextView = itemView.findViewById(R.id.textView_rental_name)

        val textViewRentalStartDate: TextView = itemView.findViewById(R.id.textView_rental_start_date)
        val textViewRentalStartTime: TextView = itemView.findViewById(R.id.textView_rental_start_time)

        val textViewGearAndQuantity : TextView = itemView.findViewById(R.id.textView_gear_and_quantity)

        val textViewRentalEndDate: TextView = itemView.findViewById(R.id.textView_rental_end_date)
        val textViewRentalEndTime: TextView = itemView.findViewById(R.id.textView_rental_end_time)

        val arrowDownImageView: ImageView = itemView.findViewById(R.id.imageView_arrow_down)
        val arrowUpImageView: ImageView = itemView.findViewById(R.id.imageView_arrow_up)

        val phoneImageButton: ImageButton = itemView.findViewById(R.id.imageButton_phone)
        val locationImageButton: ImageButton = itemView.findViewById(R.id.imageButton_location)
        val cancelImageButton: ImageButton = itemView.findViewById(R.id.imageButton_cancel)

        val extrasLinearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout_extras)
        val linearLayoutWeatherSection : LinearLayout = itemView.findViewById(R.id.linearLayout_weather_section)
        val textViewTemperature: TextView = itemView.findViewById(R.id.textView_temperature)
        val textViewWind: TextView = itemView.findViewById(R.id.textView_wind)
        val imageViewWeather : ImageView = itemView.findViewById(R.id.imageView_weather_icon)
    }
}