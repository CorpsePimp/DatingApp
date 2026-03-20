package com.example.datingapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datingapp.data.repository.ProfileRepository
import com.example.datingapp.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Wizard steps enumeration
 */
enum class WizardStep(val index: Int) {
    BASIC_INFO(0),
    PHOTOS(1),
    INTERESTS(2),
    PREVIEW(3)
}

/**
 * Sealed class for UI events
 */
sealed class ProfileEvent {
    data object ProfileSaved : ProfileEvent()
    data class Error(val message: String) : ProfileEvent()
}

/**
 * Draft profile state for the wizard
 */
data class DraftProfile(
    val name: String = "",
    val occupation: String = "",
    val bio: String = "",
    val city: String = "",
    val photoUrls: List<String> = emptyList(),
    val selectedInterests: List<String> = emptyList()
) {
    // Step 1 validation: Name, City, Bio required
    val isStep1Valid: Boolean
        get() = name.isNotBlank() && city.isNotBlank() && bio.isNotBlank()

    // Step 2 validation: At least 1 photo
    val isStep2Valid: Boolean
        get() = photoUrls.isNotEmpty()

    // Step 3 validation: At least 1 interest
    val isStep3Valid: Boolean
        get() = selectedInterests.isNotEmpty()
}

/**
 * UI State for Edit Profile Wizard
 */
data class EditProfileWizardState(
    val currentStep: WizardStep = WizardStep.BASIC_INFO,
    val draft: DraftProfile = DraftProfile(),
    val interestSearchQuery: String = "",
    val isSaving: Boolean = false,
    val previewPhotoIndex: Int = 0
) {
    val canProceedFromCurrentStep: Boolean
        get() = when (currentStep) {
            WizardStep.BASIC_INFO -> draft.isStep1Valid
            WizardStep.PHOTOS -> draft.isStep2Valid
            WizardStep.INTERESTS -> draft.isStep3Valid
            WizardStep.PREVIEW -> true
        }

    val isFirstStep: Boolean
        get() = currentStep == WizardStep.BASIC_INFO

    val isLastStep: Boolean
        get() = currentStep == WizardStep.PREVIEW

    val progress: Float
        get() = (currentStep.index + 1) / 4f
}

/**
 * ViewModel for Profile and Edit Profile Wizard
 */
class ProfileViewModel : ViewModel() {

    // Profile data from repository (for ProfileScreen)
    val profileState: StateFlow<UserProfile> = ProfileRepository.profileFlow

    // Wizard state
    private val _wizardState = MutableStateFlow(EditProfileWizardState())
    val wizardState: StateFlow<EditProfileWizardState> = _wizardState.asStateFlow()

    // One-time events (snackbar, navigation)
    private val _events = MutableSharedFlow<ProfileEvent>()
    val events: SharedFlow<ProfileEvent> = _events.asSharedFlow()

    // Flag to show success snackbar on ProfileScreen
    private val _showSuccessSnackbar = MutableStateFlow(false)
    val showSuccessSnackbar: StateFlow<Boolean> = _showSuccessSnackbar.asStateFlow()

    /**
     * Initialize wizard state from current profile
     */
    fun initWizard() {
        val currentProfile = ProfileRepository.currentProfile
        _wizardState.value = EditProfileWizardState(
            currentStep = WizardStep.BASIC_INFO,
            draft = DraftProfile(
                name = currentProfile.name,
                occupation = currentProfile.occupation,
                bio = currentProfile.bio,
                city = currentProfile.city,
                photoUrls = currentProfile.photoUrls,
                selectedInterests = currentProfile.interests
            )
        )
    }

    /**
     * Reset wizard to step 1 (keeping draft data)
     */
    fun goToStep1() {
        _wizardState.update { it.copy(currentStep = WizardStep.BASIC_INFO) }
    }

