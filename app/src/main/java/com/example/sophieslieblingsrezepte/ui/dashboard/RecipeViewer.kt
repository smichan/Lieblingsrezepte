package com.example.sophieslieblingsrezepte.ui.dashboard

import android.os.Bundle
import android.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.sophieslieblingsrezepte.R
import com.example.sophieslieblingsrezepte.data.model.Recipe
import com.example.sophieslieblingsrezepte.databinding.ActivityRecipeViewerBinding
import org.json.JSONObject

class RecipeViewer : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRecipeViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecipeViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val jsonString = intent.getStringExtra("Json")
        val jsonObject = JSONObject(jsonString)
        val recipeTitle = jsonObject.getString("name")
        /*var recipe = Recipe(recipeTitle)
        recipe.fromJson(jsonObject)*/

        val navController = findNavController(R.id.nav_host_fragment_content_recipe_viewer)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //to set the recipe title, the toolbar title needs to be set after the fragment is loaded.
        binding.toolbar.title = recipeTitle

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_recipe_viewer)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}