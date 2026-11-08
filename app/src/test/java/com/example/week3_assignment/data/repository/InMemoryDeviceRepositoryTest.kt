package com.example.week3_assignment.data.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InMemoryDeviceRepositoryTest {
    @Test
    fun `online kapali cihaz acilip tekrar kapanabilir`() = runBlocking {
        val repository = InMemoryDeviceRepository()
        repository.toggleDevice(2)
        assertTrue(repository.devices.value.first { it.id == 2 }.isOn)
        repository.toggleDevice(2)
        assertFalse(repository.devices.value.first { it.id == 2 }.isOn)
    }

    @Test
    fun `offline cihaz anahtar ile acilamaz`() = runBlocking {
        val repository = InMemoryDeviceRepository()
        repository.toggleDevice(9)
        assertFalse(repository.devices.value.first { it.id == 9 }.isOn)
    }
}
