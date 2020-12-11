package es.i12capea.marvel.presentation.di

import android.app.Application
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import es.i12capea.marvel.common.Constants
import es.i12capea.marvel.common.md5
import es.i12capea.marvel.common.toHex
import es.i12capea.marvel.data.api.CharacterApi
import es.i12capea.marvel.data.local.MarvelDatabase
import es.i12capea.marvel.data.local.dao.CharacterDao
import es.i12capea.marvel.data.local.dao.CharacterShortDao
import es.i12capea.marvel.data.local.dao.QueryResultsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {


    @Singleton
    @Provides
    fun provideCoroutineDispatcher() : CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideGsonBuilder() : Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor {

            val timestamp = System.currentTimeMillis().toString()

            val hash = (timestamp + "0bce59096c0fc4005a1d4b087a883c6776962cda" + "a4fb3476130a6fffb81b2f43286e5b58").md5().toHex()

            val url: HttpUrl = it.request().url().newBuilder()
                .addQueryParameter("ts", timestamp)
                .addQueryParameter("apikey", "a4fb3476130a6fffb81b2f43286e5b58")
                .addQueryParameter("hash", hash)
                .build()

            val newRequest = it.request().newBuilder().apply {
                addHeader("Content-type", "application/json")
                addHeader("Accept", "application/json")
            }
                .url(url)
                .build()

            val response = it.proceed(newRequest)
            response
        }
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(interceptor: Interceptor) : OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofitBuilder(okHttpClient: OkHttpClient, gson : Gson) : Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
        //.callbackExecutor(Runnable::run)
    }

    @Provides
    @Singleton
    fun provideCharacterApi(retrofit: Retrofit.Builder) : CharacterApi{
        return retrofit.build()
            .create(CharacterApi::class.java)
    }


    @Singleton
    @Provides
    fun provideGlideInstance(application: Application, requestOptions: RequestOptions): RequestManager {
        return Glide.with(application)
    }

    @Singleton
    @Provides
    fun provideAppDb(app: Application): MarvelDatabase {
        return Room
                .databaseBuilder(app, MarvelDatabase::class.java, Constants.DB_NAME)
                .fallbackToDestructiveMigration() // get correct db version if schema changed
                .build()
    }

    @Singleton
    @Provides
    fun provideQueryResultsDao(db: MarvelDatabase): QueryResultsDao {
        return db.getQueryResultsDao()
    }

    @Singleton
    @Provides
    fun provideCharacterShortDao(db: MarvelDatabase): CharacterShortDao {
        return db.getCharacterShortDao()
    }

    @Singleton
    @Provides
    fun provideCharacterDao(db: MarvelDatabase): CharacterDao {
        return db.getCharacterDao()
    }

}