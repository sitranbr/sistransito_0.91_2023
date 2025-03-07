package net.sistransito.mobile.http.services

import net.sistransito.mobile.util.User
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {
    @GET("auth/dologin")
    suspend fun login(@Query("email") email: String, @Query("password") password: String): User
}
