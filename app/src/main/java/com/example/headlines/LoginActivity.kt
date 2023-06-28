@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.headlines

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.headlines.MainActivity


class LoginActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            startMainPage(this)
            finish()
            return
        }
        var databaseHelper = UserDatabaseHelper(this)
        setContent {

            LoginScreen(this, databaseHelper)
        }
    }

    private fun startMainPage(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        ContextCompat.startActivity(context, intent, null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(context: Context, databaseHelper: UserDatabaseHelper) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    val sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)

    {
        Image(
            painter = painterResource(id = R.drawable.globe),
            contentDescription = "",
            modifier = Modifier
                .height(290.dp)
                .clip(shape = RoundedCornerShape(200.dp))
        )

        Spacer(modifier = Modifier.height(10.dp))


        Row {
            Divider(color = Color(0xFF4A030E), thickness = 2.dp, modifier = Modifier
                .width(110.dp)
                .padding(top = 20.dp, end = 20.dp))
            Text(text = "Login",
                color = Color(0xFF8c182a),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,style = MaterialTheme.typography.headlineMedium)
            Divider(color = Color(0xFF4A030E), thickness = 2.dp, modifier = Modifier
                .width(110.dp)
                .padding(top = 20.dp, start = 20.dp))

        }

        Spacer(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = username,
            onValueChange ={username=it},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "personIcon",
                    tint = Color(0xFF4a030e)
                )
            },
            //placeholder = { Text(text = "Username", color = Color.Black) },
            label = { Text(text = "username", color = Color.White)},
            textStyle = TextStyle(color = Color.White),
            //visualTransformation = PasswordVisualTransformation(),
            //colors = TextFieldDefaults.textFieldColors(Color.Black)

        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange ={password=it},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "lockIcon",
                    tint = Color(0xFF4a030e)
                )
            },
            //placeholder = { Text(text = "password", color = Color.Black) },
            label = { Text(text = "Password", color = Color.White)},
            textStyle = TextStyle(color = Color.White),
            visualTransformation = PasswordVisualTransformation(),
            //colors = TextFieldDefaults.textFieldColors(Color.Black)

        )




        Spacer(modifier = Modifier.height(12.dp))
        if (error.isNotEmpty()) {
            Toast.makeText(context,error,Toast.LENGTH_SHORT).show()
        }

        Button(
            onClick = {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    val user = databaseHelper.getUserByUsername(username)
                    if (user != null && user.password == password) {
                        error = "Successfully log in"
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        context.startActivity(
                            Intent(
                                context,
                                MainActivity::class.java
                            )
                        )
                        (context as? Activity)?.finish()
                    } else {
                        error = "Invalid username OR password"
                    }
                } else {
                    error = "Please fill all fields"
                }
            },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF4a030e)),
            modifier = Modifier
                .width(200.dp)
                .padding(top = 16.dp)
        ){
            Text(text = "Log In", fontWeight = FontWeight.Bold)
        }


        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(80.dp))
            TextButton(onClick = {
                context.startActivity(
                    Intent(
                        context,
                        RegistrationActivity::class.java
                    ))})
            { Text(text = "Sign up",
                color = Color.White
            )}

            Spacer(modifier = Modifier.width(30.dp))

            TextButton(onClick = {

                context.startActivity(
                    Intent(
                        context,
                        AccountRetrievalActivity::class.java
                    ))
            })
            { Text(text = "Forgot password ?",
                color = Color.White
            )}
        }
    }
}


