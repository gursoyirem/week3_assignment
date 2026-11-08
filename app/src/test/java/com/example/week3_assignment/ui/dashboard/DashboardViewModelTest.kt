package com.example.week3_assignment.ui.dashboard

import com.example.week3_assignment.data.repository.InMemoryDeviceRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DashboardViewModelTest {
    private val devices = InMemoryDeviceRepository.seedDevices()

    @Test
    fun `ilk ekran dogru toplam ve cihaz sayisini gosterir`() {
        val state = DashboardViewModel.buildUiState(devices, "", DashboardViewModel.ALL_ROOMS)

        assertEquals("106,0", state.totalWattLabel)
        assertEquals("6 aktif cihaz", state.activeDevicesLabel)
        assertEquals("9 / 10 çevrimiçi", state.connectedDevicesLabel)
        assertEquals(10, state.visibleDevices.size)
    }

    @Test
    fun `arama ve oda filtresi birlikte calisir ama ev genelindeki toplam sabit kalir`() {
        val state = DashboardViewModel.buildUiState(devices, "tavan", "Salon")

        assertEquals(1, state.visibleDevices.size)
        assertEquals("Tavan Avizesi", state.visibleDevices.single().name)
        assertEquals("106,0", state.totalWattLabel)
    }

    @Test
    fun `buyuk kucuk harften bagimsiz arama desteklenir`() {
        val state = DashboardViewModel.buildUiState(devices, "kamera", DashboardViewModel.ALL_ROOMS)
        assertEquals(2, state.visibleDevices.size)
    }

    @Test
    fun `eslesme yoksa bos durum dogru hesaplanir`() {
        val state = DashboardViewModel.buildUiState(devices, "bulunmayan", "Salon")
        assertFalse(state.hasDevices)
        assertTrue(state.visibleDevices.isEmpty())
    }

    @Test
    fun `offline cihaz guc hesabina katilmaz`() {
        val changed = devices.map { if (it.id == 9) it.copy(isOn = true) else it }
        val state = DashboardViewModel.buildUiState(changed, "", DashboardViewModel.ALL_ROOMS)
        assertEquals("106,0", state.totalWattLabel)
    }

    @Test
    fun `kapali klima acilirsa guc 1200 watt artar`() {
        val changed = devices.map { if (it.id == 6) it.copy(isOn = true) else it }
        val state = DashboardViewModel.buildUiState(changed, "", DashboardViewModel.ALL_ROOMS)
        assertEquals("1306,0", state.totalWattLabel)
        assertEquals("7 aktif cihaz", state.activeDevicesLabel)
    }
}
