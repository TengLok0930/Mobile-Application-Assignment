package com.example.fundraising.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundraising.R
import com.example.fundraising.ui.theme.FundRaisingTheme

@Composable
fun ProjectPage(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BackButton()

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .background(color = MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    // Profile Image
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        text = "Project 1", // change with variable
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        text = "Organisation 1", // change with variable
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier,
                text = "Progress",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp
            )

            ProgressBar(0.5F)

            Spacer(Modifier.height(25.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier,
                    text = "Contributions:    $ ",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 25.sp
                )

                Text(
                    modifier = Modifier,
                    text = "XXX", // need to change with variable
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 25.sp
                )
            }

            Spacer(Modifier.height(25.dp))

            Text(
                modifier = Modifier,
                text = "Overview",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", //change with variable
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                )
            }

            Spacer(Modifier.height(34.dp))

            Button(
                onClick = {
                    // function to join a project
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Contribute",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 30.sp,
                )
            }

            Spacer(Modifier.height(34.dp))

            HomePageNavigationBar()
        }
    }
}

@Composable
fun ProgressBar(currentProgress: Float) {
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .size(8.dp)
            .clip(RoundedCornerShape(6.dp))
            .border(width = 2.dp, color = MaterialTheme.colorScheme.primary),
        progress = { currentProgress },
        trackColor = Color.Transparent
    )
}

@Composable
fun HomePageNavigationBar(

) {
    Column{
        Divider(color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.chat_40px),
                    contentDescription = "Messages",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.home_40px),
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.account_circle_40px),
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectPagePreview() {
    FundRaisingTheme {
        ProjectPage()
    }
}