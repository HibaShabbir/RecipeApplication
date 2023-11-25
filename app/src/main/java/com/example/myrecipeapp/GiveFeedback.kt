package com.example.myrecipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myrecipeapp.data.Feedback
import com.example.myrecipeapp.data.FeedbackViewModel
import com.example.myrecipeapp.data.MyDatabase
import com.example.myrecipeapp.databinding.FragmentGiveFeedbackBinding


class GiveFeedback : Fragment() {

    private lateinit var _binding: FragmentGiveFeedbackBinding
    private lateinit var viewModel: FeedbackViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGiveFeedbackBinding.inflate(layoutInflater, container, false)
        val feedbackDao = MyDatabase.getDatabase(requireContext()).feedbackDao()

        viewModel = ViewModelProvider(this).get(FeedbackViewModel::class.java)
        _binding.submitFeedbackButton.setOnClickListener {
            val rating = _binding.ratingBar.rating
            val review = _binding.feedbackEditText.text.toString()

            // Check if the user is authenticated
            if (AuthenticationManager.isAuthenticated) {
                // Retrieve the username from the current user in AuthenticationManager
                val username = AuthenticationManager.currentUser?.username

                // Ensure the username is not null before proceeding
                if (!username.isNullOrBlank()) {
                    val feedback = Feedback(username = username, rating = rating, review = review)

                    // Assuming you have a reference to your database and a FeedbackDao instance
                    viewModel.insertFeedback(feedback)
                    showToast("Thankyou for your Feedback")
                } else {
                    // Handle the case where the username is null or blank
                    showToast("Error: Unable to retrieve username")
                }
            } else {
                // Handle the case where the user is not authenticated
                showToast("Error: User not authenticated")
            }
        }



        return _binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}