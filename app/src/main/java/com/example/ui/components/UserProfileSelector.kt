package com.example.ui.components

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.*
import com.example.ui.viewmodel.SyncViewModel

@Composable
fun UserProfileSelector(
    viewModel: SyncViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allProfiles by viewModel.allProfiles.collectAsState()
    val activeProfile by viewModel.activeProfile.collectAsState()
    val activeProfileId by viewModel.activeProfileId.collectAsState()

    var showAddProfileDialog by remember { mutableStateOf(false) }
    var showManageDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("user_profile_selector_card"),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Slate800)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header: Active Profile Hero Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Profile Avatar Circle
                    val avatarColor = remember(activeProfile?.avatarColorHex) {
                        try {
                            Color(android.graphics.Color.parseColor(activeProfile?.avatarColorHex ?: "#10B981"))
                        } catch (e: Exception) {
                            Emerald500
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(avatarColor.copy(alpha = 0.2f))
                            .border(1.5.dp, avatarColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        val initials = activeProfile?.name
                            ?.split(" ")
                            ?.take(2)
                            ?.mapNotNull { it.firstOrNull()?.toString() }
                            ?.joinToString("")
                            ?.uppercase() ?: "US"
                        Text(
                            text = initials,
                            color = avatarColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = activeProfile?.name ?: "Loading User...",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Surface(
                                color = Emerald500.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Emerald500.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    color = Emerald400,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                        }

                        // Unique User ID Badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "UID:",
                                color = TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Surface(
                                color = Slate950,
                                shape = RoundedCornerShape(4.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Slate800),
                                modifier = Modifier.testTag("active_user_id_badge")
                            ) {
                                Text(
                                    text = activeProfile?.id ?: activeProfileId,
                                    color = Cyan400,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        // Secondary user metadata
                        activeProfile?.let { prof ->
                            Text(
                                text = "Age: ${prof.age} • Blood: ${prof.bloodType} • ${prof.syncTarget}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Manage Profiles Action Button
                IconButton(
                    onClick = { showManageDialog = true },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Slate800, CircleShape)
                        .testTag("manage_profiles_icon_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Switch or Manage User Profiles",
                        tint = Emerald400,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Divider(color = Slate800, thickness = 1.dp)

            // Switch Profiles Horizontal Bar
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SWITCH USER PROFILE",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "${allProfiles.size} Profiles Registered",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    allProfiles.forEach { profile ->
                        val isSelected = profile.id == activeProfileId
                        val profColor = remember(profile.avatarColorHex) {
                            try {
                                Color(android.graphics.Color.parseColor(profile.avatarColorHex))
                            } catch (e: Exception) {
                                Emerald500
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Slate800 else Slate950,
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) profColor else Slate800
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    viewModel.switchProfile(profile.id, context)
                                }
                                .testTag("profile_chip_${profile.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Mini avatar
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(profColor.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = profile.name.take(1).uppercase(),
                                        color = profColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Column {
                                    Text(
                                        text = profile.name,
                                        color = if (isSelected) TextPrimary else TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                    Text(
                                        text = profile.id,
                                        color = if (isSelected) Cyan400 else TextMuted,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = profColor,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Add New Profile Action Chip
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Slate950,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate800),
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { showAddProfileDialog = true }
                            .testTag("add_profile_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Profile",
                                tint = Emerald400,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "New Profile",
                                color = Emerald400,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal: Add New Profile Dialog
    if (showAddProfileDialog) {
        CreateProfileDialog(
            onDismiss = { showAddProfileDialog = false },
            onConfirm = { name, email, age, bloodType, colorHex ->
                viewModel.createProfile(name, email, age, bloodType, colorHex)
                showAddProfileDialog = false
            }
        )
    }

    // Modal: Manage Profiles Dialog
    if (showManageDialog) {
        ManageProfilesDialog(
            profiles = allProfiles,
            activeProfileId = activeProfileId,
            onDismiss = { showManageDialog = false },
            onSwitch = { id ->
                viewModel.switchProfile(id, context)
                showManageDialog = false
            },
            onDelete = { id ->
                viewModel.deleteProfile(id)
            },
            onAddNew = {
                showManageDialog = false
                showAddProfileDialog = true
            }
        )
    }
}

@Composable
fun CreateProfileDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, email: String, age: Int, bloodType: String, colorHex: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("28") }
    var selectedBloodType by remember { mutableStateOf("O+") }
    var selectedColorHex by remember { mutableStateOf("#10B981") }

    val bloodTypes = listOf("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-")
    val avatarColors = listOf(
        "#10B981" to "Emerald",
        "#06B6D4" to "Cyan",
        "#8B5CF6" to "Violet",
        "#F59E0B" to "Amber",
        "#EC4899" to "Rose",
        "#3B82F6" to "Blue"
    )

    val previewSlug = name.trim().lowercase().replace(Regex("[^a-z0-9]"), "_").take(8).ifEmpty { "user" }
    val previewId = "usr_${previewSlug}_xxxx"

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Slate900,
        shape = RoundedCornerShape(16.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Emerald400
                )
                Text(
                    text = "Create Health Profile",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Each profile maintains isolated biometric sync streams and unique cryptographic ledger IDs.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                // Generated Unique ID preview chip
                Surface(
                    color = Slate950,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Assigned ID Prefix:",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            text = previewId,
                            color = Cyan400,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Full Name input
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Profile Name (e.g. Alex Morgan)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = Emerald500,
                        unfocusedBorderColor = Slate800
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("new_profile_name_input")
                )

                // Email input
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email (optional)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = Emerald500,
                        unfocusedBorderColor = Slate800
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("new_profile_email_input")
                )

                // Age & Blood Type Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = ageText,
                        onValueChange = { if (it.all { ch -> ch.isDigit() } && it.length <= 3) ageText = it },
                        label = { Text("Age") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = Emerald500,
                            unfocusedBorderColor = Slate800
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("new_profile_age_input")
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Blood Type", color = TextMuted, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            bloodTypes.take(4).forEach { bt ->
                                val sel = selectedBloodType == bt
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (sel) Emerald500.copy(alpha = 0.2f) else Slate950,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (sel) Emerald500 else Slate800
                                    ),
                                    modifier = Modifier.clickable { selectedBloodType = bt }
                                ) {
                                    Text(
                                        text = bt,
                                        color = if (sel) Emerald400 else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Avatar Color Palette
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Avatar Accent Color", color = TextMuted, fontSize = 11.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        avatarColors.forEach { (hex, _) ->
                            val col = Color(android.graphics.Color.parseColor(hex))
                            val isSel = selectedColorHex == hex
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(col)
                                    .border(
                                        width = if (isSel) 2.5.dp else 1.dp,
                                        color = if (isSel) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColorHex = hex }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val parsedAge = ageText.toIntOrNull() ?: 28
                        onConfirm(name, email, parsedAge, selectedBloodType, selectedColorHex)
                    }
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Emerald500,
                    contentColor = Slate950
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("create_profile_confirm_button")
            ) {
                Text("Create Profile", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        }
    )
}

@Composable
fun ManageProfilesDialog(
    profiles: List<UserProfile>,
    activeProfileId: String,
    onDismiss: () -> Unit,
    onSwitch: (String) -> Unit,
    onDelete: (String) -> Unit,
    onAddNew: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Slate900,
        shape = RoundedCornerShape(16.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = null,
                        tint = Emerald400
                    )
                    Text(
                        text = "User Profiles Ledger",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Select a profile to switch active health records and sync pipelines.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    profiles.forEach { profile ->
                        val isActive = profile.id == activeProfileId
                        val profColor = remember(profile.avatarColorHex) {
                            try {
                                Color(android.graphics.Color.parseColor(profile.avatarColorHex))
                            } catch (e: Exception) {
                                Emerald500
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isActive) Slate800 else Slate950
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isActive) profColor else Slate800
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSwitch(profile.id) }
                                .testTag("manage_profile_row_${profile.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(profColor.copy(alpha = 0.2f))
                                            .border(1.dp, profColor, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = profile.name.take(1).uppercase(),
                                            color = profColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }

                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = profile.name,
                                                color = TextPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                            if (isActive) {
                                                Surface(
                                                    color = Emerald500.copy(alpha = 0.2f),
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = "ACTIVE",
                                                        color = Emerald400,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = "ID: ${profile.id}",
                                            color = Cyan400,
                                            fontSize = 11.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = "Age: ${profile.age} • Blood: ${profile.bloodType}",
                                            color = TextMuted,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (!isActive) {
                                        Button(
                                            onClick = { onSwitch(profile.id) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Slate800,
                                                contentColor = TextPrimary
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text("Switch", fontSize = 11.sp)
                                        }

                                        if (profiles.size > 1) {
                                            IconButton(
                                                onClick = { onDelete(profile.id) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete Profile",
                                                    tint = CrimsonError,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onAddNew,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Emerald500,
                    contentColor = Slate950
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Profile", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = TextMuted)
            }
        }
    )
}
