package com.example.headlines


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.graphics.Color.parseColor
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.example.Articles
import com.example.headlines.ui.theme.HeadlinesTheme
import androidx.compose.ui.res.colorResource
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {
    val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HeadlinesTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier.background(Color.Black)
                    ) {
                        var isExpanded by remember { mutableStateOf(false) }

                        Row(Modifier.fillMaxWidth()) {

                            ExpandableIcon(
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .align(Alignment.CenterVertically),
                                expanded = isExpanded,
                                onExpand = { isExpanded = !isExpanded },
                                onAboutClicked = { showDialog(this@MainActivity) },
                                onSignOutClicked = {
                                    startActivity(
                                        Intent(this@MainActivity, RegistrationActivity::class.java)
                                    )
                                    finish()
                                }
                            )

                            Text(
                                text = "News Whizz📰",
                                fontSize = 30.sp,
                                modifier = Modifier
                                    .weight(1f)
                                    .align(Alignment.CenterVertically),
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        }

                        MovieList(applicationContext, movieList = mainViewModel.movieListResponse)
                        mainViewModel.getMovieList()
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableIcon(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpand: () -> Unit,
    onAboutClicked: () -> Unit,
    onSignOutClicked: () -> Unit
) {
    Box(modifier = modifier) {
        Icon(
            imageVector = Icons.Rounded.Menu,
            contentDescription = "Expandable Icon",
            tint = Color(0xFF4a030e), // Set the desired color here
            modifier = Modifier
                .size(30.dp)
                .clickable(onClick = onExpand)
        )
        if (expanded) {
            Column(
                modifier = Modifier
                    .padding(top = 48.dp, start = 8.dp)
                    .background(Color.Black)
                    .clip(RoundedCornerShape(8.dp))
                    .width(120.dp)
                    .shadow(elevation = 4.dp)
            ) {
                Text(
                    text = "About",
                    color = Color.White,
                    modifier = Modifier
                        .clickable(onClick = onAboutClicked)
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Sign Out",
                    color = Color.White,
                    modifier = Modifier
                        .clickable(onClick = onSignOutClicked)
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun MovieList(context: Context, movieList: List<Articles>) {
    var selectedIndex by remember { mutableStateOf(-1) }
    LazyColumn {
        itemsIndexed(items = movieList) { index, item ->
            MovieItem(
                context = context,
                movie = item,
                index = index,
                selectedIndex = selectedIndex
            ) { i ->
                selectedIndex = i
            }
        }
    }
}

@Composable
fun MovieItem(
    context: Context,
    movie: Articles,
    index: Int,
    selectedIndex: Int,
    onClick: (Int) -> Unit
) {
    val backgroundColor =
        if (index == selectedIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background

    Card(
        modifier = Modifier
            .padding(20.dp, 8.7.dp)
            .size(380.dp, 113.dp)
            .selectable(true, true, null,
                onClick = {
                    Log.i("test123abc", "MovieItem: $index/n$selectedIndex")
                })
            .clickable { onClick(index) }
            .height(180.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        //Surface(color = Color.LightGray)
           Surface(color = Color(parseColor("#4a030e")))
           {
            Row(
                Modifier
                    .padding(4.dp)
                    .fillMaxSize()
                    .background(Color(parseColor("#4a030e"))),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxHeight()
                        .weight(0.8f)
                        .padding(20.dp)
                        .selectable(true, true, null,
                            onClick = {
                                Log.i("test123abc", "MovieItem: $index/n${movie.description}")
                                context.startActivity(
                                    Intent(context, DisplayNews::class.java)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .putExtra("desk", movie.description.toString())
                                        .putExtra("urlToImage", movie.urlToImage)
                                        .putExtra("title", movie.title)
                                )
                            })
                ) {
                    Text(
                        text = movie.title.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = movie.urlToImage,
                            builder = {
                                scale(Scale.FILL)
                                placeholder(R.drawable.placeholder)
                            }
                        ),
                        contentDescription = movie.description,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(0.3f)
                            .size(150.dp, 80.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}


fun showDialog(context: Context) {
    val dialog = AlertDialog.Builder(context)
        .setTitle("ⓘAbout")
        .setMessage("Version 1.0\n" +
                "We will be releasing more features.\n" +
                "Stay Tuned")
        .setPositiveButton("OK")

        { _, _ ->
            // Handle OK button click if needed
        }
        .create()

    // Set the background color of the dialog to black
    // dialog.window?.setBackgroundDrawable(ColorDrawable(BLACK))

    // Set the title and message colors to white

    dialog.show()
}


