package com.arashjahani.sign_up_form.ui.sign_up


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arashjahani.sign_up_form.dao.DataRepository
import com.arashjahani.sign_up_form.dao.entity.Profile
import com.arashjahani.sign_up_form.dao.entity.RequestStatus
import com.arashjahani.sign_up_form.dao.entity.SignUpRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {

    var status= MutableStateFlow<RequestStatus?>(null)
    var avatarUploadingStatus= MutableStateFlow<RequestStatus?>(null)
    var profile= MutableStateFlow<Profile?>(null)

    fun signUp(signupRequest:SignUpRequestModel) {

        viewModelScope.launch {
            status.emit(RequestStatus.LOADING)

            val response=dataRepository.signUp(signupRequest)

            if(response.isSuccessful){
                status.emit(RequestStatus.SUCCESS)

                signupRequest.apply {
                    profile.emit(Profile(avatarUri,firstName,email,website))
                }


            }else{
                status.emit(RequestStatus.FAILED)
            }
        }
    }

    fun uploadImage(filepart: MultipartBody.Part) {

        viewModelScope.launch {
            avatarUploadingStatus.emit(RequestStatus.LOADING)

            val response=dataRepository.uploadAttachmentPhoto(filepart)

            if(response.isSuccessful){
                avatarUploadingStatus.emit(RequestStatus.SUCCESS)
            }else{
                avatarUploadingStatus.emit(RequestStatus.FAILED)
            }
        }
    }

}