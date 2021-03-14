package racosta.samples.composetodo.todologic.usecases

import io.mockk.Called
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import racosta.samples.composetodo.*

import racosta.samples.composetodo.persistence.daos.TaskDao
import racosta.samples.composetodo.persistence.daos.TaskGroupDao

class AddNewTasksUseCaseTest {

    companion object {
        private const val TASK_TITLE = "TASK TITLE"
    }

    private lateinit var out: AddNewTasksUseCase

    private val mockedTaskDao: TaskDao = mockk(relaxed = true)
    private val mockedTaskGroupDao: TaskGroupDao = mockk(relaxed = true)

    @Before
    fun setUp() {
        out = AddNewTasksUseCase(
            mockedTaskDao,
            mockedTaskGroupDao
        )
    }

    @Test
    fun `when a new task is given it calls correct dao with that task entity`() = runBlocking {
        out.addNewTask(newTask(title = TASK_TITLE))

        coVerify {
            mockedTaskDao.insertOrUpdate(taskEntity(title = TASK_TITLE, groupId = null))
            mockedTaskGroupDao wasNot Called
        }
    }

    @Test
    fun `when a new task group is given it calls correct dao with that task group entity`() = runBlocking {
        out.addNewTaskGroup(newTaskGroup(name = TASK_TITLE))

        coVerify {
            mockedTaskGroupDao.insertOrUpdate(taskGroupEntity(name = TASK_TITLE))
            mockedTaskDao wasNot Called
        }
    }
}