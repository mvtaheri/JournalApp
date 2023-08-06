package com.mohammad.journalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.mohammad.journalapp.databinding.ActivityJournalListBinding

class JournalListActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityJournalListBinding

    // Firebase Refrence
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var user:FirebaseUser
    val db = FirebaseFirestore.getInstance()
    lateinit var storageRefrence:StorageReference
    val collectionReference:CollectionReference=db.collection("Jornal")
    lateinit var journalList:MutableList<Journal>
    lateinit var adapter:JournalRecyclerAdapter
    lateinit var noPostTextView:TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_journal_list)
        setSupportActionBar(binding.toolbar)
        firebaseAuth=Firebase.auth
        user=firebaseAuth.currentUser!!

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        //Posts Array List
        journalList = arrayListOf<Journal>()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add -> if (user != null && firebaseAuth!= null){
                val intent=Intent(this,AddJournalActivity::class.java)
                startActivity(intent)
            }
            R.id.action_signout -> {
                if (user != null && firebaseAuth != null){
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
            }
            }
            return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        collectionReference.whereEqualTo("userId",
            user.uid)
            .get().addOnSuccessListener {
                if (!it.isEmpty){
                    for (document in it){
                        var journal =Journal(
                            document.data.get("title").toString(),
                            document.data.get("thoughts").toString(),
                            document.data.get("imageUrl").toString(),
                            document.data.get("userId").toString(),
                            document.data.get("timeAdded") as Timestamp,
                            document.data.get("username").toString()
                        )
                        journalList.add(journal)
                    }
                    adapter =JournalRecyclerAdapter(
                        this,
                        journalList
                    )
                    binding.recyclerView.adapter=adapter
                    adapter.notifyDataSetChanged()
                }else{
                    binding.listNoPosts.visibility= View.VISIBLE
                }
            }.addOnFailureListener {
                Toast.makeText(this,
                "Opp! Some thing went wrong",
                    Toast.LENGTH_LONG
                    ).show()
            }
    }
}