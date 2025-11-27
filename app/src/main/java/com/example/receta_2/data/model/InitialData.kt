package com.example.receta_2.data.model

import com.example.receta_2.R

val allCategories: List<SearchCategory> = listOf(
    SearchCategory("entradas", "Entradas", R.drawable.entradas, 4, CategoryGroup.MEAL_TYPE),
    SearchCategory("guarniciones", "Guarniciones", R.drawable.guarniciones, 4, CategoryGroup.MEAL_TYPE),
    SearchCategory("postres", "Postres", R.drawable.postres, 4, CategoryGroup.MEAL_TYPE),
    SearchCategory("sopas_y_cremas", "Sopas y cremas", R.drawable.sopasycremas, 4, CategoryGroup.MEAL_TYPE),
    SearchCategory("ensaladas", "Ensaladas", R.drawable.ensaladas, 4, CategoryGroup.MEAL_TYPE),
    SearchCategory("salsas_y_aderezos", "Salsas y aderezos", R.drawable.salsasyaderezos, 4, CategoryGroup.MEAL_TYPE),
    SearchCategory("bocadillos", "Bocadillos", R.drawable.bocadillos, 4, CategoryGroup.MEAL_TYPE),
    SearchCategory("tapas", "Tapas", R.drawable.tapas, 4, CategoryGroup.MEAL_TYPE),

    SearchCategory("desayuno", "Desayuno", R.drawable.desayuno, 2, CategoryGroup.TIME_OF_DAY),
    SearchCategory("almuerzo", "Almuerzo", R.drawable.almuerzo, 3, CategoryGroup.TIME_OF_DAY),
    SearchCategory("cena", "Cena", R.drawable.cena, 3, CategoryGroup.TIME_OF_DAY),
    SearchCategory("merienda", "Merienda", R.drawable.merienda, 2, CategoryGroup.TIME_OF_DAY),

    SearchCategory("mediterranea", "Mediterránea", R.drawable.mediterranea, 2, CategoryGroup.CUISINE_STYLE),
    SearchCategory("asiatica", "Asiática", R.drawable.asiatica, 1, CategoryGroup.CUISINE_STYLE),
    SearchCategory("mexicana", "Mexicana", R.drawable.mexicana, 1, CategoryGroup.CUISINE_STYLE),
    SearchCategory("italiana", "Italiana", R.drawable.italiana, 2, CategoryGroup.CUISINE_STYLE),
    SearchCategory("chilena", "Chilena", R.drawable.chilena, 1, CategoryGroup.CUISINE_STYLE),

    SearchCategory("vegetarianas", "Vegetarianas", R.drawable.vegetarianas, 2, CategoryGroup.DIETARY_NEED),
    SearchCategory("veganas", "Veganas", R.drawable.veganas, 1, CategoryGroup.DIETARY_NEED),
    SearchCategory("sin_gluten", "Sin gluten", R.drawable.ic_launcher_background, 1, CategoryGroup.DIETARY_NEED),
    SearchCategory("bajo_en_calorias", "Bajo en calorías", R.drawable.ic_launcher_background, 2, CategoryGroup.DIETARY_NEED),

    SearchCategory("horneados", "Horneados", R.drawable.ic_launcher_background, 1, CategoryGroup.COOKING_METHOD),
    SearchCategory("a_la_parrilla", "A la parrilla", R.drawable.ic_launcher_background, 1, CategoryGroup.COOKING_METHOD),
    SearchCategory("fritos", "Fritos", R.drawable.ic_launcher_background, 0, CategoryGroup.COOKING_METHOD),
    SearchCategory("sin_coccion", "Sin cocción", R.drawable.ic_launcher_background, 2, CategoryGroup.COOKING_METHOD),

    SearchCategory("navidad", "Navidad", R.drawable.ic_launcher_background, 0, CategoryGroup.SPECIAL_OCCASION),
    SearchCategory("verano", "Verano", R.drawable.ic_launcher_background, 2, CategoryGroup.SPECIAL_OCCASION),
    SearchCategory("fiestas", "Fiestas", R.drawable.ic_launcher_background, 0, CategoryGroup.SPECIAL_OCCASION),

    SearchCategory("para_principiantes", "Para principiantes", R.drawable.ic_launcher_foreground, 3, CategoryGroup.DIFFICULTY),
    SearchCategory("nivel_intermedio", "Nivel intermedio", R.drawable.ic_launcher_background, 1, CategoryGroup.DIFFICULTY),
    SearchCategory("avanzado", "Avanzado", R.drawable.ic_launcher_foreground, 0, CategoryGroup.DIFFICULTY)
)

