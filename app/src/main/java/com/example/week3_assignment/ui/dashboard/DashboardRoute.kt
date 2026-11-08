package com.example.week3_assignment.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

/** Stateful abonelik burada, stateless ekran bileşeni DashboardScreen'de. */
@Composable
fun DashboardRoute(viewModel: DashboardViewModel = viewModel(factory = DashboardViewModel.Factory)) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreen(
        state = uiState,
        onSearchChanged = viewModel::onSearchChange,
        onRoomSelected = viewModel::onRoomSelected,
        onDeviceToggled = viewModel::onDeviceToggle
    )
}
