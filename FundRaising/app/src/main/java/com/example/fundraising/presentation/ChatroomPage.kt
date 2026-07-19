package com.example.fundraising.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundraising.R
import com.example.fundraising.ui.theme.FundRaisingTheme
import kotlinx.coroutines.launch

@Composable
fun ChatroomPage(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatroomData("Hi, i want to fund this project", true),
            ChatroomData("Hi, how much you want to fund?", false)
        )
    }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BackButton()

            Text(
                modifier = Modifier
                    .padding(top = 15.dp, start = 40.dp, end = 40.dp),
                text = "Project 1",
                textAlign = TextAlign.Center,
                fontSize = 30.sp
            )

            SearchButton()
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(12.dp)
        ) {
            items(messages) { msg ->
                MessageBubble(msg)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {},
                modifier = Modifier
                    .padding(end = 6.dp),
                colors = IconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_24px),
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message") }
            )

            IconButton(onClick = {
                if (input.isNotBlank()) {
                    messages.add(ChatroomData(input, true))
                    input = ""
                    scope.launch {
                        listState.animateScrollToItem(messages.lastIndex)
                        }
                    }
                },
                modifier = Modifier
                    .padding(start = 6.dp),
                colors = IconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.send_24px),
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Composable
fun MessageBubble(msg: ChatroomData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isMe) Arrangement.End else Arrangement.Start
    ) {
        if (msg.isMe) {
            ChatBox(msg)
            UserIcon(msg)
        } else {
            UserIcon(msg)
            ChatBox(msg)
        }
    }
}

@Composable
fun UserIcon(msg: ChatroomData) {
    Box(
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 4.dp)
            .background(
                color = if (msg.isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
            .widthIn(max = 250.dp)
    ) {

    }
}

@Composable
fun ChatBox(msg: ChatroomData) {
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clip(shape = RoundedCornerShape(6.dp))
            .border(width = 2.dp, color = if (msg.isMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
            .padding(12.dp)
            .widthIn(max = 250.dp)
    ) {
        Column(
            modifier = Modifier
        ) {
            Text(text = msg.text)

            Text(text = "19/7/2026", fontSize = 10.sp)
        }
    }
}

@Composable
fun SearchButton() {
    IconButton(onClick = {},
        modifier = Modifier
            .padding(top = 10.dp),
        colors = IconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primary
            )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.search_24px),
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatroomPagePreview() {
    FundRaisingTheme {
        ChatroomPage()
    }
}