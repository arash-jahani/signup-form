package com.arashjahani.sign_up_form.dao.api

import com.arashjahani.sign_up_form.dao.entity.ApiResponse
import com.arashjahani.sign_up_form.dao.entity.SignUpRequestModel
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("/signup")
    suspend fun signUp(@Body requestBody: SignUpRequestModel): Response<ApiResponse>

    @Multipart
    @POST("/attachment_photos")
    suspend fun uploadAttachmentPhoto(
        @Part filePart: MultipartBody.Part
    ): Response<ApiResponse>
}