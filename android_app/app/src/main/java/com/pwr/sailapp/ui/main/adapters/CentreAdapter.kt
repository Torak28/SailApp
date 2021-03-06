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
import com.pwr.sailapp.data.sail.Centre
import com.pwr.sailapp.utils.formatDistance
import kotlin.collections.ArrayList

// https://www.andreasjakl.com/kotlin-recyclerview-for-high-performance-lists-in-android/

// Adapter adapts the individual list item to the main container layout (eg. list layout)
class CentreAdapter(
    private val context: Context,
    val clickListener: (Centre) -> Unit // function
) : RecyclerView.Adapter<CentreAdapter.ViewHolder>(){

    private lateinit var centres: ArrayList<Centre>

    // Creating a new view (view holder) - only a few times
    // The onCreateViewHolder() method is similar to the onCreate() method. It inflates the item layout, and returns a ViewHolder with the layout and the adapter.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.centre_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = centres.size

    // Fill the view with data from one list element - multiple times (recycler)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentCentre = centres[position]

        if(currentCentre.photoURL != null){
            Glide.with(context).asBitmap()
                .load(currentCentre.photoURL)
                .centerCrop()
                .into(holder.imageView)
            holder.imageView.visibility = View.VISIBLE
        }
        else holder.imageView.visibility = View.GONE

        holder.textViewName.text = currentCentre.name

        if(currentCentre.distance < Double.POSITIVE_INFINITY){
            holder.textViewDistance.text = formatDistance(currentCentre.distance)
        }
        else holder.textViewDistance.visibility = View.INVISIBLE

        holder.cardView.setOnClickListener {clickListener(currentCentre)}
    }

    fun setCentres(centres: ArrayList<Centre> ){
        this.centres = centres
        // Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh itself.
        notifyDataSetChanged()
    }

    // https://codelabs.developers.google.com/codelabs/android-training-create-recycler-view/index.html?index=..%2F..android-training#4
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView:ImageView = itemView.findViewById(R.id.imageView_centre_photo)
        val textViewName:TextView = itemView.findViewById(R.id.textView_centre_name)
        val textViewDistance: TextView = itemView.findViewById(R.id.textView_distance)
        val cardView:CardView = itemView.findViewById(R.id.centre_card)

    }

}