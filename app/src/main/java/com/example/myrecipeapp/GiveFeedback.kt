package com.example.myrecipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myrecipeapp.databinding.FragmentGiveFeedbackBinding


class GiveFeedback : Fragment() {

    private lateinit var give_feedback_binding: FragmentGiveFeedbackBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        give_feedback_binding = FragmentGiveFeedbackBinding.inflate(layoutInflater, container, false)
        return give_feedback_binding.root
    }

}