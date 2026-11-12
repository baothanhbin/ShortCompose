package com.baothanhbin.shortcompose.feature.cinemix

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baothanhbin.shortcompose.feature.cinemix.component.CinemixContent

@Composable
fun CinemixRoute(
    viewModel: CinemixViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    CinemixScreen(
        title = uiState.title,
    )
}

@Composable
fun CinemixScreen(
    title: String,
    modifier: Modifier = Modifier,
) {
    CinemixContent(
        title = title,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun CinemixScreenPreview() {
    CinemixScreen(
        title = "Cinemix Preview Title"
    )
}