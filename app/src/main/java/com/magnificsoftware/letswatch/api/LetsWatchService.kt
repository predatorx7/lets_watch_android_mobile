package com.magnificsoftware.letswatch.api

import androidx.lifecycle.LiveData
import com.magnificsoftware.letswatch.data_class.plain.auth.userdata.UserProfileResponse
import com.magnificsoftware.letswatch.data_class.plain.banners.ShowBannersDataResponse
import com.magnificsoftware.letswatch.data_class.plain.category.ShowCategoriesDataResponse
import com.magnificsoftware.letswatch.data_class.plain.search.SearchDataResponse
import com.magnificsoftware.letswatch.data_class.plain.show_details.MovieShowDetailsResponse
import com.magnificsoftware.letswatch.data_class.plain.show_details.SeriesShowDetailsResponse
import com.magnificsoftware.letswatch.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * REST API access points
 */
interface LetsWatchService {
    @GET("home/category_video_show_details")
    fun getShowsPerCategories(): LiveData<ApiResponse<ShowCategoriesDataResponse>>

    @GET("home/banners")
    fun getShowBanners(): LiveData<ApiResponse<ShowBannersDataResponse>>

    @GET("home/video_show_details/{showId}?video_type=movie?user_id=")
    fun getMovieShowDetails(@Path("showId") showId: Int): LiveData<ApiResponse<MovieShowDetailsResponse>>

    @GET("tv_show/tv_show/{showId}")
    fun getSeriesShowDetails(
        @Path("showId") showId: Int,
        @Query("season_no") seasonNumber: Int = 1,
        @Query("episode_no") episodeNumber: Int? = null,
        @Query("user_id") userId: Int? = null,
    ): LiveData<ApiResponse<SeriesShowDetailsResponse>>

    @GET("home/search")
    fun searchShow(@Query("search") query: String): LiveData<ApiResponse<SearchDataResponse>>

    @GET("user_detail/user_profile")
    fun getUser(@Header("Authorization") login: String): LiveData<ApiResponse<UserProfileResponse>>
}