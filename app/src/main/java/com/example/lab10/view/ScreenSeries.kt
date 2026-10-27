package com.example.lab10.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lab10.data.SerieApiService
import com.example.lab10.data.SerieModel
import kotlinx.coroutines.delay

@Composable
fun ContenidoSeriesListado(navController: NavHostController, servicio: SerieApiService) {
    var listaSeries: SnapshotStateList<SerieModel> = remember { mutableStateListOf() }
    LaunchedEffect(Unit) {
        val listado = servicio.selectSeries()
        listado.forEach { listaSeries.add(it) }
    }

    LazyColumn (

    ){
        item {
            Row (
                modifier = Modifier.fillParentMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.1f)
                )
                Text(
                    text = "SERIE",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.7f)
                )
                Text(
                    text = "Accion",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.2f)
                ) //, fontWeight = FontWeight.Bold)
            }
        }

        items(listaSeries) { item ->
            Row(
                modifier = Modifier.padding(start=8.dp).fillParentMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${item.id}", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier=Modifier.weight(0.1f))
                Text(text = item.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier=Modifier.weight(0.6f))
                IconButton(
                    onClick = {
                        navController.navigate("serieVer/${item.id}")
                        Log.e("SERIE-VER","ID = ${item.id}")
                    },
                    Modifier.weight(0.1f)
                ) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Ver", modifier=Modifier.align(Alignment.CenterVertically))
                }
                IconButton(
                    onClick = {
                        navController.navigate("serieDel/${item.id}")
                        Log.e("SERIE-DEL","ID = ${item.id}")
                    },
                    Modifier.weight(0.1f)
                ) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Ver", modifier=Modifier.align(Alignment.CenterVertically))
                }
            }
        }
    }
}
