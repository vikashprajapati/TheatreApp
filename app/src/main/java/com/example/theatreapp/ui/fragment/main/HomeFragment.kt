package com.example.theatreapp.ui.fragment.main

import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.theatreapp.R
import com.example.theatreapp.databinding.FragmentHomeBinding
import com.example.theatreapp.ui.fragment.BaseFragment
import com.vikash.syncr_core.data.models.response.joinroomresponse.JoinedRoomResponse
import com.vikash.syncr_core.utils.Event
import com.vikash.syncr_core.viewmodels.HomeFragmentViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeFragmentViewModel>(){
    private val TAG = HomeFragment::class.java.canonicalName

    override fun getViewBinding(): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater).apply {
            composeContainer.setContent {
                LoginForm()
            }
        }

    override fun initViewModel(): HomeFragmentViewModel =
        ViewModelProvider(this)[HomeFragmentViewModel::class.java]

    private fun navigateToStreamingRoom(roomDetails: JoinedRoomResponse){
        val bundle = bundleOf("videoUrl" to roomDetails.room.currentVideoUrl)
        findNavController().navigate(R.id.action_homeFragment_to_roomFrament, bundle)
    }

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
                    .width(300.dp),
                elevation = 16.dp
            ){
                Column(
                    modifier = Modifier.padding(horizontal = 36.dp, vertical = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val textFieldColors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        backgroundColor = Color.White,
                        cursorColor = colorResource(id = R.color.secondary_color),
                        focusedLabelColor = colorResource(id = R.color.secondary_color)
                    )
                    UserField(textFieldColors)
                    RoomField(textFieldColors)
                    SwitchGroup()
                    CustomButton(stringResource(id = R.string.join_room_button))
                }
            }

            viewModelObservers()
        }
    }

    @Composable
    fun RoomField(textFieldColors: TextFieldColors){
        // var roomName by remember { mutableStateOf("") }
        val roomName by viewModel.room.observeAsState("")
        TextField(
            value = roomName,
            onValueChange = {viewModel.room.value = it},
            label = {
                Text(text = "RoomName")
            },
            shape = RoundedCornerShape(12.dp),
            colors = textFieldColors,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }


    @Composable
    fun UserField(textFieldColors: TextFieldColors){
        //                var userName by remember { mutableStateOf("") }
        val userName by viewModel.user.observeAsState("")
        TextField(
            value = userName,
            onValueChange = {viewModel.user.value = it},
            label = {
                Text(text = "UserName")
            },
            shape = RoundedCornerShape(12.dp),
            colors = textFieldColors,
            modifier = Modifier.padding(bottom = 24.dp),
            trailingIcon = {
                painterResource(id = R.drawable.ic_person)
            }
        )
    }

    @Composable
    fun SwitchGroup(){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween)  {
            Text(text = "LAN Mode")
            var checkedState by remember { mutableStateOf(false) }
            Switch(
                checked = checkedState,
                onCheckedChange = {checkedState = it},
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(id = R.color.primary_color)
                )
            )
        }
    }

    @Composable
    fun viewModelObservers(){
        val connectionState by viewModel.connectionState.observeAsState()
        var message = connectionState?.getContentIfNotHandledOrReturnNull()?:""
        if(message.isNotBlank()){
            shortToast(message)
//            MessageSnackBar(message)
            return
        }

        val joinRoomState by viewModel.joinRoomState.observeAsState()
        val roomDetails = joinRoomState?.getContentIfNotHandledOrReturnNull()
        if(roomDetails != null){
            navigateToStreamingRoom(roomDetails)
            return
        }

        val isInvalidInput by viewModel.invalidInput.observeAsState()
        message = isInvalidInput?.getContentIfNotHandledOrReturnNull()?:""
        if(!message.isBlank()){
            shortToast(message)
//            MessageSnackBar(message = message)
        }

        val visibility by viewModel.loading.observeAsState(View.GONE)
        if(visibility ==  View.VISIBLE){
            Dialog(onDismissRequest = {}, ) {
                CircularProgressIndicator(
                    strokeWidth = 4.dp,
                    color = colorResource(id = R.color.primary_color),
                    modifier = Modifier
                        .size(48.dp)

                )
            }
        }
    }


    @Composable
    fun CustomButton(text : String){
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.primary_color)
            ),
            onClick = {
                viewModel.validateInput()
            },
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = text,
                color = colorResource(id = R.color.white)
            )
        }
    }


    @Composable
    fun MessageSnackBar(message : String){
        Row(modifier = Modifier.height(24.dp)) {
            Snackbar(
                backgroundColor = colorResource(id = R.color.accent_color),
                modifier = Modifier
                    .padding(8.dp),
                elevation = 8.dp,
                action = {
                    Text(
                        color = colorResource(id = R.color.secondary_color),
                        text = "Dismiss",
                        modifier = Modifier
                            .clickable(onClick = {
                                viewModel.invalidInput.value = Event("")
                            })
                    )
                }
            ) {
                Text(text = message, color = colorResource(id = R.color.black))
            }
        }
    }
}

