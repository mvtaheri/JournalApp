package com.mohammad.journalapp

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mohammad.journalapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        auth=Firebase.auth
        binding.createAcctBTN.setOnClickListener(){
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.emailSignInButton.setOnClickListener(){
            loginWithEmailPassword(
            binding.email.text.toString().trim(),
            binding.password.text.toString().trim()
            )

        }
    }
    private fun loginWithEmailPassword(email:String,password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){  task->
                if (task.isSuccessful) {
                    var journal:JournalUser = JournalUser.instance!!
                    journal.userId=auth.currentUser?.uid
                    journal.userName=auth.currentUser?.displayName.toString()
                    goToJournalList()
                }else{
                    Toast.makeText(this,"Authentication Fail",Toast.LENGTH_LONG)
                        .show()
                }
            }
    }
    override fun onStart() {
        super.onStart()
        val currentUser =auth.currentUser
        if (currentUser != null)
            goToJournalList()
    }
    private fun goToJournalList(){
        var intent=Intent(this,JournalListActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add -> if (auth.currentUser != null && auth!= null){
                val intent=Intent(this,AddJournalActivity::class.java)
                startActivity(intent)
            }
            R.id.action_signout -> {
                if (auth.currentUser != null && auth != null){
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}