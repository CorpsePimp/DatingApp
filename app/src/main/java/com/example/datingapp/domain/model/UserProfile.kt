package com.example.datingapp.domain.model

/**
 * User profile data model
 */
data class UserProfile(
    val id: String = "1",
    val name: String = "",
    val occupation: String = "",
    val bio: String = "",
    val city: String = "",
    val photoUrls: List<String> = emptyList(),
    val interests: List<String> = emptyList(),
    val profileCompletion: Float = 0f
) {
    /**
     * Calculate profile completion percentage
     */
    fun calculateCompletion(): Float {
        var score = 0f
        if (name.isNotBlank()) score += 0.2f
        if (occupation.isNotBlank()) score += 0.15f
        if (bio.isNotBlank()) score += 0.2f
        if (city.isNotBlank()) score += 0.15f
        if (photoUrls.isNotEmpty()) score += 0.15f
        if (interests.isNotEmpty()) score += 0.15f
        return score.coerceAtMost(1f)
    }
}

/**
 * Available interests for selection (50+ items, sorted by popularity)
 */
object AvailableInterests {
    val all = listOf(
        // Most Popular (Top Tier)
        "Fitness", "Travel", "Movies", "Music", "Photography",
        "Cooking", "Reading", "Gaming", "Sports", "Art",
        "Dancing", "Yoga", "Hiking", "Coffee", "Wine",
        "Fashion", "Dogs", "Cats", "Nature", "Beach",
        // Popular (Mid Tier)
        "Netflix", "Concerts", "Foodie", "Running", "Gym",
        "Swimming", "Cycling", "Meditation", "Volunteering", "Writing",
        "Poetry", "Theater", "Comedy", "Karaoke", "Board Games",
        "Camping", "Fishing", "Golf", "Tennis", "Basketball",
        // Niche (Lower Tier)
        "Anime", "Manga", "K-Pop", "Astrology", "Tattoos",
        "Vintage", "Sneakers", "Crypto", "Investing", "Trading",
        "Nootropics", "Biohacking", "Startups", "Programming", "AI",
        "VR", "Esports", "Cosplay", "LARP", "DnD"
    )
}
