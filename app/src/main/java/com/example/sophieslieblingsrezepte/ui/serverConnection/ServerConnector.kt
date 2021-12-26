package com.example.sophieslieblingsrezepte.ui.serverConnection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.sophieslieblingsrezepte.data.Result
import com.example.sophieslieblingsrezepte.data.model.Recipe
import com.example.sophieslieblingsrezepte.data.model.RecipeOverview
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CompletableFuture

class ServerConnector(private val _token: String?) {
    private val _recipeUrl = URL("https://cookbook.norsecorby.de/v1/recipe")
    private val _recipeSearchUrl = URL("https://cookbook.norsecorby.de/v1/recipe/search")
    private val _ingredientUrl = URL("https://cookbook.norsecorby.de/v1/ingredient")
    private val _preparationstepUrl = URL("https://cookbook.norsecorby.de/v1/preparationstep")
    private val _sharingruleUrl = URL("https://cookbook.norsecorby.de/v1/SharingRule")
    private val _pictureUrl = URL("https://cookbook.norsecorby.de/v1/picture")

    fun saveNewRecipe(recipe: Recipe) : Result<Boolean> {
        try
        {

            if (recipe.mainPicture != null)
            {
                recipe.pictureId = setPicture(recipe.mainPicture!!)
            }

            val recipeId = saveRecipeAsJson(recipeAsJson(recipe.name, recipe.pictureId))


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

    private fun loadRecipe(json: JSONObject, recipeOverview: RecipeOverview)
    {
        val id = json.get("id") as Int
        var pictureId: Int? = null
        if (!json.isNull("mainPictureId"))
        {
            pictureId = json.get("mainPictureId") as Int
        }

        if (!recipeOverview.recipeIds.contains(id))
        {
            recipeOverview.recipeIds.add(id)
            val recipe = Recipe(json.get("name") as String)
            recipe.recipeId = id
            recipe.pictureId = pictureId
            recipeOverview.recipes.add(recipe)
        }
    }

    fun searchRecipes() : RecipeOverview
    {
        var recipeOverview = RecipeOverview(ArrayList(), ArrayList())
        var array = getJsonArray(_recipeSearchUrl)
        for (i in 0 until array.length()) {
            val jsonObject = array.getJSONObject(i)
            loadRecipe(jsonObject, recipeOverview)
        }
        return recipeOverview
    }

    // in: recipe as json
    // out: recipeId
    private fun saveRecipeAsJson(json: JSONObject): Int?
    {
        val recipeBody = putJson(json, _recipeUrl)
        return recipeBody.get("id") as Int?
    }

    private fun saveIngredientAsJson(json: JSONObject): Int?
    {
        val ingredientBody = putJson(json, _ingredientUrl)
        return ingredientBody.get("id") as Int?
    }

    private fun saveStepAsJson(json: JSONObject): Int?
    {
        val stepBody = putJson(json, _preparationstepUrl)
        return stepBody.get("id") as Int?
    }

    private fun getJsonArray(url: URL): JSONArray
    {
        var body: JSONArray
        try {
            val future: CompletableFuture<JSONArray>? = CompletableFuture.supplyAsync{
                var responseBody = JSONArray("[]")
                val connection = url.openConnection() as HttpURLConnection
                try {
                    val bearer = "Bearer $_token"
                    connection.requestMethod = "GET"
                    connection.setRequestProperty("Authorization", bearer)
                    connection.setRequestProperty("Content-Type", "application/json; utf-8")
                    connection.setRequestProperty("Accept", "application/json")
                    val responseCode = connection.responseCode
                    println(responseCode)
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    responseBody = JSONArray(response)
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

    private fun getJson(url: URL): JSONObject
    {
        var body: JSONObject
        try {
            val future: CompletableFuture<JSONObject>? = CompletableFuture.supplyAsync{
                var responseBody = JSONObject("{}")
                val connection = url.openConnection() as HttpURLConnection
                try {
                    val bearer = "Bearer $_token"
                    connection.requestMethod = "GET"
                    connection.setRequestProperty("Authorization", bearer)
                    connection.setRequestProperty("Content-Type", "application/json; utf-8")
                    connection.setRequestProperty("Accept", "application/json")
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

    private fun putJson(json: JSONObject, url: URL): JSONObject
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
                    connection.setRequestProperty("Content-Type", "application/json; utf-8")
                    connection.setRequestProperty("Accept", "application/json")
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

    fun deleteRecipe(id: Int)
    {
        delete(id, _recipeUrl)
    }

    fun deletePicture(id: Int)
    {
        delete(id, _pictureUrl)
    }

    private fun delete(id: Int, url: URL)
    {
        val deleteURL= URL("$url/$id")
        try {
            CompletableFuture.supplyAsync{
                var responseBody = JSONObject("{}")
                val connection = deleteURL.openConnection() as HttpURLConnection
                try {
                    val bearer = "Bearer $_token"
                    connection.requestMethod = "DELETE"
                    connection.setRequestProperty("Authorization", bearer)
                    connection.setRequestProperty("Content-Type", "application/json; utf-8")
                    connection.setRequestProperty("Accept", "application/json")

                    val responseCode = connection.responseCode
                    println(responseCode)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                finally {
                    connection.disconnect()
                }
                return@supplyAsync responseBody
            }

        } catch (e: Throwable) {
            throw IOException("Server access failed", e)
        }

    }

    fun setPicture(bm: Bitmap, imageId: Int? = null): Int
    {
        var result: JSONObject
        if (imageId != null)
        {
            result = patchPicture(bm, imageId)
        }
        else
        {
            result = postPicture(bm)
        }
        val serverId = result.getInt("id")
        return serverId
    }

    private fun patchPicture(bm: Bitmap, imageId: Int): JSONObject
    {
        return JSONObject("{}")
    }

    private fun postPicture(bm: Bitmap): JSONObject
    {
        val url = URL("$_pictureUrl")
        val stream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        var body: JSONObject
        try {
            val future: CompletableFuture<JSONObject>? = CompletableFuture.supplyAsync{
                var responseBody = JSONObject("{}")
                val connection = url.openConnection() as HttpURLConnection
                try {
                    val bearer = "Bearer $_token"
                    connection.requestMethod = "POST"
                    connection.setRequestProperty("Authorization", bearer)
                    connection.setRequestProperty("Content-Type", "image/png")
                    connection.setRequestProperty("Accept", "application/json")
                    connection.doOutput = true
                    connection.outputStream.buffered().use { it.write(byteArray) }

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

    fun getPicture(imageId: Int?): Bitmap?
    {
        if (imageId != null)
        {
            var output: ByteArray
            val url = URL("$_pictureUrl/$imageId")

            try {
                val future: CompletableFuture<ByteArray>? = CompletableFuture.supplyAsync{
                    var response = ByteArray(0)
                    val connection = url.openConnection() as HttpURLConnection
                    try {
                        val bearer = "Bearer $_token"
                        connection.requestMethod = "GET"
                        connection.setRequestProperty("Authorization", bearer)
                        connection.setRequestProperty("Accept", "application/json")
                        val responseCode = connection.responseCode
                        val contentLength = connection.contentLength
                        println(responseCode)
                        response = connection.inputStream.buffered().use { it.readBytes() }
                        println(contentLength)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    finally {
                        connection.disconnect()
                    }
                    return@supplyAsync response
                }
                if (future != null) {
                    output = future.get()
                }
                else
                {
                    throw IOException("No value returned")
                }
            } catch (e: Throwable) {
                throw IOException("Server access failed", e)
            }

            val bitmap = BitmapFactory.decodeByteArray(output, 0, output.size)
            return bitmap
        }
        return null
    }

    fun getRecipe(recipeId: Int): JSONObject
    {
        val url = URL("$_recipeUrl/$recipeId")
        val json = getJson(url)
        return json
    }

    private fun recipeAsJson(name: String?, mainPictureId: Int? = null, preparationTime: Int? = null, cookingTime: Int? = null, note: String? = null): JSONObject
    {
        var json = JSONObject()
        json.put("name", name)
        json.put("preparationTime", preparationTime)
        json.put("cookingTime", cookingTime)
        json.put("note", note)
        json.put("mainPictureId",mainPictureId)
        return json
    }

    private fun ingredientAsJson(name: String?, recipeId: Int?, amount: String, unit: String?, optional: Boolean? = false, pictureId: Int? = null): JSONObject
    {
        var json = JSONObject()
        json.put("name", name)
        json.put("amount", amount)
        json.put("unit", unit)
        json.put("optional", optional)
        json.put("pictureId", pictureId)
        json.put("recipeId", recipeId)
        return json
    }

    private fun stepAsJson(description: String?, recipeId: Int?, stepNo: Int? = null): JSONObject
    {
        var json = JSONObject()
        json.put("description", description)
        json.put("stepNo", stepNo)
        json.put("recipeId", recipeId)
        return json
    }
}