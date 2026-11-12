package com.baothanhbin.shortcompose.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.baothanhbin.shortcompose.R
import com.baothanhbin.shortcompose.app.navigation.TopLevelDestination

private val SelectedColor = Color(0xFFEA7B71)
private val SelectedTabShape = RoundedCornerShape(16.dp)

@Composable
fun CinemixBottomBar(
    selectedDestination: TopLevelDestination,
    onDestinationClick: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(321.dp)
                .height(68.dp)
                .clip(RoundedCornerShape(34.dp))
        ) {
            Image(
                painter = painterResource(R.drawable.bg_cinemix_glass),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds,
            )

            Row(
                modifier = Modifier
                    .matchParentSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                TopBarTab(
                    destination = TopLevelDestination.CINEMIX,
                    selected = selectedDestination == TopLevelDestination.CINEMIX,
                    onClick = onDestinationClick,
                )

                Spacer(Modifier.width(16.dp))

                TopBarTab(
                    destination = TopLevelDestination.PLAYLIST,
                    selected = selectedDestination == TopLevelDestination.PLAYLIST,
                    onClick = onDestinationClick,
                )

                Spacer(Modifier.width(16.dp))

                TopBarTab(
                    destination = TopLevelDestination.SETTINGS,
                    selected = selectedDestination == TopLevelDestination.SETTINGS,
                    onClick = onDestinationClick,
                )
            }
        }
    }
}

@Composable
private fun TopBarTab(
    destination: TopLevelDestination,
    selected: Boolean,
    onClick: (TopLevelDestination) -> Unit,
) {

    val color = if (selected) SelectedColor else Color.White

    Box(
        modifier = Modifier
            // The XML bar changes these values immediately. Keeping that behavior
            // avoids three simultaneous layout animations on every destination change.
            .width(if (selected) 113.dp else 80.dp)
            .height(if (selected) 52.dp else 48.dp)
            .clip(SelectedTabShape)
            .clickable { onClick(destination) },
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Image(
                painter = painterResource(R.drawable.bg_cinemix_glass_16),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds,
            )
        }

        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(
                    if (selected) destination.selectedIconId
                    else destination.unselectedIconId,
                ),
                contentDescription = stringResource(destination.labelId),
                tint = color,
                modifier = Modifier.size(24.dp),
            )

            if (selected) {
                Text(
                    text = stringResource(destination.labelId),
                    color = SelectedColor,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp, end = 4.dp),
                )
            }
        }
    }
}
