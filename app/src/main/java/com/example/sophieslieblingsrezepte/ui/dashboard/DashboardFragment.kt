package com.example.sophieslieblingsrezepte.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
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
    private lateinit var serverConnector: ServerConnector
    private lateinit var launcher: ActivityResultLauncher<String>


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // lateinit initializing
        val token = requireActivity().intent.getStringExtra("Token")
        serverConnector = ServerConnector(token!!)
        var contract = RecipeViewerContract()
        launcher = this.registerForActivityResult(contract) {

            if (it != null) deleteRecipe(it)

        }

        val root = binding.root
        // set recycler view
        binding.galleryRecipe.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
        binding.galleryRecipe.layoutManager = layoutManager
        updateGallery()
        return root
    }

    private fun deleteRecipe(id: Int) {

        serverConnector.deleteRecipe(id)
        //TODO: Fix this not like an idiot
        println("Wait for it")
        updateGallery()
    }

    private fun updateGallery()
    {
        val galleryImages = prepareData()
        val adapter = GalleryAdapter(context, galleryImages)
        binding.galleryRecipe.adapter = adapter

        adapter.clicked.observe(viewLifecycleOwner, Observer {
            val galleryPosition = it ?: return@Observer
            val recipeId = galleryImages[galleryPosition].recipe_id
            loadRecipe(recipeId)
        })
    }

    private fun prepareData(): ArrayList<GalleryImage> {

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
        val json = serverConnector.getRecipe(recipeId)
        launcher.launch(json.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class RecipeViewerContract : ActivityResultContract<String, Int>()
{
    override fun createIntent(context: Context, json: String?): Intent {
        val intent = Intent(context, RecipeViewer::class.java)
        intent.putExtra("Json", json)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Int? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        if (intent == null)
        {
            return null
        }
        // default Value cannot be returned, because Id is always set in the else case
        return intent!!.getIntExtra("id", -1)
    }
}


