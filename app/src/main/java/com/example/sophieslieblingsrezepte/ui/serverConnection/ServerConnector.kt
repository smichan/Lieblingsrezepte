package com.example.sophieslieblingsrezepte.ui.serverConnection

import com.example.sophieslieblingsrezepte.data.Result
import com.example.sophieslieblingsrezepte.data.model.Recipe
import com.example.sophieslieblingsrezepte.ui.newRecipe.NewRecipeViewModel
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CompletableFuture

class ServerConnector(private val _token: String?) {
    private val _recipeUrl = URL("https://cookbook.norsecorby.de/v1/recipe")
    private val _ingredientUrl = URL("https://cookbook.norsecorby.de/v1/ingredient")
    private val _preparationstepUrl = URL("https://cookbook.norsecorby.de/v1/preparationstep")
    private val _sharingruleUrl = URL("https://cookbook.norsecorby.de/v1/SharingRule")
    private val _pictureUrl = URL("https://cookbook.norsecorby.de/v1/picture")

    fun saveNewRecipe(recipe: Recipe) : Result<Boolean> {
        try
        {
            val recipeId = saveRecipeAsJson(recipeAsJson(recipe.name))

            var iterationIngredients = recipe.ingredients
            for (ingredient in iterationIngredients)
            {
                val ingredientId = saveIngredientAsJson(ingredientAsJson(ingredient.ingredientName, recipeId, ingredient.amount, ingredient.unit))
                ingredient.serverId = ingredientId
            }
            var iterationSteps = recipe.steps
            for (step in iterationSteps)
            {
                val stepId = saveStepAsJson(stepAsJson(step.description, recipeId, step.order))
                step.serverId = stepId
            }
        }
        catch (e: Throwable)
        {
            return Result.Error(IOException("Saving new recipe failed", e))
        }
        return Result.Success(true)
    }

    // in: recipe as json
    // out: recipeId
    fun saveRecipeAsJson(json: JSONObject): Int?
    {
        val recipeBody = putJson(json, _recipeUrl)
        return recipeBody.get("id") as Int?
    }

    fun saveIngredientAsJson(json: JSONObject): Int?
    {
        val ingredientBody = putJson(json, _ingredientUrl)
        return ingredientBody.get("id") as Int?
    }

    fun saveStepAsJson(json: JSONObject): Int?
    {
        val stepBody = putJson(json, _preparationstepUrl)
        return stepBody.get("id") as Int?
    }

    fun putJson(json: JSONObject, url: URL): JSONObject
    {
        var body: JSONObject
        try {
            val future: CompletableFuture<JSONObject>? = CompletableFuture.supplyAsync{
                var responseBody = JSONObject("{}")
                val connection = url.openConnection() as HttpURLConnection
                try {
                    val bearer = "Bearer $_token"
                    connection.requestMethod = "PUT"
                    connection.setRequestProperty("Authorization", bearer)
                    connection.setRequestProperty("Content-Type", "application/json; utf-8");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.doOutput = true
                    connection.outputStream.bufferedWriter().use {it.write(json.toString())}
                    val responseCode = connection.responseCode
                    println(responseCode)
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    responseBody = JSONObject(response)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                finally {
                    connection.disconnect()
                }
                return@supplyAsync responseBody
            }
            if (future != null) {
                body = future.get()
            }
            else
            {
                throw IOException("No value returned")
            }
        } catch (e: Throwable) {
            throw IOException("Server access failed", e)
        }
        return body
    }



    fun recipeAsJson(name: String?, preparationTime: Int? = null, cookingTime: Int? = null, note: String? = null, mainPictureId: Int? = null): JSONObject
    {
        var json = JSONObject()
        json.put("name", name)
        json.put("preparationTime", preparationTime)
        json.put("cookingTime", cookingTime)
        json.put("note", note)
        json.put("mainPictureId",mainPictureId)
        return json
    }

    fun ingredientAsJson(name: String?, recipeId: Int?, amount: String?, unit: String?, optional: Boolean? = false, pictureId: Int? = null): JSONObject
    {
        val intAmount = amount?.toInt()
        var json = JSONObject()
        json.put("name", name)
        json.put("amount", intAmount)
        json.put("unit", unit)
        json.put("optional", optional)
        json.put("pictureId", pictureId)
        json.put("recipeId", recipeId)
        return json
    }

    fun stepAsJson(description: String?, recipeId: Int?, stepNo: Int? = null): JSONObject
    {
        var json = JSONObject()
        json.put("description", description)
        json.put("stepNo", stepNo)
        json.put("recipeId", recipeId)
        return json
    }
}