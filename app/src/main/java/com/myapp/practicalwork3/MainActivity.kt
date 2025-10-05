package com.myapp.practicalwork3

import android.R.attr.text
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.myapp.practicalwork3.ui.theme.PracticalWork3Theme
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "storage")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticalWork3Theme {
                MainScreen()
            }
        }
    }
}

@Composable
@Preview
fun MainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var inputText by remember { mutableStateOf("") }
    val SAVED_TEXT_KEY = stringPreferencesKey("saved_text")
    // Load at startup
    LaunchedEffect(Unit) {
        val prefs = context.dataStore.data.first()
        inputText = prefs[SAVED_TEXT_KEY] ?: ""
    }

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