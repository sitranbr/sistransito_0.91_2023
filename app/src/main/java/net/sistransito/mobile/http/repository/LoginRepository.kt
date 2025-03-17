package net.sistransito.mobile.http.repository

import net.sistransito.mobile.utility.User
import net.sistransito.mobile.http.services.LoginService
import net.sistransito.mobile.http.RetrofitClient

class LoginRepository private constructor(private val loginService: LoginService) {

    suspend fun login(email: String, password: String): User? {
        return loginService.login(email, password)
    }

    companion object {
        fun create(): LoginRepository {
            val loginService = RetrofitClient.createService(LoginService::class.java)
            return LoginRepository(loginService)
        }
    }
}
