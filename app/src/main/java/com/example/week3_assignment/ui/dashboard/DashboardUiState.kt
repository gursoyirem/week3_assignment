package com.example.week3_assignment.ui.dashboard

import com.example.week3_assignment.data.model.DeviceCategory

/** Compose katmanının sadece ekrana basacağı, hesaplanmış ve hazır veriler. */
data class DeviceRowUiModel(
    val id: Int,
    val name: String,
    val room: String,
    val category: DeviceCategory,
    val powerLabel: String,
    val isOnline: Boolean,
    val isOn: Boolean,
    val statusLabel: String
)

data class DashboardUiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val selectedRoom: String = DashboardViewModel.ALL_ROOMS,
    val rooms: List<String> = listOf(DashboardViewModel.ALL_ROOMS),
    val visibleDevices: List<DeviceRowUiModel> = emptyList(),
    val totalWattLabel: String = "0,0",
    val activeDevicesLabel: String = "0 aktif cihaz",
    val connectedDevicesLabel: String = "0 / 0 çevrimiçi",
    val resultsLabel: String = "0 cihaz",
    val hasDevices: Boolean = false
)
