package com.example.theatreapp.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vikash.syncr_core.viewmodels.HomeFragmentViewModel
import me.nikhilchaudhari.library.neumorphic
import me.nikhilchaudhari.library.shapes.Punched

@Composable
fun LoginForm(){
    Card(
        backgroundColor = Color(236, 234, 235),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(16.dp)
            .size(300.dp, 100.dp)
            .neumorphic(
                neuShape =
                // Punched shape
                Punched.Rounded(radius = 8.dp)
            )
    ){
        Text(
            text = "Hello this is a card"
        )
    }
}
