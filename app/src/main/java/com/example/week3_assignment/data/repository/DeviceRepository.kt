package com.example.week3_assignment.data.repository

import com.example.week3_assignment.data.model.SmartDevice
import kotlinx.coroutines.flow.StateFlow

/** Veri erişimi arayüzü: ViewModel veri kaynağının detayını bilmez. */
interface DeviceRepository {
    val devices: StateFlow<List<SmartDevice>>
    suspend fun toggleDevice(deviceId: Int)
}
