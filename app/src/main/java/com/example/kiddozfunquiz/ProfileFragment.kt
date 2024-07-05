package com.example.kiddozfunquiz

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kiddozfunquiz.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.app.Activity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReferences: DatabaseReference
    private var user: FirebaseUser? = null

    companion object {
        const val EDIT_PROFILE_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        // Initialize the database reference
        databaseReferences = FirebaseDatabase.getInstance().reference

        displayUserData()

        binding.btnLogout.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            intent.putExtra(EditProfileActivity.EXTRA_NAME, binding.tvYourName.text)
            intent.putExtra(EditProfileActivity.EXTRA_EMAIL, binding.tvYourEmail.text)
            intent.putExtra(EditProfileActivity.EXTRA_COUNTRY, binding.tvYourCountry.text)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayUserData() {
        user?.let {
            binding.tvYourEmail.text = it.email ?: ""

            val uid = it.uid
            databaseReferences.child("Users").child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val name = dataSnapshot.child("name").getValue(String::class.java) ?: ""
                        val country = dataSnapshot.child("country").getValue(String::class.java) ?: ""

                        binding.tvYourName.text = name
                        binding.tvYourCountry.text = country
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle possible errors.
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                binding.tvYourName.text = it.getStringExtra(EditProfileActivity.EXTRA_NAME)
                binding.tvYourCountry.text = it.getStringExtra(EditProfileActivity.EXTRA_COUNTRY)
            }
        }
    }
}