    /**
     * Navigate to next step
     */
    fun nextStep() {
        _wizardState.update { state ->
            val nextIndex = (state.currentStep.index + 1).coerceAtMost(3)
            val nextStep = WizardStep.entries.find { it.index == nextIndex } ?: WizardStep.PREVIEW
            state.copy(currentStep = nextStep)
        }
    }

    /**
     * Navigate to previous step
     */
    fun previousStep() {
        _wizardState.update { state ->
            val prevIndex = (state.currentStep.index - 1).coerceAtLeast(0)
            val prevStep = WizardStep.entries.find { it.index == prevIndex } ?: WizardStep.BASIC_INFO
            state.copy(currentStep = prevStep)
        }
    }

    // ==================== Step 1: Basic Info ====================

    fun updateName(name: String) {
        _wizardState.update { it.copy(draft = it.draft.copy(name = name)) }
    }

    fun updateOccupation(occupation: String) {
        _wizardState.update { it.copy(draft = it.draft.copy(occupation = occupation)) }
    }

    fun updateBio(bio: String) {
        _wizardState.update { it.copy(draft = it.draft.copy(bio = bio)) }
    }

    fun updateCity(city: String) {
        _wizardState.update { it.copy(draft = it.draft.copy(city = city)) }
    }

    // ==================== Step 2: Photos ====================

    fun addPhoto() {
        viewModelScope.launch {
            val picsumUrl = "https://picsum.photos/seed/${System.currentTimeMillis()}/400/600"
            _wizardState.update { state ->
                val currentPhotos = state.draft.photoUrls
                if (currentPhotos.size < 6) {
                    state.copy(draft = state.draft.copy(photoUrls = currentPhotos + picsumUrl))
                } else {
                    state
                }
            }
        }
    }

    fun removePhoto(photoUrl: String) {
        _wizardState.update { state ->
            val newPhotos = state.draft.photoUrls - photoUrl
            state.copy(
                draft = state.draft.copy(photoUrls = newPhotos),
                previewPhotoIndex = state.previewPhotoIndex.coerceAtMost((newPhotos.size - 1).coerceAtLeast(0))
            )
        }
    }

    fun replacePhoto(oldUrl: String, newUrl: String) {
        _wizardState.update { state ->
            val newPhotos = state.draft.photoUrls.map { if (it == oldUrl) newUrl else it }
            state.copy(draft = state.draft.copy(photoUrls = newPhotos))
        }
    }

    fun setPreviewPhotoIndex(index: Int) {
        _wizardState.update { it.copy(previewPhotoIndex = index) }
    }

    // ==================== Step 3: Interests ====================

    fun updateInterestSearch(query: String) {
        _wizardState.update { it.copy(interestSearchQuery = query) }
    }

    fun toggleInterest(interest: String) {
        _wizardState.update { state ->
            val current = state.draft.selectedInterests
            val updated = if (interest in current) {
                current - interest
            } else {
                current + interest
            }
            state.copy(draft = state.draft.copy(selectedInterests = updated))
        }
    }

    // ==================== Step 4: Save ====================

    fun saveProfile(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _wizardState.update { it.copy(isSaving = true) }

            try {
                val draft = _wizardState.value.draft
                val updatedProfile = UserProfile(
                    id = ProfileRepository.currentProfile.id,
                    name = draft.name,
                    occupation = draft.occupation,
                    bio = draft.bio,
                    city = draft.city,
                    photoUrls = draft.photoUrls,
                    interests = draft.selectedInterests
                )

                val success = ProfileRepository.updateProfile(updatedProfile)

                if (success) {
                    _showSuccessSnackbar.value = true
                    _events.emit(ProfileEvent.ProfileSaved)
                    onSuccess()
                }
            } catch (e: Exception) {
                _events.emit(ProfileEvent.Error(e.message ?: "Unknown error"))
            } finally {
                _wizardState.update { it.copy(isSaving = false) }
            }
        }
    }

    /**
     * Clear success snackbar flag
     */
    fun clearSuccessSnackbar() {
        _showSuccessSnackbar.value = false
    }
}
