package com.example.sophieslieblingsrezepte.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toolbar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var launcher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecipeViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val jsonString = intent.getStringExtra("Json")
        val jsonObject = JSONObject(jsonString)
        val recipeTitle = jsonObject.getString("name")

        var contract = EditRecipeContract()
        launcher = this.registerForActivityResult(contract) {
            print(it)

        }
        val navController = findNavController(R.id.nav_host_fragment_content_recipe_viewer)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //to set the recipe title, the toolbar title needs to be set after the fragment is loaded.
        binding.toolbar.title = recipeTitle

        binding.fabDelete.setOnClickListener{
            val jsonString = intent.getStringExtra("Json")
            val jsonObject = JSONObject(jsonString)
            val recipeId = jsonObject.getInt("id")

            intent.putExtra("id", recipeId)
            setResult(RESULT_OK, intent)
            this.finish()
        }

        binding.fabEdit.setOnClickListener{
            //launcher.launch("test")


        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_recipe_viewer)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}

class EditRecipeContract : ActivityResultContract<String, String>()
{
    override fun createIntent(context: Context, json: String?): Intent {
        val intent = Intent(context, EditRecipeFragment::class.java)
        intent.putExtra("Json", json)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        if (intent == null)
        {
            return null
        }
        // default Value cannot be returned, because Id is always set in the else case
        return intent!!.getStringExtra("EditedJson")
    }
}