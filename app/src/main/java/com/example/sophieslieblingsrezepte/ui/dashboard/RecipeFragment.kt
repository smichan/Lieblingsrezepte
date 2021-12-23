package com.example.sophieslieblingsrezepte.ui.dashboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sophieslieblingsrezepte.R
import com.example.sophieslieblingsrezepte.databinding.FragmentShowRecipeBinding

class RecipeFragment : Fragment() {
    private lateinit var viewModel: RecipeViewModel
    private lateinit var _binding: FragmentShowRecipeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowRecipeBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
        // TODO: Use the ViewModel
    }
}