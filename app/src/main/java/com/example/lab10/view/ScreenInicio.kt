package com.example.lab10.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab10.data.SerieApiService
import com.example.lab10.data.SerieModel
import com.example.lab10.ui.theme.Dorado
import com.example.lab10.ui.theme.colorCategoria

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ScreenInicio(
    servicio: SerieApiService,
    onVerSeries: () -> Unit,
    onAbrirSerie: (Int) -> Unit
) {
    var series by remember { mutableStateOf<List<SerieModel>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            // Ordenadas de mayor a menor rating
            series = servicio.selectSeries().sortedByDescending { it.rating }
        } catch (e: Exception) {
            Log.e("SERIE-INICIO", "Error al conectar con la API", e)
        }
        cargando = false
    }

    if (cargando) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val destacada = series.firstOrNull()
    // Cantidad de series por categoría, de la que tiene más a la que tiene menos
    val porCategoria = series.groupingBy { it.category.lowercase() }.eachCount()
        .toList().sortedByDescending { it.second }
    val promedio = if (series.isEmpty()) 0.0 else series.map { it.rating }.average()

    // Todo el contenido entra en una sola pantalla (sin desplazarse)
    Column(modifier = Modifier.fillMaxSize()) {
        // Serie destacada: ocupa el espacio que sobre, así no quedan huecos
        FondoDegradado(
            color = colorCategoria(destacada?.category ?: ""),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = destacada?.name?.take(1)?.uppercase() ?: "S",
                fontSize = 220.sp,
                fontWeight = FontWeight.Black,
                color = Color.White.copy(alpha = 0.07f),
                modifier = Modifier.align(Alignment.TopCenter)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (destacada != null) "SERIE DESTACADA" else "BIENVENIDO",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = destacada?.name ?: "Series App",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 36.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                if (destacada != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("★ ${destacada.rating}/10", color = Dorado, fontWeight = FontWeight.Bold)
                        Text(destacada.release_date.take(4), color = Color.White.copy(alpha = 0.8f))
                        Text(nombreCategoria(destacada.category), color = Color.White.copy(alpha = 0.8f))
                    }
                } else {
                    Text("Inicia Django para ver tus series", color = Color.White.copy(alpha = 0.8f))
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = onVerSeries, shape = RoundedCornerShape(8.dp)) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Ver catálogo", fontWeight = FontWeight.SemiBold)
                    }
                    if (destacada != null) {
                        OutlinedButton(
                            onClick = { onAbrirSerie(destacada.id) },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Outlined.Edit, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(6.dp))
                            Text("Editar", color = Color.White)
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Resumen rápido del catálogo
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Dato("${series.size}", "Series", Modifier.weight(1f))
                Dato("★ ${"%.1f".format(promedio)}", "Promedio", Modifier.weight(1f), Dorado)
                Dato("${porCategoria.size}", "Categorías", Modifier.weight(1f))
            }

            if (series.isNotEmpty()) {
                // Top 3 en una fila fija, sin deslizar
                Text(text = "Top 3 mejor valoradas", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    series.take(3).forEach { serie ->
                        PosterSerie(
                            nombre = serie.name,
                            categoria = serie.category,
                            rating = serie.rating,
                            fecha = serie.release_date,
                            id = serie.id,
                            tamTitulo = 13.sp,
                            modifier = Modifier.weight(1f),
                            onClick = { onAbrirSerie(serie.id) }
                        )
                    }
                    // Espacios vacíos si hay menos de 3 series
                    repeat(3 - series.take(3).size) { Spacer(Modifier.weight(1f)) }
                }

                Text(text = "Categorías", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    porCategoria.forEach { (categoria, cantidad) ->
                        val color = colorCategoria(categoria)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(color.copy(alpha = 0.16f), RoundedCornerShape(50))
                                .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(50))
                                .clickable { onVerSeries() }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Box(Modifier.size(8.dp).background(color, CircleShape))
                            Spacer(Modifier.width(6.dp))
                            Text(nombreCategoria(categoria), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Spacer(Modifier.width(6.dp))
                            Text("$cantidad", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
fun Dato(valor: String, titulo: String, modifier: Modifier = Modifier, color: Color = Color.White) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(valor, fontSize = 20.sp, fontWeight = FontWeight.Black, color = color)
        Text(titulo, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
