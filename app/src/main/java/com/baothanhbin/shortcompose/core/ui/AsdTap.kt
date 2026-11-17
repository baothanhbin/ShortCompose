package com.baothanhbin.shortcompose.core.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.launch

/** Matches Short's View.animateTap(): 0.94 scale in 70 ms, then back in 90 ms. */
fun Modifier.asdTap(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = this then AsdTapElement(enabled = enabled, onClick = onClick)

private data class AsdTapElement(
    val enabled: Boolean,
    val onClick: () -> Unit,
) : ModifierNodeElement<AsdTapNode>() {
    override fun create() = AsdTapNode(enabled, onClick)

    override fun update(node: AsdTapNode) {
        node.enabled = enabled
        node.onClick = onClick
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "asdTap"
        properties["enabled"] = enabled
        properties["onClick"] = onClick
    }
}

private class AsdTapNode(
    var enabled: Boolean,
    var onClick: () -> Unit,
) : DelegatingNode(), DrawModifierNode, PointerInputModifierNode, SemanticsModifierNode {

    private val scale = Animatable(1f)

    private val pointerInputNode = delegate(
        SuspendingPointerInputModifierNode {
            detectTapGestures(
                onTap = {
                    if (!enabled || scale.isRunning) return@detectTapGestures
                    coroutineScope.launch {
                        scale.animateTo(
                            targetValue = 0.94f,
                            animationSpec = tween(durationMillis = 70, easing = LinearOutSlowInEasing),
                        )
                        scale.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = 90, easing = FastOutSlowInEasing),
                        )
                        onClick()
                    }
                }
            )
        }
    )

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize,
    ) {
        pointerInputNode.onPointerEvent(pointerEvent, pass, bounds)
    }

    override fun onCancelPointerInput() {
        pointerInputNode.onCancelPointerInput()
    }

    override fun ContentDrawScope.draw() {
        // Drawing the scale directly avoids allocating a persistent graphics layer for
        // every card/chip. This matters while scrolling the main Lazy layouts.
        val contentScope = this
        scale(scale.value) {
            contentScope.drawContent()
        }
    }

    override fun SemanticsPropertyReceiver.applySemantics() {
        role = Role.Button
        if (!enabled) disabled()
        onClick(action = {
            if (enabled) {
                onClick()
                true
            } else {
                false
            }
        })
    }
}
