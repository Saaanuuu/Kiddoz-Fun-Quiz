package com.example.kiddozfunquiz


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kiddozfunquiz.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.app.Activity
import android.content.Intent

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private lateinit var databaseReferences: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReferences = FirebaseDatabase.getInstance().getReference("Users")

        binding.tvEditName.hint = intent.getStringExtra(EXTRA_NAME)
        binding.tvYourEmail.text = intent.getStringExtra(EXTRA_EMAIL)
        binding.tvEditCountry.hint = intent.getStringExtra(EXTRA_COUNTRY)

        binding.btnSaveProfile.setOnClickListener {
            val name = binding.tvEditName.text.toString()
            val country = binding.tvEditCountry.text.toString()

            if (name.isNotEmpty() && country.isNotEmpty()) {
                val user = User(name, country)
                if (uid != null) {
                    saveUserData(uid, user)
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(uid: String, user: User) {
        databaseReferences.child(uid).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                val resultIntent = Intent().apply {
                    putExtra(EXTRA_NAME, user.name)
                    putExtra(EXTRA_COUNTRY, user.country)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_COUNTRY = "extra_country"
    }
}