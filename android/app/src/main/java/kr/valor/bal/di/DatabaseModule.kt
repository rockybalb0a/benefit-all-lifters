package kr.valor.bal.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.valor.bal.data.AppDatabase
import kr.valor.bal.data.WorkoutDao
import kr.valor.bal.utilities.DATABASE_NAME
import kr.valor.bal.utilities.randomGenerator
import javax.inject.Provider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

//    @Singleton
//    @Provides
//    fun provideAppDatabase(@ApplicationContext appContext: Context, provider: Provider<WorkoutDao>): AppDatabase {
//        return Room.databaseBuilder(
//            appContext,
//            AppDatabase::class.java,
//            DATABASE_NAME
//        )
//            .addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    CoroutineScope(Dispatchers.IO).launch {
//                        randomGenerator().forEach {
//                            provider.get().insertWorkoutOverview(it)
//                        }
//                    }
//                }
//            })
//            .build()
//    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .build()
    }

    @Provides
    fun provideWorkoutDao(appDatabase: AppDatabase): WorkoutDao {
        return appDatabase.workoutDao()
    }
}
