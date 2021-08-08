package kr.valor.bal.data.remote

import kr.valor.bal.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {
    companion object {
        const val API_KEY = BuildConfig.YOUTUBE_API_KEY
        const val BASE_URL = "https://www.googleapis.com"
    }

    @GET("/youtube/v3/search")
    suspend fun requestVideo(
        @Query("key") developerKey: String = API_KEY,
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 10,
        @Query("type") type: String = "video",
        @Query("q") query: String = "klokov squat"
    ): YoutubeVideoContainer
}