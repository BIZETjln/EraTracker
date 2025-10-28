package com.bizetj.goldeneratracker.ui.screens.welcome

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bizetj.goldeneratracker.R
import com.bizetj.goldeneratracker.ui.theme.GoldenPrimary
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    onContinue: () -> Unit
) {
    var startAnimations by remember { mutableStateOf(false) }

    // Animation du logo (scale + fade)
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimations) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimations) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "logoAlpha"
    )

    // Animation du texte clignotant (pulse élégant)
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    // Démarrer les animations au lancement
    LaunchedEffect(Unit) {
        delay(200)
        startAnimations = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onContinue()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // Logo plus grand
            Image(
                painter = painterResource(id = R.drawable.get_main_logo),
                contentDescription = "Golden Era Tracker Logo",
                modifier = Modifier
                    .size(280.dp)
                    .scale(logoScale)
                    .alpha(logoAlpha)
            )

            Spacer(modifier = Modifier.height(120.dp))

            // Texte clignotant élégant avec police moderne
            if (startAnimations) {
                Text(
                    text = "Touchez l'écran pour commencer",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldenPrimary,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.alpha(pulseAlpha)
                )
            }
        }
    }
}