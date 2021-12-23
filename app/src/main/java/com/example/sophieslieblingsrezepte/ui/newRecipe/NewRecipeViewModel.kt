package com.example.sophieslieblingsrezepte.ui.newRecipe

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sophieslieblingsrezepte.R
import com.example.sophieslieblingsrezepte.data.model.Ingredient
import com.example.sophieslieblingsrezepte.data.model.Recipe
import com.example.sophieslieblingsrezepte.data.model.Step
import com.example.sophieslieblingsrezepte.ui.serverConnection.ServerConnector
import org.json.JSONObject

class NewRecipeViewModel : ViewModel() {


    private val _ingredients = MutableLiveData<List<Ingredient>>(listOf())
    private val _steps = MutableLiveData<List<Step>>(listOf())
    private val _name = MutableLiveData<String>()

    private var _nextStepNo = 0
    var recipe: Recipe = Recipe("New Recipe")

    // livedata to observe changes
    val ingredients: LiveData<List<Ingredient>> = _ingredients
    val steps: LiveData<List<Step>> = _steps
    val name: LiveData<String> = _name

    fun setName(name: String?)
    {
        if (!name.isNullOrEmpty())
        {
            recipe.name = name
            _name.value = recipe.name
        }
    }

    fun addIngredient(ingredient: Ingredient)
    {
        recipe.addIngredient(ingredient)
        _ingredients.value = recipe.ingredients
    }

    fun addStep(step: Step)
    {
        step.order = _nextStepNo
        countStepUp()
        recipe.addStep(step)
        _steps.value = recipe.steps

    }

    private fun countStepUp()
    {
        _nextStepNo += 1
    }
}