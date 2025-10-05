package com.myapp.practicalwork3

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.myapp.practicalwork3.ui.theme.PracticalWork3Theme
import androidx.compose.ui.unit.dp
import android.content.Intent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "storage")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var selectedTheme by remember { mutableStateOf("Default") }
            val isDarkTheme = when (selectedTheme) {
                "Dark theme" -> true
                "Light theme" -> false
                "Default theme" -> isSystemInDarkTheme() // default system theme setting
                else -> isSystemInDarkTheme()
            }
            PracticalWork3Theme(darkTheme = isDarkTheme) {
                MainScreen(
                    selectedTheme = selectedTheme,
                    onThemeChange = { selectedTheme = it }
                )
            }
        }
    }
}

@Composable
@Preview
fun MainScreen(
    selectedTheme: String,
    onThemeChange: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var inputText by remember { mutableStateOf("") }
    val SAVED_TEXT_KEY = stringPreferencesKey("saved_text")
    // load text at startup
    LaunchedEffect(Unit) {
        val prefs = context.dataStore.data.first()
        inputText = prefs[SAVED_TEXT_KEY] ?: ""
    }

    var isThemeDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("1-st Activity", fontSize = 24.sp, modifier=Modifier.padding(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                SecondActivityBtn (
                    onClick = {
                        context.startActivity(Intent(context, SecondActivity::class.java))
                    }
                )
                Box {
                    Button(onClick = { isThemeDropdownExpanded = true }) {
                        Text(selectedTheme)
                    }

                    DropdownMenu(
                        expanded = isThemeDropdownExpanded,
                        onDismissRequest = { isThemeDropdownExpanded = false }
                    ) {
                        listOf("Default theme", "Light theme", "Dark theme").forEach { theme ->
                            DropdownMenuItem(
                                text = { Text(theme) },
                                onClick = {
                                    onThemeChange(theme)
                                    isThemeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField (
                    value = inputText,
                    onValueChange = {
                        inputText = it
                    },
                    label = {
                        Text ("Enter some text")
                    },
                    modifier = Modifier.width(240.dp)
                )
                SaveBtn (
                    onClick = {
                        scope.launch {
                            context.dataStore.edit {
                                prefs -> prefs[SAVED_TEXT_KEY] = inputText
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SecondActivityBtn(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "Go to 2-nd")
    }
}
@Composable
fun SaveBtn(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "Save")
    }
}