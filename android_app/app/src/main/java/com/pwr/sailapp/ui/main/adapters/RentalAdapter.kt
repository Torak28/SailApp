package com.pwr.sailapp.ui.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pwr.sailapp.R
import com.pwr.sailapp.data.Centre
import com.pwr.sailapp.data.Rental

// https://www.andreasjakl.com/kotlin-recyclerview-for-high-performance-lists-in-android/

// Adapter adapts the individual list item to the main container layout (eg. list layout)
class RentalAdapter(
    private val context: Context,
    val clickListener: (Rental) -> Unit // function
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
        Glide.with(context).asBitmap()
            .load(currentRental.centre.photoURL)
            .centerCrop()
            .into(holder.imageView)
        holder.textViewName.text = currentRental.centre.name
        holder.textViewRentalDate.text = currentRental.rentDate
        holder.textViewRentalLocation.text = currentRental.centre.location
        holder.textViewRentalStart.text = currentRental.rentStartTime
        holder.cardView.setOnClickListener {clickListener(currentRental)}
    }

    // Setter for list of centres to use LiveData TODO consider just adding or removing single rentals
    fun setRentals(rentals: ArrayList<Rental> ){
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
        val textViewRentalLocation: TextView = itemView.findViewById(R.id.textView_rental_location)
    //    val textViewRentalLength: TextView = itemView.findViewById(R.id.textView_rental_length)
        val cardView: CardView = itemView.findViewById(R.id.rental_card)

    }
}