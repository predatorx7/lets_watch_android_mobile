package com.magnificsoftware.letswatch.api

import androidx.lifecycle.LiveData
import com.magnificsoftware.letswatch.data_class.plain.stub_data.OMDbSearchResponse
import com.magnificsoftware.letswatch.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * OMDb REST API access points
 */
interface OMDbService {
    companion object {
        // Warning: CLEARTEXT is disabled by default from android 9. Try using Https versions of api before enabling it,
        const val baseUrl = "https://www.omdbapi.com/"
        private const val apiKey: String = "c4033450"
        private const val i: String = "tt3896198"
        private const val s: String = "Movies"
    }

    @GET(".")
    fun getData(
        @Query("i") i: String = OMDbService.i,
        @Query("apikey") apiKey: String = OMDbService.apiKey,
        @Query("s") s: String = OMDbService.s
    ): LiveData<ApiResponse<OMDbSearchResponse>>
}