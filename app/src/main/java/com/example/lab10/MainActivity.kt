package com.example.lab10

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lab10.ui.theme.Lab10Theme
import com.example.lab10.view.SeriesApp

private const val PERMISO_RED_LOCAL = "android.permission.ACCESS_LOCAL_NETWORK"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Iconos del sistema claros, porque la app es oscura
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        // En Android 17+ hay que pedir este permiso para poder llegar a la API local
        if (Build.VERSION.SDK_INT >= 37 &&
            checkSelfPermission(PERMISO_RED_LOCAL) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(PERMISO_RED_LOCAL), 1)
        }
        setContent {
            Lab10Theme {
                SeriesApp()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab10Theme {
        Greeting("Android")
    }
}
