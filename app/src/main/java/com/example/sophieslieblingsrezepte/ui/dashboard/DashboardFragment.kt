package com.example.sophieslieblingsrezepte.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sophieslieblingsrezepte.R
import com.example.sophieslieblingsrezepte.data.CreateList
import com.example.sophieslieblingsrezepte.data.GalleryAdapter
import com.example.sophieslieblingsrezepte.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    private final var image_titles: Array<String> =
            arrayOf("January", "February", "March", "April", "May")
    private final var image_ids: IntArray =
            intArrayOf(R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5)

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        //val root = inflater.inflate(binding.root, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/

        val root = binding.root
        // set recycler view
        binding.galleryRecipe.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        binding.galleryRecipe.layoutManager = layoutManager
        val createLists: ArrayList<CreateList> = prepareData()
        val adapter: GalleryAdapter = GalleryAdapter(context, createLists)
        binding.galleryRecipe.adapter = adapter
        return root
    }

    private fun prepareData(): ArrayList<CreateList> {
        val images: ArrayList<CreateList> = ArrayList()
        for (i in image_titles.indices) {
            val createList = CreateList(image_titles[i], image_ids[i])
            images.add(createList)
        }
        return images
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

