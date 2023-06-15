package com.arashjahani.sign_up_form.dao.api.interceptor


import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.concurrent.TimeUnit

class FakeInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Simulate a 22-second delay
        TimeUnit.SECONDS.sleep(3)

        // Create a mock response
        val responseBody = "{ \"message\": \"Success\" }"
        val mediaType = "application/json".toMediaTypeOrNull()
        return Response.Builder()
            .code(200)
            .protocol(Protocol.HTTP_1_0)
            .message("OK")
            .body(responseBody.toResponseBody(mediaType))
            .request(chain.request())
            .build()
    }
}