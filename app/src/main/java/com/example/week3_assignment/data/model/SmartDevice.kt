package com.example.week3_assignment.data.model

enum class DeviceCategory {
    LIGHT, CAMERA, THERMOSTAT, VACUUM
}

/** Ham verideki watt: cihaz AÇIK ve ONLINE olduğunda çektiği anlık güç. */
data class SmartDevice(
    val id: Int,
    val name: String,
    val category: DeviceCategory,
    val room: String,
    val isOnline: Boolean,
    val isOn: Boolean,
    val powerWatts: Double
)
