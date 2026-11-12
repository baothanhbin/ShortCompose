package com.baothanhbin.shortcompose.feature.settings.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.baothanhbin.shortcompose.R

@Composable
fun SettingsContent(
    title: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxSize()
            .paint(
                painterResource(R.drawable.bg_main), contentScale = ContentScale.Crop
            )
    ) {


    }
}
