package com.arashjahani.sign_up_form.dao


import com.arashjahani.sign_up_form.dao.api.ApiService
import com.arashjahani.sign_up_form.dao.entity.ApiResponse
import com.arashjahani.sign_up_form.dao.entity.SignUpRequestModel
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject



class DataRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : DataRepository {
    override suspend fun signUp(requestBody: SignUpRequestModel): Response<ApiResponse> {
        return apiService.signUp(requestBody)
    }

    override suspend fun uploadAttachmentPhoto(filePart: MultipartBody.Part): Response<ApiResponse> {
        return apiService.uploadAttachmentPhoto(filePart)
    }


}