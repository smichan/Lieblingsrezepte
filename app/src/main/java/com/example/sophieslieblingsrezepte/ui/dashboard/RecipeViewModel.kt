package com.example.sophieslieblingsrezepte.ui.dashboard

import androidx.lifecycle.ViewModel
import com.example.sophieslieblingsrezepte.data.model.Ingredient
import com.example.sophieslieblingsrezepte.data.model.Recipe
import com.example.sophieslieblingsrezepte.data.model.Step
import org.json.JSONObject

class RecipeViewModel : ViewModel() {
    val recipe: Recipe = Recipe("New Recipe")

    fun addRecipe(jsonObject: JSONObject) {
        recipe.fromJson(jsonObject)
    }
}