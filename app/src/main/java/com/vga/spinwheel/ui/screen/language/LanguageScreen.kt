package com.vga.spinwheel.ui.screen.language

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vga.spinwheel.R

@Composable
fun LanguageScreen(
    initialCode: String,
    onDone: (String) -> Unit,
) {
    var selectedCode by remember { mutableStateOf(initialCode) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredLanguages = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            SupportedLanguages.list
        } else {
            SupportedLanguages.list.filter {
                it.nativeName.contains(searchQuery, ignoreCase = true) ||
                        it.englishName.contains(searchQuery, ignoreCase = true) ||
                        it.code.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Top Row: Title + Done Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.language_title),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1B2E),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.language_description),
                    fontSize = 13.sp,
                    color = Color(0xFF6E6B7B),
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = { onDone(selectedCode) },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                modifier = Modifier.height(42.dp),
            ) {
                Text(
                    text = stringResource(R.string.language_done),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = stringResource(R.string.language_search_hint),
                    color = Color(0xFF9E9E9E),
                    fontSize = 14.sp,
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFF4F46E5),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Language List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            items(filteredLanguages) { item ->
                val isSelected = item.code.equals(selectedCode, ignoreCase = true)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Color(0xFF6366F1) else Color(0xFFF3F4F6),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(if (isSelected) Color(0xFFF5F3FF) else Color(0xFFFAFAFA))
                        .clickable { selectedCode = item.code }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = item.flagRes),
                        contentDescription = item.nativeName,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.nativeName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.englishName,
                            fontSize = 13.sp,
                            color = Color(0xFF9CA3AF),
                        )
                    }

                    RadioButton(
                        selected = isSelected,
                        onClick = { selectedCode = item.code },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF6366F1),
                            unselectedColor = Color(0xFFD1D5DB),
                        ),
                    )
                }
            }
        }
    }
}
