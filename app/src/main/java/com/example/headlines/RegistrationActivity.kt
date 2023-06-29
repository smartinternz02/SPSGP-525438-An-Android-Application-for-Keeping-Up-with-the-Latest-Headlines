package com.example.headlines

import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.headlines.ui.theme.HeadlinesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationActivity : ComponentActivity() {
    private lateinit var databaseHelper: UserDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseHelper = UserDatabaseHelper(this)
        setContent {


                    RegistrationScreen(this,databaseHelper)
                }
            }
        }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(context: Context, databaseHelper: UserDatabaseHelper) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    Column(
        Modifier
            .background(Color.Black)
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)

    {




        Image(
            painter = painterResource(id = R.drawable.signupwhiz),
            contentDescription = "",
            modifier = Modifier
                .height(290.dp)
                .clip(shape = RoundedCornerShape(18.dp))
        )
        Spacer(modifier = Modifier.padding(2.dp))

        Text(
            text = "Sign Up",
            color = Color(0xFF8c182a),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp, style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Divider(
            color = Color(0xFF4A030E),
            thickness = 2.dp,
            modifier = Modifier
                .width(250.dp)
            //.padding(top = 20.dp, start = 10.dp, end = 70.dp)
        )

        Spacer(modifier = Modifier.padding(5.dp))

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

        Spacer(modifier = Modifier.height(8.dp))

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


        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange ={email=it},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "emailIcon",
                    tint = Color(0xFF4a030e)
                )
            },
            //placeholder = { Text(text = "password", color = Color.Black) },
            label = { Text(text = "email", color = Color.White)},
            textStyle = TextStyle(color = Color.White),
            //visualTransformation = PasswordVisualTransformation(),
            //colors = TextFieldDefaults.textFieldColors(Color.Black)

        )



        Spacer(modifier = Modifier.height(3.dp))

        if (error.isNotEmpty()) {
            Toast.makeText(context,error,Toast.LENGTH_SHORT).show()
        }

        Button(
            onClick = {
                if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                    if (isPasswordValid(password)) {
                        val user = User(
                            id = null,
                            firstName = username,
                            lastName = null,
                            email = email,
                            password = password
                        )
                        databaseHelper.insertUser(user)
                        error = "User registered successfully"
                        // Start LoginActivity using the current context
                        context.startActivity(
                            Intent(
                                context,
                                LoginActivity::class.java
                            )
                        )
                    } else {
                        error = "Password should contain at least 5 alphabets and 1 number"
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
        ) {
            Text(text = "Register", fontWeight = FontWeight.Bold)
        }



        Row(
            modifier = Modifier.padding(1.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {


            Text(text = "Have an account?",color= Color.White)

            TextButton(onClick = {
                context.startActivity(
                    Intent(
                        context,
                        LoginActivity::class.java
                    )
                )
            }) {
                Text(text = "Log in",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFF4285F4)
                )}

        }
    }
}
private fun startLoginActivity(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    ContextCompat.startActivity(context, intent, null)
}

fun isPasswordValid(password: String): Boolean
{
    val alphabetCount = password.count { it.isLetter() }
    val digitCount = password.count { it.isDigit() }
    return alphabetCount >= 5 && digitCount >= 1
}


