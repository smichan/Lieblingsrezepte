package com.example.sophieslieblingsrezepte.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.sophieslieblingsrezepte.R


class GalleryAdapter(context: Context?, galleryList: ArrayList<CreateList>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    private var galleryList: ArrayList<CreateList> = galleryList
    private  var context: Context? = context

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_image, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.title.setText(galleryList.get(i).image_title);
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.img.setImageResource(galleryList.get(i).image_id);

        viewHolder.img.setOnClickListener(View.OnClickListener {
            Toast.makeText(context,"Image",Toast.LENGTH_SHORT).show();



        })
    }

    override fun getItemCount(): Int {
        return galleryList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById<View>(R.id.title) as TextView
        var img: ImageView = view.findViewById<View>(R.id.img) as ImageView
    }
}

