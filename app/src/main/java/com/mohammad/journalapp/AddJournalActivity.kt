package com.mohammad.journalapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mohammad.journalapp.databinding.ActivityAddJournalBinding
import java.util.Date


class AddJournalActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAddJournalBinding
    var currentUserId: String = ""
    var currentUserName: String = ""

    lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser

    //Fierbase FierStore
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var storageReference: StorageReference
    val collectionReference: CollectionReference = db.collection("Journal")
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_journal)
        storageReference = FirebaseStorage.getInstance().getReference()
        auth = Firebase.auth
        binding.apply {
            postProgressBar.visibility = View.INVISIBLE
            if (JournalUser.instance != null) {
                currentUserId = auth.currentUser?.uid.toString()
                currentUserName = auth.currentUser?.displayName.toString()
                postUsernameTextview.text = currentUserName
            }
            postCameraButton.setOnClickListener(){
                var i =Intent(Intent.ACTION_GET_CONTENT)
                i.setType("image/*")
                startActivityForResult(i,1)
            }
            postSaveJournalButton.setOnClickListener() {
                savejournal()
            }
        }
    }

    private fun savejournal() {
        var title: String = binding.postTitleEt.text.toString().trim()
        var thoughts: String = binding.postDescriptionEt.text.toString().trim()
        binding.postProgressBar.visibility = View.VISIBLE
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thoughts) && imageUri != null) {
            //savinag the path of images in storage
            val filePath: StorageReference = storageReference.child("journal_images")
                .child("my_image" + Timestamp.now().seconds)
            //uploading the images
            filePath.putFile(imageUri).addOnSuccessListener {
                filePath.downloadUrl.addOnSuccessListener {
                    var imageUri = it.toString()
                    val timestamp: Timestamp = Timestamp(Date())
                    var journal: Journal = Journal(
                        title,
                        thoughts,
                        imageUri,
                        currentUserId,
                        timestamp,
                        currentUserName
                    )
                    collectionReference.add(journal)
                        .addOnSuccessListener {
                            binding.postProgressBar.visibility = View.INVISIBLE
                            var i: Intent = Intent(this, JournalListActivity::class.java)
                            startActivity(i)
                            finish()
                        }.addOnFailureListener {
                            binding.postProgressBar.visibility = View.INVISIBLE
                        }
                }
            }
        } else {
            binding.postProgressBar.visibility = View.INVISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1 && resultCode == RESULT_OK){
            if (data != null){
                imageUri = data.data!!
                binding.postImageView.setImageURI(imageUri)

            }
        }
    }

    override fun onStart() {
        super.onStart()
        user=auth.currentUser!!
    }
}