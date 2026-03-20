package com.example.datingapp.data.repository

import com.example.datingapp.domain.model.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton repository for user profile data
 * Simulates a backend with network latency
 */
object ProfileRepository {

    // Initial mock data
    private val initialProfile = UserProfile(
        id = "1",
        name = "Stefan",
        occupation = "1C Developer",
        bio = "Learning Go, love strength training",
        city = "Voronezh",
        photoUrls = listOf(
            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&h=400&fit=crop",
            "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&h=400&fit=crop"
        ),
        interests = listOf("Fitness", "Go")
    ).let { it.copy(profileCompletion = it.calculateCompletion()) }

    private val _profileFlow = MutableStateFlow(initialProfile)
    val profileFlow: StateFlow<UserProfile> = _profileFlow.asStateFlow()

    /**
     * Current profile value
     */
    val currentProfile: UserProfile
        get() = _profileFlow.value

    /**
     * Update profile with simulated network delay
     * @param newProfile Updated profile data
     * @return true if successful
     */
    suspend fun updateProfile(newProfile: UserProfile): Boolean {
        // Simulate network latency (1.5 seconds)
        delay(1500L)

        // Update profile with recalculated completion
        val updatedProfile = newProfile.copy(
            profileCompletion = newProfile.calculateCompletion()
        )
        _profileFlow.value = updatedProfile

        return true
    }

    /**
     * Add a photo to profile
     */
    suspend fun addPhoto(photoUrl: String): Boolean {
        delay(500L)
        val current = _profileFlow.value
        val newPhotos = current.photoUrls + photoUrl
        _profileFlow.value = current.copy(
            photoUrls = newPhotos,
            profileCompletion = current.copy(photoUrls = newPhotos).calculateCompletion()
        )
        return true
    }

    /**
     * Remove a photo from profile
     */
    suspend fun removePhoto(photoUrl: String): Boolean {
        delay(300L)
        val current = _profileFlow.value
        val newPhotos = current.photoUrls - photoUrl
        _profileFlow.value = current.copy(
            photoUrls = newPhotos,
            profileCompletion = current.copy(photoUrls = newPhotos).calculateCompletion()
        )
        return true
    }
}
