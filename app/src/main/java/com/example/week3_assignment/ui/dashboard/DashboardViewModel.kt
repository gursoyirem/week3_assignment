package com.example.week3_assignment.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.week3_assignment.data.model.SmartDevice
import com.example.week3_assignment.data.repository.DeviceRepository
import com.example.week3_assignment.data.repository.InMemoryDeviceRepository
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** UI'de filtre veya toplam hesabı yoktur; tüm türetmeler bu katmandadır. */
class DashboardViewModel(private val repository: DeviceRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val selectedRoom = MutableStateFlow(ALL_ROOMS)

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.devices,
        searchQuery,
        selectedRoom
    ) { devices, query, room ->
        buildUiState(devices, query, room)
    }
        .flowOn(Dispatchers.Default) // filtreleme/toplama ana thread'de yapılmaz
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState()
        )

    fun onSearchChange(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun onRoomSelected(room: String) {
        selectedRoom.value = room
    }

    fun onDeviceToggle(deviceId: Int) {
        viewModelScope.launch {
            repository.toggleDevice(deviceId) // Repository Dispatchers.Default kullanır.
        }
    }

    companion object {
        const val ALL_ROOMS = "Tümü"
        private val trLocale: Locale = Locale.forLanguageTag("tr-TR")

        /** Test edilebilir saf dönüşüm; Compose yalnızca bu sonucun çizimini yapar. */
        internal fun buildUiState(
            devices: List<SmartDevice>,
            searchQuery: String,
            selectedRoom: String
        ): DashboardUiState {
            val trimmedQuery = searchQuery.trim()
            val rooms = listOf(ALL_ROOMS) + devices.map { it.room }.distinct().sorted()
            val validRoom = selectedRoom.takeIf { it in rooms } ?: ALL_ROOMS

            val visible = devices.filter { device ->
                (validRoom == ALL_ROOMS || device.room == validRoom) &&
                    device.name.contains(trimmedQuery, ignoreCase = true)
            }

            // Arama / oda filtresinden BAĞIMSIZ olan ev genelindeki toplam güç.
            val active = devices.filter { it.isOnline && it.isOn }
            val watts = active.sumOf { it.powerWatts }
            val onlineCount = devices.count { it.isOnline }

            return DashboardUiState(
                isLoading = false,
                searchQuery = searchQuery,
                selectedRoom = validRoom,
                rooms = rooms,
                visibleDevices = visible.map { device ->
                    DeviceRowUiModel(
                        id = device.id,
                        name = device.name,
                        room = device.room,
                        category = device.category,
                        powerLabel = "${formatWatts(device.powerWatts)} W",
                        isOnline = device.isOnline,
                        isOn = device.isOn,
                        statusLabel = when {
                            !device.isOnline -> "Çevrimdışı"
                            device.isOn -> "Açık • Çevrimiçi"
                            else -> "Kapalı • Çevrimiçi"
                        }
                    )
                },
                totalWattLabel = formatWatts(watts),
                activeDevicesLabel = "${active.size} aktif cihaz",
                connectedDevicesLabel = "$onlineCount / ${devices.size} çevrimiçi",
                resultsLabel = "${visible.size} cihaz listeleniyor",
                hasDevices = visible.isNotEmpty()
            )
        }

        private fun formatWatts(value: Double): String =
            String.format(trLocale, "%.1f", value)

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                    "Bilinmeyen ViewModel sınıfı: $modelClass"
                }
                return DashboardViewModel(InMemoryDeviceRepository()) as T
            }
        }
    }
}
