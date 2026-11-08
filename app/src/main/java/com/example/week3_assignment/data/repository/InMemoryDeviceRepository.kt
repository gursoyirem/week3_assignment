package com.example.week3_assignment.data.repository

import com.example.week3_assignment.data.model.DeviceCategory
import com.example.week3_assignment.data.model.SmartDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/** Ödevin PDF'inde verilen 10 örneği saklayan çevrimdışı veri kaynağı. */
class InMemoryDeviceRepository : DeviceRepository {
    private val mutableDevices = MutableStateFlow(seedDevices())
    private val writeMutex = Mutex()
    override val devices = mutableDevices.asStateFlow()

    override suspend fun toggleDevice(deviceId: Int) = withContext(Dispatchers.Default) {
        // Mutex eşzamanlı tıklamalarda hiçbir güncellemenin kaybolmamasını sağlar.
        writeMutex.withLock {
            mutableDevices.value = mutableDevices.value.map { device ->
                if (device.id == deviceId && device.isOnline) {
                    device.copy(isOn = !device.isOn)
                } else {
                    device
                }
            }
        }
    }

    companion object {
        fun seedDevices(): List<SmartDevice> = listOf(
            SmartDevice(1, "Köşe Lambası", DeviceCategory.LIGHT, "Salon", true, true, 15.0),
            SmartDevice(2, "Tavan Avizesi", DeviceCategory.LIGHT, "Salon", true, false, 60.0),
            SmartDevice(3, "Bahçe Kamerası", DeviceCategory.CAMERA, "Bahçe", true, true, 8.5),
            SmartDevice(4, "Giriş Kamerası", DeviceCategory.CAMERA, "Antre", true, true, 8.5),
            SmartDevice(5, "Merkezi Termostat", DeviceCategory.THERMOSTAT, "Salon", true, true, 12.0),
            SmartDevice(6, "Yatak Odası Kliması", DeviceCategory.THERMOSTAT, "Yatak Odası", true, false, 1200.0),
            SmartDevice(7, "Mutfak Spotları", DeviceCategory.LIGHT, "Mutfak", true, false, 45.0),
            SmartDevice(8, "Roborock S7", DeviceCategory.VACUUM, "Koridor", true, true, 50.0),
            SmartDevice(9, "Balkon Aydınlatması", DeviceCategory.LIGHT, "Balkon", false, false, 25.0),
            SmartDevice(10, "Çalışma Odası Lambası", DeviceCategory.LIGHT, "Ofis", true, true, 12.0)
        )
    }
}
