package com.example.sophieslieblingsrezepte.data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.sophieslieblingsrezepte.MainActivity
import com.example.sophieslieblingsrezepte.R
import com.example.sophieslieblingsrezepte.ui.login.LoginFormState
import com.example.sophieslieblingsrezepte.ui.serverConnection.ServerConnector


class GalleryAdapter(context: Context?, galleryList: ArrayList<GalleryImage>) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    private var galleryList: ArrayList<GalleryImage> = galleryList
    private var context: Context? = context

    private val _clicked = MutableLiveData<Int>()
    val clicked: LiveData<Int> = _clicked

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_image, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.title.setText(galleryList.get(i).picture_title)
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP)
        val bitmap = galleryList.get(i).picture_bitmap
        if (bitmap != null)
        {
            viewHolder.img.setImageBitmap(bitmap)
        }
        else
        {
            viewHolder.img.setImageResource(R.drawable.missing_image)
        }

        viewHolder.img.setOnClickListener(View.OnClickListener {
            _clicked.value = i
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

