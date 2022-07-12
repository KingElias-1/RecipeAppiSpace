package com.hgecapsi.recipeapp.utils

object Constants {
        const val DISH_TYPE: String = "DishType"
        const val DISH_CATEGORY: String = "DishCategory"
        const val DISH_COOKING_TIME: String = "DishCookingTime"

        const val DISH_IMAGE_SOURCE_LOCAL: String = "Local"
        const val DISH_IMAGE_SOURCE_ONLINE: String = "Online"

        const val EXTRA_DISH_DETAILS: String = "DishDetails"

    // TODO Step 6: Add Constants for filter on all recipe page
    // START
    const val ALL_ITEMS: String = "None"
    const val FILTER_SELECTION: String = "FilterSelection"
    // END

        /**
         * This function will return the Dish Type List items.
         */
        fun dishTypes(): ArrayList<String> {
            val list = ArrayList<String>()
            list.add("breakfast")
            list.add("lunch")
            list.add("snacks")
            list.add("dinner")
            list.add("salad")
            list.add("side dish")
            list.add("dessert")
            list.add("other")
            return list
        }

        /**
         *  This function will return the Dish Category list items.
         */
        fun dishCategories(): ArrayList<String> {
            val list = ArrayList<String>()
            list.add("Pizza")
            list.add("BBQ")
            list.add("Pastry")
            list.add("Burger")
            list.add("Cafe")
            list.add("Chicken")
            list.add("Dessert")
            list.add("Drinks")
            list.add("Hot Dogs")
            list.add("Juices")
            list.add("Sandwich")
            list.add("Tea & Coffee")
            list.add("Wraps")
            list.add("Other")
            return list
        }


        /**
         *  This function will return the Dish Cooking Time list items. The time added is in Minutes.
         */
        fun dishCookTime(): ArrayList<String> {
            val list = ArrayList<String>()
            list.add("10")
            list.add("15")
            list.add("20")
            list.add("30")
            list.add("45")
            list.add("50")
            list.add("60")
            list.add("90")
            list.add("120")
            list.add("150")
            list.add("180")
            return list
        }

    //setting up constants for the api fetch
    // TODO Step 2: Define the API key and query param of the URL as that we are going to use in the URL to get the response.
    // START
    // TODO Step 2: Add the BASE URL of the API.
    // START
    const val BASE_URL = "https://api.spoonacular.com/"
    // END
    // TODO Step 5: Add the default values to the constants.
    // START
    // KEY PARAMS VALUES ==> YOU CAN CHANGE AS PER REQUIREMENT FROM HERE TO MAKE THE DIFFERNCE IN THE API RESPONSE.
    const val LIMIT_LICENSE_VALUE: Boolean = true
    const val TAGS_VALUE: String = "vegetarian,dessert"
    const val NUMBER_VALUE: Int = 2
    // END
    const val API_ENDPOINT:String = "recipes/random"

    // API KEY VALUE from the spoonacular console.
    const val API_KEY_VALUE: String = "f6b01f9931d947c3a939ec1042de82cb"

    // KEY PARAMS
    const val API_KEY: String = "apiKey"
    const val LIMIT_LICENSE: String = "limitLicense"
    const val TAGS: String = "tags"
    const val NUMBER: String = "number"

    // END
    // TODO Step 3: Add the constants that we will use while building the Notification.
    // START
    const val NOTIFICATION_ID = "FavDish_notification_id"
    const val NOTIFICATION_NAME = "FavDish"
    const val NOTIFICATION_CHANNEL = "FavDish_channel_01"
    // END
}