package com.example.sophieslieblingsrezepte.ui.newRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Ingredient (amount: String, unit: String, ingredient: String) {
    public val amount: String = amount
    public val unit: String = unit
    public val ingredient: String = ingredient
}

class Step (description: String, order: Int)
{
    public val description: String = description
    public val order: Int = order
}

class NewRecipeViewModel : ViewModel() {
    private val _ingredients = MutableLiveData<List<Ingredient>>(listOf())
    private val _steps = MutableLiveData<List<Step>>(listOf())

    val ingredients: LiveData<List<Ingredient>> = _ingredients
    val steps: LiveData<List<Step>> = _steps

    fun addIngredient(ingredient: Ingredient)
    {
        var list = _ingredients.value
        list = list?.plus(ingredient)

        _ingredients.value = list!!
    }

    fun addStep(step: Step)
    {
        var list = _steps.value
        list = list?.plus(step)

        _steps.value = list!!
    }
}