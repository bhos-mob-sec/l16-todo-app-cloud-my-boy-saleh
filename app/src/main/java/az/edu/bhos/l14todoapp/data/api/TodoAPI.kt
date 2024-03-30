package az.edu.bhos.l14todoapp.data.api

import az.edu.bhos.l14todoapp.data.dto.TodoRemoteDto
import retrofit2.http.GET

interface TodoAPI {
    // TODO fix the issue

    @GET("todos")
    suspend fun getTodos(): List<TodoRemoteDto>
}