package com.example.lab10.ui.theme

import androidx.compose.ui.graphics.Color

// Tema oscuro estilo plataforma de streaming
val Fondo = Color(0xFF0B0F17)
val Superficie = Color(0xFF141A26)
val SuperficieAlta = Color(0xFF1E2636)
val Borde = Color(0xFF2A3346)
val Acento = Color(0xFF1A98FF)
val AcentoMorado = Color(0xFF8A3FFC)
val TextoPrincipal = Color(0xFFF2F4F8)
val TextoSecundario = Color(0xFF9AA4B5)
val Dorado = Color(0xFFF5C518)

// Color de cada categoría de la API
val ColorDrama = Color(0xFF4F5BD5)
val ColorComedy = Color(0xFFE08A1E)
val ColorHorror = Color(0xFFC0392B)
val ColorAction = Color(0xFF0E9F8C)
val ColorThriller = Color(0xFF6D4C9F)
val ColorScifi = Color(0xFF1C7ED6)
val ColorFantasy = Color(0xFF9C36B5)
val ColorCrime = Color(0xFF5C6B7A)
val ColorRomance = Color(0xFFD6336C)
val ColorAnimation = Color(0xFF37B24D)

fun colorCategoria(categoria: String): Color = when (categoria.lowercase()) {
    "drama" -> ColorDrama
    "comedy" -> ColorComedy
    "horror" -> ColorHorror
    "action" -> ColorAction
    "thriller" -> ColorThriller
    "scifi" -> ColorScifi
    "fantasy" -> ColorFantasy
    "crime" -> ColorCrime
    "romance" -> ColorRomance
    "animation" -> ColorAnimation
    else -> TextoSecundario
}
