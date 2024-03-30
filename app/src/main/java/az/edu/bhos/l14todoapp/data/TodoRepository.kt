package az.edu.bhos.l14todoapp.data

import az.edu.bhos.l14todoapp.entities.TodoEntity
import kotlinx.coroutines.flow.Flow
import kotlin.io.path.fileVisitor

interface TodoRepository {
    suspend fun syncTodos()
    fun observeTodoEntries(): Flow<List<TodoEntity>>
    suspend fun save(todoEntity: TodoEntity)
}

class TodoRepositoryImpl(
    private val localData: TodoLocalData,
    private val remoteData: TodoRemoteData
) : TodoRepository {

    override suspend fun syncTodos() {
        val todoList = remoteData.getTodos()
        // TODO save to local
        todoList.forEach {
            localData.save(it.toEntity())
        }

    }

    override fun observeTodoEntries(): Flow<List<TodoEntity>> {
        return localData.observeTodoItems()
    }

    override suspend fun save(todoEntity: TodoEntity) {
        localData.save(todoEntity)
    }

}