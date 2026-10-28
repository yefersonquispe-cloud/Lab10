package com.example.lab10.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lab10.data.SerieApiService
import com.example.lab10.data.SerieModel
import com.example.lab10.ui.theme.ColorHorror
import com.example.lab10.ui.theme.Dorado
import com.example.lab10.ui.theme.colorCategoria
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

val categorias = listOf(
    "drama" to "Drama", "comedy" to "Comedy", "action" to "Action", "horror" to "Horror",
    "thriller" to "Thriller", "scifi" to "Sci-Fi", "fantasy" to "Fantasy",
    "crime" to "Crime", "romance" to "Romance", "animation" to "Animation"
)

fun nombreCategoria(valor: String): String =
    categorias.firstOrNull { it.first.equals(valor, ignoreCase = true) }?.second
        ?: valor.replaceFirstChar { it.uppercase() }

fun irASeries(navController: NavHostController) {
    navController.navigate("series") {
        popUpTo("series") { inclusive = true }
    }
}

@Composable
fun ContenidoSeriesListado(navController: NavHostController, servicio: SerieApiService) {
    var listaSeries: SnapshotStateList<SerieModel> = remember { mutableStateListOf() }
    var cargando by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var reintentar by remember { mutableStateOf(0) }

    LaunchedEffect(reintentar) {
        cargando = true
        error = null
        try {
            val listado = servicio.selectSeries()
            listaSeries.clear()
            listado.forEach { listaSeries.add(it) }
        } catch (e: Exception) {
            Log.e("SERIE-LISTADO", "Error al conectar con la API", e)
            error = "No se pudo conectar con la API.\nVerifica que Django esté ejecutándose."
        }
        cargando = false
    }

    when {
        cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        error != null -> Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.CloudOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(56.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(text = error!!, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp))
            Button(onClick = { reintentar++ }, shape = RoundedCornerShape(8.dp)) { Text("Reintentar") }
        }
        else -> BoxWithConstraints(Modifier.fillMaxSize()) {
            val anchoPoster = (maxWidth - 32.dp - 20.dp) / 3
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 100.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        Text(text = "Mi catálogo", fontSize = 28.sp, fontWeight = FontWeight.Black)
                        Text(
                            text = "${listaSeries.size} títulos · toca un póster para editarlo",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                categorias.forEach { (valor, texto) ->
                    val delGrupo = listaSeries.filter { it.category.equals(valor, ignoreCase = true) }
                    if (delGrupo.isNotEmpty()) {
                        item(key = "titulo-$valor") {
                            TituloCategoria(texto, colorCategoria(valor), delGrupo.size)
                        }
                        items(delGrupo.chunked(3)) { fila ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                fila.forEach { serie ->
                                    PosterConAcciones(
                                        serie = serie,
                                        modifier = Modifier.width(anchoPoster),
                                        onEditar = {
                                            navController.navigate("serieVer/${serie.id}")
                                            Log.e("SERIE-VER", "ID = ${serie.id}")
                                        },
                                        onEliminar = {
                                            navController.navigate("serieDel/${serie.id}")
                                            Log.e("SERIE-DEL", "ID = ${serie.id}")
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TituloCategoria(titulo: String, color: Color, cantidad: Int) {
    Row(
        modifier = Modifier.padding(top = 12.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(width = 4.dp, height = 20.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.width(8.dp))
        Text(text = titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(8.dp))
        Text(text = "$cantidad", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun PosterConAcciones(
    serie: SerieModel,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        PosterSerie(
            nombre = serie.name,
            categoria = serie.category,
            rating = serie.rating,
            fecha = serie.release_date,
            id = serie.id,
            tamTitulo = 13.sp,
            modifier = Modifier.fillMaxWidth(),
            onClick = onEditar
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onEditar) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Editar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Eliminar",
                    tint = ColorHorror
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContenidoSerieEditar(navController: NavHostController, servicio: SerieApiService, pid: Int = 0 ) {
    val esNuevo = pid == 0
    var id by remember { mutableStateOf<Int>(pid) }
    var name by remember { mutableStateOf<String?>("") }
    var release_date by remember { mutableStateOf<String?>("") }
    var rating by remember { mutableStateOf<String?>(if (esNuevo) "5" else "") }
    var category by remember { mutableStateOf<String?>("") }
    var error by remember { mutableStateOf<String?>(null) }
    var grabando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (esNuevo) {
        LaunchedEffect(Unit) {
            try {
                id = (servicio.selectSeries().maxOfOrNull { it.id } ?: 0) + 1
            } catch (e: Exception) {
                Log.e("SERIE-NUEVO", "Error al calcular el ID", e)
                error = "No se pudo conectar con la API"
            }
        }
    } else {
        LaunchedEffect(Unit) {
            try {
                val objSerie = servicio.selectSerie(id.toString())
                name = objSerie.body()?.name ?: ""
                release_date = objSerie.body()?.release_date ?: ""
                rating = objSerie.body()?.rating?.toString() ?: ""
                category = objSerie.body()?.category ?: ""
            } catch (e: Exception) {
                Log.e("SERIE-VER", "Error al obtener la serie", e)
                error = "No se pudo obtener la serie"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            PosterSerie(
                nombre = name ?: "",
                categoria = category ?: "",
                rating = rating?.toIntOrNull(),
                fecha = release_date ?: "",
                id = id,
                tamTitulo = 13.sp,
                modifier = Modifier.width(120.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = if (esNuevo) "NUEVA SERIE" else "EDITAR SERIE",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = name?.ifBlank { null } ?: "Sin título",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 28.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = if (esNuevo) "ID $id · se asigna automáticamente" else "ID $id",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
        }

        OutlinedTextField(
            value = name!!,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            leadingIcon = { Icon(Icons.Outlined.Title, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = release_date!!,
            onValueChange = { release_date = it },
            label = { Text("Fecha de estreno") },
            placeholder = { Text("AAAA-MM-DD") },
            leadingIcon = { Icon(Icons.Outlined.CalendarMonth, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        val valorRating = rating?.toIntOrNull() ?: 0
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Rating", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                Text("★ $valorRating/10", color = Dorado, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Slider(
                value = valorRating.toFloat(),
                onValueChange = { rating = it.roundToInt().toString() },
                valueRange = 0f..10f,
                steps = 9,
                colors = SliderDefaults.colors(
                    thumbColor = Dorado,
                    activeTrackColor = Dorado,
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent
                )
            )
        }

        Text("Categoría", color = MaterialTheme.colorScheme.onSurfaceVariant)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            categorias.forEach { (valor, texto) ->
                val colorCat = colorCategoria(valor)
                val seleccionada = category.equals(valor, ignoreCase = true)
                FilterChip(
                    selected = seleccionada,
                    onClick = { category = valor },
                    label = { Text(texto) },
                    shape = RoundedCornerShape(50),
                    colors = FilterChipDefaults.filterChipColors(
                        labelColor = colorCat,
                        selectedContainerColor = colorCat,
                        selectedLabelColor = Color.White
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = seleccionada,
                        borderColor = colorCat.copy(alpha = 0.6f)
                    )
                )
            }
        }

        if (error != null) {
            Text(text = error!!, color = MaterialTheme.colorScheme.error)
        }

        Button(
            enabled = !grabando,
            onClick = {
                val valorFinal = rating!!.toIntOrNull()
                if (name!!.isBlank() || release_date!!.isBlank() || category!!.isBlank() || valorFinal == null) {
                    error = "Completa el nombre, la fecha y elige una categoría"
                    return@Button
                }
                val objSerie = SerieModel(id, name!!.trim(), release_date!!.trim(), valorFinal, category!!.lowercase())
                grabando = true
                scope.launch {
                    try {
                        val respuesta = if (esNuevo)
                            servicio.insertSerie(objSerie)
                        else
                            servicio.updateSerie(id.toString(), objSerie)
                        if (respuesta.isSuccessful) {
                            irASeries(navController)
                        } else {
                            error = "La API rechazó los datos (código ${respuesta.code()}): ${respuesta.errorBody()?.string()}"
                        }
                    } catch (e: Exception) {
                        Log.e("SERIE-GRABAR", "Error al grabar la serie", e)
                        error = "No se pudo conectar con la API"
                    }
                    grabando = false
                }
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Icon(Icons.Filled.Check, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (esNuevo) "Agregar serie" else "Guardar cambios",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        TextButton(
            onClick = { irASeries(navController) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ContenidoSerieEliminar(navController: NavHostController, servicio: SerieApiService, id: Int) {
    var showDialog by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                irASeries(navController)
            },
            containerColor = MaterialTheme.colorScheme.surface,
            icon = { Icon(Icons.Outlined.Delete, contentDescription = null, tint = ColorHorror) },
            title = { Text(text = "Confirmar Eliminación") },
            text = {  Text("¿Está seguro de eliminar la Serie con ID $id?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        scope.launch {
                            try {
                                servicio.deleteSerie(id.toString())
                            } catch (e: Exception) {
                                Log.e("SERIE-DEL", "Error al eliminar la serie", e)
                            }
                            irASeries(navController)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorHorror, contentColor = Color.White)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        irASeries(navController)
                    }
                ) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}
