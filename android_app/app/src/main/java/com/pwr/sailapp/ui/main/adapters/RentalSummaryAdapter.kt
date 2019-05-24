package com.pwr.sailapp.ui.main.adapters

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pwr.sailapp.R
import com.pwr.sailapp.data.*
import com.pwr.sailapp.data.sail.Rental
import com.pwr.sailapp.utils.DateUtil

// https://www.andreasjakl.com/kotlin-recyclerview-for-high-performance-lists-in-android/

// Adapter adapts the individual list item to the main container layout (eg. list layout)
class RentalSummaryAdapter(
    private val context: Context,
    val phoneListener: (Rental) -> Unit = {}, // default value is {}
    val locationListener: (Rental) -> Unit = {},
    val cancelListener: (Rental) -> Unit = {}
) : RecyclerView.Adapter<RentalSummaryAdapter.ViewHolder>() {

    private var rentals = ArrayList<RentalSummary>()

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
        Glide.with(context).asBitmap()
            .load(currentRental.photoURL)
            .centerCrop()
            .into(holder.imageView)
        holder.textViewName.text = currentRental.centreName
        holder.textViewRentalDate.text = currentRental.startDate
     //   holder.textViewRentalLocation.text = currentRental.centre.location
        holder.textViewRentalStart.text = currentRental.startTime
        holder.textViewRentalDuration.text = currentRental.endTime

        // Expand the view and hide down arrow when clicked
        holder.arrowDownImageView.setOnClickListener {
            holder.extrasLinearLayout.visibility = View.VISIBLE
            it.visibility = View.GONE
        }

        // Collapse the view and show up arrow when clicked
        holder.arrowUpImageView.setOnClickListener {
            holder.arrowDownImageView.visibility = View.VISIBLE
            holder.extrasLinearLayout.visibility = View.GONE
        }

        // Handlers for image buttons
        holder.phoneImageButton.setOnClickListener { phoneListener(currentRental.rental) }
        holder.locationImageButton.setOnClickListener { locationListener(currentRental.rental) }
        holder.cancelImageButton.setOnClickListener { cancelListener(currentRental.rental) }

        // Weather section
        if(currentRental.temperature != null
            && currentRental.wind != null
            && currentRental.iconName != null
        ){
            holder.linearLayoutWeatherSection.visibility = View.VISIBLE
            holder.textViewTemperature.text = currentRental.temperature
            holder.textViewWind.text = currentRental.wind
            val iconID = when(currentRental.iconName){
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
        else {
            Log.e("onBindViewHolder", "currentRental.currently = null")
            holder.linearLayoutWeatherSection.visibility = View.GONE
        }
    }

    fun setRentals(rentals: ArrayList<RentalSummary> ){
        this.rentals = rentals

        // Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh itself.
        notifyDataSetChanged() // TODO replace this method with more efficient one
    }

    // https://codelabs.developers.google.com/codelabs/android-training-create-recycler-view/index.html?index=..%2F..android-training#4
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val imageView: ImageView = itemView.findViewById(R.id.imageView_rental_photo)
        val textViewName: TextView = itemView.findViewById(R.id.textView_rental_name)
        val textViewRentalDate: TextView = itemView.findViewById(R.id.textView_rental_date)
        val textViewRentalStart: TextView = itemView.findViewById(R.id.textView_rental_start)
     //   val textViewRentalLocation: TextView = itemView.findViewById(R.id.textView_rental_location)
        val textViewRentalDuration: TextView = itemView.findViewById(R.id.textView_rental_duration)
        val arrowDownImageView: ImageView = itemView.findViewById(R.id.imageView_arrow_down)
        val arrowUpImageView: ImageView = itemView.findViewById(R.id.imageView_arrow_up)
        val phoneImageButton: ImageButton = itemView.findViewById(R.id.imageButton_phone)
        val locationImageButton: ImageButton = itemView.findViewById(R.id.imageButton_location)
        val cancelImageButton: ImageButton = itemView.findViewById(R.id.imageButton_cancel)
        val extrasLinearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout_extras)
        val textViewTemperature: TextView = itemView.findViewById(R.id.textView_temperature)
        val textViewWind: TextView = itemView.findViewById(R.id.textView_wind)
        val imageViewWeather : ImageView = itemView.findViewById(R.id.imageView_weather_icon)
        val linearLayoutWeatherSection : LinearLayout = itemView.findViewById(R.id.linearLayout_weather_section)
    }

}