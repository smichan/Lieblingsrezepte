package com.example.sophieslieblingsrezepte.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.sophieslieblingsrezepte.R
import com.example.sophieslieblingsrezepte.data.model.Recipe
import com.example.sophieslieblingsrezepte.databinding.ActivityRecipeViewerBinding
import com.example.sophieslieblingsrezepte.ui.editRecipe.EditRecipeFragment
import com.example.sophieslieblingsrezepte.ui.serverConnection.ServerConnector
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

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragmentContainerView, RecipeFragment())
        }
        //to set the recipe title, the toolbar title needs to be set after the fragment is loaded.
        binding.toolbar.title = recipeTitle

        binding.fabEdit.setOnClickListener{
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fragmentContainerView, EditRecipeFragment())
            }
        }

        binding.fabDelete.setOnClickListener{
            val jsonString = intent.getStringExtra("Json")
            val jsonObject = JSONObject(jsonString)
            val recipeId = jsonObject.getInt("id")

            intent.putExtra("id", recipeId)
            setResult(RESULT_OK, intent)
            this.finish()
        }
    }
/*
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_recipe_viewer)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }*/
}
