package com.example.sophieslieblingsrezepte.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sophieslieblingsrezepte.R
import com.example.sophieslieblingsrezepte.ui.serverConnection.ServerConnector

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val deleteButton = root.findViewById<Button>(R.id.buttonDelete)
        deleteButton.setOnClickListener {
            deleteAllRecipes()
        }
        return root
    }

    private fun deleteAllRecipes() {
        val token = requireActivity().intent.getStringExtra("Token")

        val serverConnector = ServerConnector(token!!)

        val recipeOverview = serverConnector.searchRecipes()

        recipeOverview.recipeIds.forEach{x -> serverConnector.deleteRecipe(x)}
    }
}