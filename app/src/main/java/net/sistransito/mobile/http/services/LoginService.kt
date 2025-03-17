package net.sistransito.mobile.http.services

import net.sistransito.mobile.utility.User
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {
    @GET("auth/dologin")
    suspend fun login(@Query("email") email: String, @Query("password") password: String): User
}
