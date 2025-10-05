package com.myapp.practicalwork3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.content.Intent;
import android.widget.Toast
import com.myapp.practicalwork3.ui.theme.PracticalWork3Theme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PracticalWork3Theme {
                SecondScreen()
            }
        }
    }
}

@Composable
@Preview
fun SecondScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var readText by remember { mutableStateOf("") }
    val SAVED_TEXT_KEY = stringPreferencesKey("saved_text")

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("2-nd Activity", fontSize = 24.sp, modifier=Modifier.padding(16.dp))
            BackBtn (
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField (
                    value = readText,
                    onValueChange = {
                        readText = it
                    },
                    label = {
                        Text ("Enter some text")
                    },
                    modifier = Modifier.width(240.dp)
                )
                ReadBtn (
                    onClick = {
                        scope.launch {
                            val prefs = context.dataStore.data.first()
                            val value = prefs[SAVED_TEXT_KEY]

                            if (value != null) {
                                readText = value
                            } else {
                                Toast.makeText(context, "Nothing found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun BackBtn(onClick: () -> Unit) {
    Button(
        onClick = onClick,
    ) {
        Text(text = "Back")
    }
}

@Composable
fun ReadBtn(onClick: () -> Unit) {
    Button(
        onClick = onClick,
    ) {
        Text(text = "Read storage")
    }
}
