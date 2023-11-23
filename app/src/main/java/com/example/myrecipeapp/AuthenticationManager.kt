package com.example.myrecipeapp

import com.example.myrecipeapp.data.UserProfile

object AuthenticationManager {
    var isAuthenticated: Boolean = false
    var currentUser: UserProfile? = null
}
