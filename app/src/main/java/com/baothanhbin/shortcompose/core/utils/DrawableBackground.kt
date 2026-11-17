package com.baothanhbin.shortcompose.core.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat
import kotlin.math.roundToInt

/** Draws any Android drawable, including shape/selector/layer-list XML unsupported by painterResource. */
@Composable
fun Modifier.drawableBackground(@DrawableRes resourceId: Int): Modifier {
    val context = LocalContext.current
    val drawable = remember(context, resourceId) {
        ResourcesCompat.getDrawable(context.resources, resourceId, context.theme)?.mutate()
    } ?: return this
    return drawWithCache {
        drawable.setBounds(0, 0, size.width.roundToInt(), size.height.roundToInt())
        onDrawBehind {
            drawIntoCanvas { canvas -> drawable.draw(canvas.nativeCanvas) }
        }
    }
}
