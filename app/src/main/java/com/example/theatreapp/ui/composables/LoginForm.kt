package com.example.theatreapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theatreapp.R

@Preview
@Composable
fun LoginForm(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Hello!",
            fontSize = 42.sp,
            color = colorResource(id = R.color.secondary_color),
            fontWeight = FontWeight.ExtraBold,
        )

        Row(
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text(
                text = "Welcome to",
                fontSize = 36.sp,
                color = colorResource(id = R.color.tertiary_color),
                fontWeight = FontWeight.ExtraBold,
            )

            Text(
                text = " Syncr",
                fontSize = 36.sp,
                color = colorResource(id = R.color.primary_color),
                fontWeight = FontWeight.ExtraBold,
            )
        }


        Card(
            backgroundColor = colorResource(id = R.color.off_white),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .size(300.dp),
            elevation = 16.dp
        ){
            Column(
                modifier = Modifier.padding(horizontal = 36.dp, vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val textFieldStyles = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.White,
                    cursorColor = colorResource(id = R.color.secondary_color),
                    focusedLabelColor = colorResource(id = R.color.secondary_color)
                )

                /*var username by remember { mutableStateOf("") }*/
                TextField(
                    value = "",
                    onValueChange = {},
                    label = {
                        Text(text = "UserName")
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldStyles,
                    modifier = Modifier.padding(bottom = 24.dp),
                    trailingIcon = {

                    }
                )

                TextField(value = "",
                    onValueChange = {},
                    label = {
                        Text(text = "RoomName")
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldStyles,
                    modifier = Modifier.padding(bottom = 48.dp)
                )

                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.primary_color)
                    ),
                    onClick = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.join_room_button),
                        color = colorResource(id = R.color.white)
                    )
                }
            }
        }
    }
}