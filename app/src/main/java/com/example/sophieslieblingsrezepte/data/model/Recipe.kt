package com.example.sophieslieblingsrezepte.data.model

import androidx.lifecycle.MutableLiveData
import org.json.JSONObject
import java.net.URL

class Recipe(var name: String)
{
    var recipeId: Int? = null
    var pictureId: Int? = null
    var ingredients: List<Ingredient> = listOf()
    var steps: List<Step> = listOf()

    fun addStep(step: Step)
    {
        steps = steps.plus(step)
    }

    fun addIngredient(ingredient: Ingredient)
    {
        ingredients = ingredients.plus(ingredient)
    }

    fun fromJson(json: JSONObject)
    {

        this.recipeId = json.getInt("id")
        this.pictureId = getValueFromJson(json, "mainPictureId") as Int?
        if (!json.isNull("ingredients")) {
            val ingredients = json.getJSONArray("ingredients")
            for (i in 0 until ingredients.length()) {
                val newIngredient = Ingredient(
                    getValueFromJson(json, "amount") as String,
                    getValueFromJson(json, "unit") as String,
                    getValueFromJson(json, "name") as String,
                    getValueFromJson(json, "id") as Int
                )
                this.addIngredient(newIngredient)
            }
        }
        if (!json.isNull("preparationSteps")) {
            val steps = json.getJSONArray("preparationSteps")
            for (i in 0 until steps.length())
            {
                val newStep = Step(
                    getValueFromJson(json, "description") as String,
                    getValueFromJson(json, "stepNo") as Int,
                    getValueFromJson(json, "id") as Int
                )
                this.addStep(newStep)
            }
        }
    }
/*
    fun fromJson(json: JSONObject): Recipe
    {
        val recipe = Recipe(json.getString("name"))
        recipe.recipeId = json.getInt("id")
        recipe.pictureId = getValueFromJson(json, "mainPictureId") as Int?
        if (!json.isNull("ingredients")) {
            val ingredients = json.getJSONArray("ingredients")
            for (i in 0 until ingredients.length()) {
                val newIngredient = Ingredient(
                    getValueFromJson(json, "amount") as String,
                    getValueFromJson(json, "unit") as String,
                    getValueFromJson(json, "name") as String,
                    getValueFromJson(json, "id") as Int
                )
                recipe.addIngredient(newIngredient)
            }
        }
        if (!json.isNull("preparationSteps")) {
            val steps = json.getJSONArray("preparationSteps")
            for (i in 0 until steps.length())
            {
                val newStep = Step(
                    getValueFromJson(json, "description") as String,
                    getValueFromJson(json, "stepNo") as Int,
                    getValueFromJson(json, "id") as Int
                )
                recipe.addStep(newStep)
            }
        }
        return recipe
    }
*/
    private fun getValueFromJson(json: JSONObject, name: String) : Any?
    {
        if (!json.isNull(name))
        {
            return json.get(name)
        }
        return null
    }
}


class Ingredient (amount: String, unit: String, ingredient: String, serverId: Int? = null)
{
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
