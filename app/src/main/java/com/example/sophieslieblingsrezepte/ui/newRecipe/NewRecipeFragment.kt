package com.example.sophieslieblingsrezepte.ui.newRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sophieslieblingsrezepte.R

class NewRecipeFragment : Fragment() {

    private lateinit var newRecipeViewModel: NewRecipeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        newRecipeViewModel =
                ViewModelProvider(this).get(NewRecipeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_recipe, container, false)

        return root
    }
}