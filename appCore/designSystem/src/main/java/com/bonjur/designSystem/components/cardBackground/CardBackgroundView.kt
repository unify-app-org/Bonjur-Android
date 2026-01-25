import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bonjur.designSystem.commonModel.AppUIEntities
import com.bonjur.designSystem.ui.theme.colors.Palette
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize

@Composable
fun CardBackgroundView(
    modifier: Modifier = Modifier,
    cardType: AppUIEntities.ActivityType = AppUIEntities.ActivityType.COMMUNITY,
    bgColorType: AppUIEntities.BackgroundType = AppUIEntities.BackgroundType.Primary,
    circleStrokeColor: Color = Palette.white.copy(alpha = 0.5f),
    strokeWidth: Dp = 40.dp,
    cornerRadius: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                color = bgColorType.bgColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .onGloballyPositioned { coordinates ->
                size = coordinates.size
            }
    ) {
        // Draw circles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width.toFloat()
            val height = size.height.toFloat()
            val strokeWidthPx = with(density) { strokeWidth.toPx() }

            if (width > 0 && height > 0) {
                // First circle
                val firstCircleRadius = width * 0.4f / 2f
                val firstCircleX = width * if (cardType == AppUIEntities.ActivityType.COMMUNITY) 0.6f else 0.8f
                val firstCircleY = -width * 0.05f

                drawCircle(
                    color = circleStrokeColor,
                    radius = firstCircleRadius,
                    center = Offset(firstCircleX, firstCircleY),
                    style = Stroke(width = strokeWidthPx)
                )

                // Second circle (only for community type)
                if (cardType == AppUIEntities.ActivityType.COMMUNITY) {
                    val secondCircleRadius = width * 0.4f / 2f
                    val secondCircleX = width * 0.3f
                    val secondCircleY = height + width * 0.05f

                    drawCircle(
                        color = circleStrokeColor,
                        radius = secondCircleRadius,
                        center = Offset(secondCircleX, secondCircleY),
                        style = Stroke(width = strokeWidthPx)
                    )
                }
            }
        }

        // Content
        content()
    }
}