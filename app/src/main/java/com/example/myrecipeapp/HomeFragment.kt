package com.example.myrecipeapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.myrecipeapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var _binding: FragmentHomeBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ImagePagerAdapter
    private lateinit var userProfileViewModel: UserProfileViewModel
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private val DELAY_MS: Long = 3000 // Delay in milliseconds before switching to the next page

    // Explicitly specify the type of pageSwitcher
    private val pageSwitcher: Runnable = object : Runnable {
        override fun run() {
            if (currentPage == adapter.itemCount - 1) {
                currentPage = 0
            } else {
                currentPage += 1
            }
            viewPager.setCurrentItem(currentPage, true)
            handler.postDelayed(this, DELAY_MS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        viewPager = _binding.viewPager

        // Get the TextView by ID
        val usernameTextView: TextView = _binding.username

        // Set the text dynamically based on the current user
        lifecycleScope.launch {
            userProfileViewModel.findUserProfile(AuthenticationManager.currentUser!!.username)
                .observe(viewLifecycleOwner) { userProfile ->
                    usernameTextView.text = userProfile?.name
                }
        }

        // Replace this with your list of image resources
        val imageList: MutableList<Int> = ArrayList()
        imageList.add(R.drawable.food_image1)
        imageList.add(R.drawable.food_image2)
        imageList.add(R.drawable.food_image3)
        imageList.add(R.drawable.food_image4)
        imageList.add(R.drawable.food_image5)
        imageList.add(R.drawable.food_image6)

        adapter = ImagePagerAdapter(requireContext(), imageList)
        viewPager.adapter = adapter
        // Start automatic image sliding
        handler.postDelayed(pageSwitcher, DELAY_MS)

        return _binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Stop the handler when the fragment is destroyed to prevent memory leaks
        handler.removeCallbacks(pageSwitcher)
    }
}