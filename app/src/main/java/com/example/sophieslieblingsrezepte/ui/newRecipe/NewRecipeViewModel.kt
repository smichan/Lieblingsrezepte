package com.example.sophieslieblingsrezepte.ui.newRecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Ingredient (amount: String, unit: String, ingredient: String) {
    public val amount: String = amount
    public val unit: String = unit
    public val ingredient: String = ingredient
}

class NewRecipeViewModel : ViewModel() {
    private val _ingredients = MutableLiveData<List<Ingredient>>(listOf())

    val ingredients: LiveData<List<Ingredient>> = _ingredients

    fun addIngredient(ingredient: Ingredient)
    {
        var list = ingredients.value
        list = list?.plus(ingredient)

        _ingredients.value = list!!
    }
}