package com.example.carserviceandroidapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carserviceandroidapp.ImageAndText
import com.example.carserviceandroidapp.R
import com.example.carserviceandroidapp.ServiceHistoryViewHolder

class CustomAdapter(
    context: Context?, var aList: ArrayList<ImageAndText>,
    var itemClickListener: ItemClickListener?
) : RecyclerView.Adapter<ServiceHistoryViewHolder?>() {
    var inflater: LayoutInflater

    init {
        // mData = data;
        inflater = LayoutInflater.from(context)
        //this.names = names;
    }

    fun getItem(id: Int): Int {
        return aList[id].imageId
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceHistoryViewHolder {
        val view = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return ServiceHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceHistoryViewHolder, position: Int) {
        (holder as ViewHolder).imageView.setImageResource(
            aList[position].imageId
        )
        holder.textView.text = aList[position].txt
    }

    override fun getItemCount(): Int {
        return aList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var textView: TextView

        init {
            imageView = itemView.findViewById(R.id.imgDelete)
            textView = itemView.findViewById(R.id.txt)
            itemView.setOnClickListener { view ->
                if (itemClickListener != null) itemClickListener!!.onItemClick(
                    view,
                    adapterPosition
                )
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}