val sampleRecipes: List<Recipe> = listOf(
    Recipe("entrada_01", "Bruschetta de Tomate", "Clásica entrada italiana, fresca y llena de sabor.", "https://picsum.photos/id/292/800/600",
        listOf("entradas", "sin_coccion", "para_principiantes", "mediterranea", "vegetarianas", "verano", "italiana"),
        listOf("4 rebanadas de pan rústico", "2 tomates maduros", "1 diente de ajo", "Hojas de albahaca fresca", "Aceite de oliva virgen extra", "Sal y pimienta al gusto"),
        listOf("Tostar las rebanadas de pan.", "Frotar suavemente el diente de ajo sobre el pan tostado.", "Picar los tomates y la albahaca, y mezclarlos en un bol con aceite de oliva, sal y pimienta.", "Colocar la mezcla de tomate sobre el pan.")
    ),
    Recipe("entrada_02", "Ceviche Clásico", "Pescado marinado en limón, un plato refrescante y ligero.", "https://picsum.photos/id/31/800/600",
        listOf("entradas", "chilena", "sin_coccion", "bajo_en_calorias", "verano"),
        listOf("500g de pescado blanco firme", "1 taza de jugo de limón fresco", "1 cebolla morada en juliana", "1 ají sin venas picado", "Cilantro fresco", "Sal y pimienta"),
        listOf("Cortar el pescado en cubos.", "Marinar con limón 20 minutos.", "Añadir cebolla, ají, cilantro, sal y pimienta.", "Servir frío.")
    ),
    Recipe("principal_01", "Lasaña a la Boloñesa", "Un clásico para comida familiar.", "https://picsum.photos/id/219/800/600",
        listOf("platos_principales", "italiana", "horneados", "almuerzo", "cena", "nivel_intermedio"),
        listOf("Láminas de lasaña", "Carne molida", "Cebolla", "Ajo", "Tomate triturado", "Bechamel", "Queso parmesano", "Sal y pimienta"),
        listOf("Sofreír base.", "Cocinar boloñesa.", "Hacer bechamel.", "Montar capas.", "Hornear a 180°C 30-40 minutos.")
    ),
    Recipe("postre_01", "Tarta de Manzana", "Rústica y deliciosa con canela.", "https://picsum.photos/id/431/800/600",
        listOf("postres", "horneados", "merienda", "vegetarianas"),
        listOf("Masa", "Manzanas", "Azúcar", "Canela", "Limón", "Mermelada (opcional)"),
        listOf("Precalentar horno.", "Preparar manzanas.", "Montar tarta.", "Hornear 35-45 minutos.")
    ),
    Recipe("desayuno_01", "Tostadas con Palta y Huevo", "Desayuno completo, nutritivo y rápido.", "https://picsum.photos/id/180/800/600",
        listOf("desayuno", "para_principiantes", "vegetarianas", "bajo_en_calorias"),
        listOf("Pan integral", "Palta", "Huevos", "Sal, pimienta, aceite de oliva", "Copos de chile (opcional)"),
        listOf("Tostar pan.", "Cocinar huevos.", "Aplastar palta.", "Armar y condimentar.")
    )
)
