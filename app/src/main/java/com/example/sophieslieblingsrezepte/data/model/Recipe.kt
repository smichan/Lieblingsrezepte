package com.example.sophieslieblingsrezepte.data.model

class Recipe(name: String)
{
    var name: String = name
    var recipeId: Int? = null
    var ingredients: List<Ingredient> = listOf()
    var steps: List<Step> = listOf()


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
