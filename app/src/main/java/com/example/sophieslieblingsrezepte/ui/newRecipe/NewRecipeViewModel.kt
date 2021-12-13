package com.example.sophieslieblingsrezepte.ui.newRecipe

import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sophieslieblingsrezepte.R
import com.example.sophieslieblingsrezepte.ui.serverConnection.ServerConnector
import org.json.JSONObject

class Ingredient (amount: String, unit: String, ingredient: String, serverId: Int? = null) {
    public val amount: String = amount
    public val unit: String = unit
    public val ingredientName: String = ingredient
    public var serverId: Int? = serverId
}

class Step (description: String, order: Int, serverId: Int? = null)
{

    public val description: String = description
    public var order: Int = order
    public var serverId: Int? = serverId
}

class NewRecipeViewModel : ViewModel() {
    private val _ingredients = MutableLiveData<List<Ingredient>>(listOf())
    private val _steps = MutableLiveData<List<Step>>(listOf())
    private val _name = MutableLiveData<String>()

    private var _nextStepNo = 0

    val ingredients: LiveData<List<Ingredient>> = _ingredients
    val steps: LiveData<List<Step>> = _steps
    val name: LiveData<String> = _name

    fun setName(name: String)
    {
        _name.value = name
    }

    fun addIngredient(ingredient: Ingredient)
    {
        var list = _ingredients.value
        list = list?.plus(ingredient)

        _ingredients.value = list!!
    }

    fun addStep(step: Step)
    {
        var list = _steps.value
        step.order = _nextStepNo
        countStepUp()
        list = list?.plus(step)
        _steps.value = list!!
    }
    fun setServerId(id: Int?, ingredient: Ingredient)
    {
        ingredient.serverId = id
        var list = _ingredients.value?.toMutableList()
        val index = list?.indexOf(ingredient)
        if (index != null)
        {
            list?.set(index, ingredient)
            _ingredients.value = list!!
        }
    }

    fun setServerId(id: Int?, step: Step)
    {
        step.serverId = id
        var list = _steps.value?.toMutableList()

        val index = list?.indexOf(step)
        if (index != null)
        {
            list?.set(index, step)
            _steps.value = list!!
        }
    }

    fun countStepUp()
    {
        _nextStepNo += 1
    }
}