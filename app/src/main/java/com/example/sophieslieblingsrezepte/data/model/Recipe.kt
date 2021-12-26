package com.example.sophieslieblingsrezepte.data.model

import android.graphics.Bitmap
import android.icu.text.DecimalFormat
import org.json.JSONObject

class Recipe(var name: String? = null)
{
    var recipeId: Int? = null
    var pictureId: Int? = null
    var mainPicture: Bitmap? = null
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
        this.recipeId = getValueFromJson(json, "id") as Int?
        this.pictureId = getValueFromJson(json, "mainPictureId") as Int?
        if (!json.isNull("ingredients")) {
            val ingredients = json.getJSONArray("ingredients")
            for (i in 0 until ingredients.length()) {
                val ingredientJson = ingredients.get(i) as JSONObject
                val amount = getValueFromJson(ingredientJson, "amount")
                val df = DecimalFormat("#.##")
                val amount_dz = df.format(amount)
                val unit = getValueFromJson(ingredientJson, "unit") as String
                val ingredientName = getValueFromJson(ingredientJson, "name") as String
                val ingredientId = getValueFromJson(ingredientJson, "id") as Int

                val newIngredient = Ingredient(amount_dz, unit, ingredientName, ingredientId)

                this.addIngredient(newIngredient)
            }
        }
        if (!json.isNull("preparationSteps")) {
            val steps = json.getJSONArray("preparationSteps")
            for (i in 0 until steps.length())
            {
                val stepJson = steps.get(i) as JSONObject
                val newStep = Step(
                    getValueFromJson(stepJson, "description") as String,
                    getValueFromJson(stepJson, "stepNo") as Int,
                    getValueFromJson(stepJson, "id") as Int
                )
                this.addStep(newStep)
            }
        }
    }

    private fun getValueFromJson(json: JSONObject, name: String) : Any?
    {
        if (!json.isNull(name))
        {
            return json.get(name)
        }
        return null
    }

    fun addPicture(bm: Bitmap) {
        mainPicture = bm
    }
}


class Ingredient(amount: String, unit: String, ingredient: String, serverId: Int? = null)
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

class Picture (bm: Bitmap, serverId: Int? = null)
{
    public val bm: Bitmap = bm
    public var serverId: Int? = serverId
}
