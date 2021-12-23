package com.example.sophieslieblingsrezepte.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sophieslieblingsrezepte.data.GalleryImage
import com.example.sophieslieblingsrezepte.data.GalleryAdapter
import com.example.sophieslieblingsrezepte.databinding.FragmentDashboardBinding
import com.example.sophieslieblingsrezepte.ui.serverConnection.ServerConnector


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel


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

        val root = binding.root
        // set recycler view
        binding.galleryRecipe.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        binding.galleryRecipe.layoutManager = layoutManager
        val galleryImages: ArrayList<GalleryImage> = prepareData()

        val adapter = GalleryAdapter(context, galleryImages)
        binding.galleryRecipe.adapter = adapter

        adapter.clicked.observe(viewLifecycleOwner, Observer {
            val galleryPosition = it ?: return@Observer
            val recipeId = galleryImages[galleryPosition].recipe_id
            loadRecipe(recipeId)
        })

        return root
    }

    private fun prepareData(): ArrayList<GalleryImage> {

        val token = requireActivity().intent.getStringExtra("Token")
        val serverConnector = ServerConnector(token!!)
        val recipeOverview = serverConnector.searchRecipes()

        val galleryList: ArrayList<GalleryImage> = ArrayList()
        for (i in 0 until recipeOverview.recipeIds.size)
        {
            val picture = serverConnector.getPicture(recipeOverview.recipes[i].pictureId)

            galleryList.add(GalleryImage(recipeOverview.recipeIds[i], recipeOverview.recipes[i].name, picture))
        }
        return galleryList
    }

    private fun loadRecipe(recipeId: Int)
    {
        val token = requireActivity().intent.getStringExtra("Token")
        val serverConnector = ServerConnector(token!!)
        val json = serverConnector.getRecipe(recipeId)
        val intent = Intent(context, RecipeViewer::class.java)
        intent.putExtra("Json", json.toString());
        this.startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

