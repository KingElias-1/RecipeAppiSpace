package com.hgecapsi.recipeapp.API

object RandomDish {

    data class Recipes(
    val recipes: List<Recipe> = listOf()
    )

    data class Recipe(
        val aggregateLikes: Int = 0,
        val analyzedInstructions: List<AnalyzedInstruction> = listOf(),
        val cheap: Boolean = false,
        val cookingMinutes: Int = 0,
        val creditsText: String = "",
        val cuisines: List<String> = listOf(),
        val dairyFree: Boolean = false,
        val diets: List<String> = listOf(),
        val dishTypes: List<String> = listOf(),
        val extendedIngredients: List<ExtendedIngredient> = listOf(),
        val gaps: String = "",
        val glutenFree: Boolean = false,
        val healthScore: Double = 0.0,
        val id: Int = 0,
        val image: String = "",
        val imageType: String = "",
        val instructions: String = "",
        val license: String = "",
        val lowFodmap: Boolean = false,
        val occasions: List<String> = listOf(),
        val originalId: Any = Any(),
        val preparationMinutes: Int = 0,
        val pricePerServing: Double = 0.0,
        val readyInMinutes: Int = 0,
        val servings: Int = 0,
        val sourceName: String = "",
        val sourceUrl: String = "",
        val spoonacularScore: Double = 0.0,
        val spoonacularSourceUrl: String = "",
        val summary: String = "",
        val sustainable: Boolean = false,
        val title: String = "",
        val vegan: Boolean = false,
        val vegetarian: Boolean = false,
        val veryHealthy: Boolean = false,
        val veryPopular: Boolean = false,
        val weightWatcherSmartPoints: Int = 0
    )

    data class AnalyzedInstruction(
        val name: String = "",
        val steps: List<Step> = listOf()
    )

    data class ExtendedIngredient(
        val aisle: String = "",
        val amount: Double = 0.0,
        val consistency: String = "",
        val id: Int = 0,
        val image: String = "",
        val measures: Measures = Measures(),
        val meta: List<String> = listOf(),
        val name: String = "",
        val nameClean: String = "",
        val original: String = "",
        val originalName: String = "",
        val unit: String = ""
    )

    data class Step(
        val equipment: List<Equipment> = listOf(),
        val ingredients: List<Ingredient> = listOf(),
        val length: Length = Length(),
        val number: Int = 0,
        val step: String = ""
    )

    data class Equipment(
        val id: Int = 0,
        val image: String = "",
        val localizedName: String = "",
        val name: String = "",
        val temperature: Temperature = Temperature()
    )

    data class Ingredient(
        val id: Int = 0,
        val image: String = "",
        val localizedName: String = "",
        val name: String = ""
    )

    data class Length(
        val number: Int = 0,
        val unit: String = ""
    )

    data class Temperature(
        val number: Double = 0.0,
        val unit: String = ""
    )

    data class Measures(
        val metric: Metric = Metric(),
        val us: Us = Us()
    )

    data class Metric(
        val amount: Double = 0.0,
        val unitLong: String = "",
        val unitShort: String = ""
    )

    data class Us(
        val amount: Double = 0.0,
        val unitLong: String = "",
        val unitShort: String = ""
    )

}