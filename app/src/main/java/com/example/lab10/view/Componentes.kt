package com.example.lab10.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab10.ui.theme.Borde
import com.example.lab10.ui.theme.Dorado
import com.example.lab10.ui.theme.Fondo
import com.example.lab10.ui.theme.colorCategoria

@Composable
fun PosterSerie(
    nombre: String,
    categoria: String,
    rating: Int?,
    fecha: String,
    modifier: Modifier = Modifier,
    id: Int? = null,
    tamTitulo: TextUnit = 15.sp,
    esquina: Dp = 12.dp,
    onClick: (() -> Unit)? = null
) {
    val colorCat = colorCategoria(categoria)
    val forma = RoundedCornerShape(esquina)
    Box(
        modifier = modifier
            .aspectRatio(2f / 3f)
            .clip(forma)
            .background(Brush.verticalGradient(listOf(colorCat, colorCat.copy(alpha = 0.55f), Fondo)))
            .border(1.dp, Borde, forma)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Text(
            text = nombre.trim().take(1).uppercase().ifEmpty { "?" },
            fontSize = 110.sp,
            fontWeight = FontWeight.Black,
            color = Color.White.copy(alpha = 0.13f),
            modifier = Modifier.align(Alignment.Center)
        )
        if (id != null && id > 0) {
            Etiqueta("#$id", Modifier.align(Alignment.TopStart).padding(8.dp))
        }
        if (rating != null) {
            Etiqueta("★ $rating", Modifier.align(Alignment.TopEnd).padding(8.dp), Dorado)
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = nombre.ifBlank { "Nombre de la serie" },
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = tamTitulo,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            val detalle = listOf(fecha.take(4), if (categoria.isBlank()) "" else nombreCategoria(categoria))
                .filter { it.isNotBlank() }
                .joinToString(" · ")
            if (detalle.isNotEmpty()) {
                Text(text = detalle, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun Etiqueta(texto: String, modifier: Modifier = Modifier, color: Color = Color.White) {
    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.55f), RoundedCornerShape(6.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(text = texto, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun FondoDegradado(color: Color, modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(listOf(color.copy(alpha = 0.85f), color.copy(alpha = 0.35f), Fondo))
        ),
        content = content
    )
}
