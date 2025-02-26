import com.book.mybook.model.LoginRequest
import com.book.mybook.model.LoginResponse
import com.book.mybook.model.RegisterRequest
import com.book.mybook.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


}