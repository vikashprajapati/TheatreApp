# Syncr Theatre App
An Android app for enjoying synchronized videos with a group of friends and chatting with them while watching videos. Apart from using this app 
with group of your friends, you can use your multiple smartphone in your home to create theatre like experience by placing your devices in
multiple corner of your room for playing audio and enjoying video on one of the screen. Also you might have faced low volume situation when watching
movie while being on a trip with group of your friends, for that situation this app will solve that issue.

## Features 
- Synchronized video on multiple devices
- Synchronized video playback events to multiple devices 
- Realtime chat with group of friends

## Technology used
App built with Kotlin and Android Jetpack Components.
- Single activity with multiple fragments is used with MVVM architecture. 
- Socket.io for sharing video synchronization, playback & message events.
- Jetpack libraries such as ViewModel, DataBinding, Navigation Component, LiveData.
- A server with ExpressJs is also used for communication the events with multiple devices.

## What's Next
- Player control events will be broadcasted to let other people in the room know who sent which event.
- Streaming screen UI will be updated to include information regarding the current room.
- In upcoming build youtube player will be included with search functionality.
