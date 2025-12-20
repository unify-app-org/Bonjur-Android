package com.bonjur.auth.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.bonjur.appfoundation.FeatureStore
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import com.bonjur.auth.presentation.model.ExampleAction
import com.bonjur.auth.presentation.model.ExampleSideEffect
import com.bonjur.auth.presentation.model.ExampleViewState

@Composable
fun ExampleView(
    store: FeatureStore<ExampleViewState, ExampleAction, ExampleSideEffect>
) {
    val state = store.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = state.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Counter: ${state.counter}")

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { store.send(ExampleAction.Increment) }) {
                Text("Increment")
            }

            Button(onClick = { store.send(ExampleAction.Decrement) }) {
                Text("Decrement")
            }
        }
    }
}
