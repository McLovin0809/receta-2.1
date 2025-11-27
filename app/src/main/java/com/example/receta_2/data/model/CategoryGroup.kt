package com.example.receta_2.data.model

import androidx.annotation.StringRes
import com.example.receta_2.R

enum class CategoryGroup(@StringRes val titleResId: Int) {
    MEAL_TYPE(R.string.category_group_meal_type),
    TIME_OF_DAY(R.string.category_group_time_of_day),
    CUISINE_STYLE(R.string.category_group_cuisine_style),
    DIETARY_NEED(R.string.category_group_dietary_need),
    COOKING_METHOD(R.string.category_group_cooking_method),
    SPECIAL_OCCASION(R.string.category_group_special_occasion),
    DIFFICULTY(R.string.category_group_difficulty)
}
