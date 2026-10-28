package com.example.lab10.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val StreamingColorScheme = darkColorScheme(
    primary = Acento,
    onPrimary = TextoPrincipal,
    primaryContainer = Fondo,
    onPrimaryContainer = TextoPrincipal,
    secondary = AcentoMorado,
    onSecondary = TextoPrincipal,
    secondaryContainer = Acento.copy(alpha = 0.18f),
    onSecondaryContainer = Acento,
    background = Fondo,
    onBackground = TextoPrincipal,
    surface = Superficie,
    onSurface = TextoPrincipal,
    surfaceVariant = SuperficieAlta,
    onSurfaceVariant = TextoSecundario,
    surfaceContainer = Superficie,
    surfaceContainerHigh = SuperficieAlta,
    outline = Borde,
    error = ColorHorror
)

@Composable
fun Lab10Theme(content: @Composable () -> Unit) {
    // La app usa siempre su tema oscuro de streaming
    MaterialTheme(
        colorScheme = StreamingColorScheme,
        typography = Typography,
        content = content
    )
}
