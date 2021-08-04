package kr.valor.bal.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.valor.bal.data.remote.YoutubeApiService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class YoutubeApiModule {

    @Singleton
    @Provides
    fun provideYoutubeApiService(): YoutubeApiService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(YoutubeApiService.BASE_URL)
            .build()

        return retrofit.create(YoutubeApiService::class.java)
    }
}