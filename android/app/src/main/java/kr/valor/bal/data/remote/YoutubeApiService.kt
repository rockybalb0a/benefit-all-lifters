package kr.valor.bal.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {
    companion object {
        const val API_KEY = "AIzaSyC_7_028fd5AD_ITBELysXgLuFR2zbBUSo"
        const val BASE_URL = "https://www.googleapis.com"
    }

    @GET("/youtube/v3/search")
    suspend fun requestVideo(
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 50,
        @Query("q") query: String = "klokov squat",
        @Query("type") type: String = "video",
        @Query("key") developerKey: String = API_KEY
    ): YoutubeVideoContainer
}