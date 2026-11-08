package com.example.week3_assignment.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week3_assignment.data.model.DeviceCategory
import com.example.week3_assignment.ui.theme.CardBorder
import com.example.week3_assignment.ui.theme.Navy
import com.example.week3_assignment.ui.theme.OfflineRed
import com.example.week3_assignment.ui.theme.PageBackground
import com.example.week3_assignment.ui.theme.Teal
import com.example.week3_assignment.ui.theme.TealSoft
import com.example.week3_assignment.ui.theme.TextPrimary
import com.example.week3_assignment.ui.theme.TextSecondary

/**
 * Stateless Compose UI: herhangi bir remember/mutableState/filter/sumOf içermez.
 * Tüm veriler ViewModel'den parametrelerle gelir, kullanıcı olayları callback ile gider.
 */
@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onSearchChanged: (String) -> Unit,
    onRoomSelected: (String) -> Unit,
    onDeviceToggled: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
            .safeDrawingPadding()
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                color = Teal,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item(key = "header") { DashboardHeader() }
                item(key = "energy") { EnergyCard(state) }
                item(key = "search") {
                    SearchField(
                        value = state.searchQuery,
                        onValueChanged = onSearchChanged
                    )
                }
                item(key = "rooms_label") {
                    SectionTitle(title = "Odalar", subtitle = "İstediğin odayı seç")
                }
                item(key = "rooms") {
                    RoomFilters(
                        rooms = state.rooms,
                        selectedRoom = state.selectedRoom,
                        onRoomSelected = onRoomSelected
                    )
                }
                item(key = "devices_label") {
                    SectionTitle(title = "Cihazlar", subtitle = state.resultsLabel)
                }
                if (state.hasDevices) {
                    items(state.visibleDevices, key = { it.id }) { device ->
                        DeviceCard(device = device, onToggle = onDeviceToggled)
                    }
                } else {
                    item(key = "empty") { EmptyDevicesCard() }
                }
                item(key = "footer") {
                    Text(
                        text = "Enerji toplamına yalnızca açık ve çevrimiçi cihazlar dahildir.",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Akıllı Evim", style = MaterialTheme.typography.headlineMedium, color = Navy)
            Text("Evinin kontrolü senin elinde", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }
        Box(
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).background(TealSoft),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Home, contentDescription = null, tint = Teal, modifier = Modifier.size(25.dp))
        }
    }
}

@Composable
private fun EnergyCard(state: DashboardUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Navy),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(Navy, Color(0xFF245F73))))
                .padding(22.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(37.dp).clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.16f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Bolt, contentDescription = null, tint = Color(0xFFFFD77A), modifier = Modifier.size(21.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("CANLI ENERJİ TÜKETİMİ", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        letterSpacing = 0.9.sp, color = Color.White.copy(alpha = 0.74f))
                    Text("Anlık güç kullanımı", fontSize = 12.sp, color = Color.White.copy(alpha = 0.86f))
                }
            }
            Spacer(Modifier.height(18.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(state.totalWattLabel, color = Color.White, fontWeight = FontWeight.Bold,
                    fontSize = 39.sp, lineHeight = 43.sp)
                Spacer(Modifier.width(7.dp))
                Text("W", fontSize = 19.sp, color = Color.White.copy(alpha = 0.78f),
                    modifier = Modifier.padding(bottom = 5.dp))
            }
            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.20f))
            Spacer(Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EnergyInfo(label = state.activeDevicesLabel, icon = Icons.Rounded.CheckCircle)
                EnergyInfo(label = state.connectedDevicesLabel, icon = Icons.Rounded.Home)
            }
        }
    }
}

@Composable
private fun EnergyInfo(label: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(icon, contentDescription = null, tint = Color(0xFF86F0CD), modifier = Modifier.size(16.dp))
        Text(text = label, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SearchField(value: String, onValueChanged: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text("Cihaz adıyla ara...", color = TextSecondary) },
        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = TextSecondary) },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChanged("") }) {
                    Icon(Icons.Rounded.Close, contentDescription = "Aramayı temizle")
                }
            }
        },
        shape = RoundedCornerShape(17.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Teal,
            unfocusedBorderColor = CardBorder,
            cursorColor = Teal
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, color = TextPrimary, style = MaterialTheme.typography.titleLarge)
        Text(text = subtitle, color = TextSecondary, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun RoomFilters(
    rooms: List<String>,
    selectedRoom: String,
    onRoomSelected: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(rooms, key = { it }) { room ->
            val selected = room == selectedRoom
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable { onRoomSelected(room) },
                color = if (selected) Teal else Color.White,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (selected) Teal else CardBorder)
            ) {
                Text(
                    text = room,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 11.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selected) Color.White else TextPrimary
                )
            }
        }
    }
}

@Composable
private fun DeviceCard(device: DeviceRowUiModel, onToggle: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(15.dp))
                    .background(if (device.isOnline) TealSoft else PageBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = deviceIcon(device.category),
                    contentDescription = null,
                    tint = if (device.isOnline) Teal else TextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(11.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = device.name,
                    fontSize = 14.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text("${device.room}  ·  ${device.powerLabel}", fontSize = 11.sp, color = TextSecondary)
                Spacer(Modifier.height(7.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(6.dp).clip(CircleShape)
                            .background(if (device.isOnline) Teal else OfflineRed)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = device.statusLabel,
                        fontSize = 10.sp,
                        color = if (device.isOnline) Teal else OfflineRed,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(Modifier.width(6.dp))
            Switch(
                checked = device.isOn,
                onCheckedChange = { onToggle(device.id) },
                enabled = device.isOnline,
                modifier = Modifier.semantics {
                    contentDescription = "${device.name} ${if (device.isOn) "kapat" else "aç"}"
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Teal,
                    checkedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFDCE6EB),
                    uncheckedThumbColor = Color.White,
                    uncheckedBorderColor = Color(0xFFDCE6EB),
                    disabledUncheckedTrackColor = Color(0xFFE9EDF1),
                    disabledUncheckedThumbColor = Color(0xFFC4CDD7)
                )
            )
        }
    }
}

private fun deviceIcon(category: DeviceCategory): ImageVector = when (category) {
    DeviceCategory.LIGHT -> Icons.Rounded.Lightbulb
    DeviceCategory.CAMERA -> Icons.Rounded.Videocam
    DeviceCategory.THERMOSTAT -> Icons.Rounded.AcUnit
    DeviceCategory.VACUUM -> Icons.Rounded.SmartToy
}

@Composable
private fun EmptyDevicesCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier.padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Rounded.Search, contentDescription = null, tint = Teal, modifier = Modifier.size(30.dp))
            Text("Cihaz bulunamadı", color = TextPrimary, fontWeight = FontWeight.Bold)
            Text("Aramanı veya oda seçimini değiştirebilirsin.",
                color = TextSecondary, fontSize = 12.sp)
        }
    }
}
