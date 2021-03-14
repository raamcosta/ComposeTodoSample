package racosta.samples.composetodo.todologic.usecases

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import racosta.samples.composetodo.*
import racosta.samples.composetodo.persistence.daos.TaskDao
import racosta.samples.composetodo.persistence.daos.TaskGroupDao
import racosta.samples.composetodo.persistence.entities.TaskEntity
import racosta.samples.composetodo.persistence.entities.TaskGroupEntity
import racosta.samples.composetodo.todologic.entities.TasksGroupSummary

class GetAllTasksGroupsUseCaseTest {

    private lateinit var out: GetAllTaskGroupsUseCase

    private val mockedTaskDao: TaskDao = mockk()
    private val mockedTaskGroupDao: TaskGroupDao = mockk()

    @Before
    fun setUp() {
        out = GetAllTaskGroupsUseCase(
            mockedTaskDao,
            mockedTaskGroupDao
        )
    }

    @Test
    fun `when db groups change the correct value is emitted`() = runBlocking {
        every { mockedTaskGroupDao.getAll() } returns flow {
            emit(emptyList<TaskGroupEntity>())
            emit(listOf(taskGroupEntity(1, name = "new task group")))
        }
        every { mockedTaskDao.getAll() } returns flow {
            emit(listOf(taskEntity(id = 1, groupId = null)))
        }

        val listOfExpectedEmissions = listOf(
            listOf(TasksGroupSummary(totalTasks = 1, tasksDone = 0)),
            listOf(
                TasksGroupSummary(id = null, totalTasks = 1, tasksDone = 0),
                TasksGroupSummary(id = 1, name = "new task group", totalTasks = 0, tasksDone = 0)
            )
        )
        var i = 0

        out.allTaskGroups.collect {
            assertThat(it, isEqualTo(listOfExpectedEmissions[i++]))
        }
    }

    @Test
    fun `when db tasks change the correct value is emitted`() = runBlocking {
        every { mockedTaskGroupDao.getAll() } returns flow {
            emit(emptyList<TaskGroupEntity>())
            emit(listOf(taskGroupEntity(1, name = "new task group")))
        }
        every { mockedTaskDao.getAll() } returns flow {
            emit(listOf(taskEntity(id = 1, groupId = null)))
            emit(listOf(taskEntity(id = 1, groupId = null), taskEntity(id = 2, groupId = 1)))
            emit(listOf(taskEntity(id = 1, groupId = null), taskEntity(id = 2, groupId = 1, isDone = true), taskEntity(id = 3, groupId = 1)))
        }

        val listOfExpectedEmissions = listOf(
            listOf(
                TasksGroupSummary(id = null, name = null, totalTasks = 1, tasksDone = 0),
            ),
            listOf(
                TasksGroupSummary(id = null, totalTasks = 1, tasksDone = 0),
                TasksGroupSummary(id = 1, name = "new task group", totalTasks = 1, tasksDone = 0)
            )   ,
            listOf(
                TasksGroupSummary(id = null, totalTasks = 1, tasksDone = 0),
                TasksGroupSummary(id = 1, name = "new task group", totalTasks = 2, tasksDone = 1)
            )
        )
        var i = 0

        out.allTaskGroups.collect {
            println("collected #$i")
            assertThat(it, isEqualTo(listOfExpectedEmissions[i++]))
        }
    }

    @Test
    fun `given empty db get all returns 1 default empty group`() = runBlocking {
        coEvery { mockedTaskDao.getAll() } returns flow {
            emit(emptyList<TaskEntity>())
        }
        coEvery { mockedTaskGroupDao.getAll() } returns flow {
            emit(emptyList<TaskGroupEntity>())
        }

        val result = out.allTaskGroups.first()

        assertThat(result, `is`(listOf(defaultTaskGroup())))
    }

    @Test
    fun `given 1 task with specific group get all returns 2 tasks groups`() = runBlocking {
        coEvery { mockedTaskDao.getAll() } returns flow {
            emit(listOf(taskEntity(id = 1, groupId = 1)))
        }
        coEvery { mockedTaskGroupDao.getAll() } returns flow {
            emit(listOf(taskGroupEntity(id = 1)))
        }

        val result = out.allTaskGroups.first()

        val expected = listOf(
            defaultTaskGroup(),
            tasksGroup(
                id = 1,
                totalTasks = 1
            )
        )

        assertThat(result, `is`(expected))
    }

    @Test
    fun `given 2 tasks of same group get all returns 2 groups one of them with two total tasks`() =
        runBlocking {
            coEvery { mockedTaskDao.getAll() } returns flow {
                emit(
                    listOf(
                        taskEntity(id = 1, groupId = 1),
                        taskEntity(id = 2, groupId = 1),
                    )
                )
            }
            coEvery { mockedTaskGroupDao.getAll() } returns flow {
                emit(listOf(taskGroupEntity(id = 1)))
            }

            val result = out.allTaskGroups.first()

            val expected = listOf(
                defaultTaskGroup(),
                tasksGroup(
                    id = 1,
                    totalTasks = 2
                )
            )

            assertThat(result, `is`(expected))
        }

    @Test
    fun `given 2 tasks of different groups get all returns 2 tasks groups each with 1 task and the default group`() =
        runBlocking {
            coEvery { mockedTaskDao.getAll() } returns flow {
                emit(
                    listOf(
                        taskEntity(id = 1, groupId = 1),
                        taskEntity(id = 2, groupId = 2),
                    )
                )
            }
            coEvery { mockedTaskGroupDao.getAll() } returns flow {
                emit(
                    listOf(
                        taskGroupEntity(id = 1),
                        taskGroupEntity(id = 2),
                    )
                )
            }

            val result = out.allTaskGroups.first()

            val expected = listOf(
                defaultTaskGroup(),
                tasksGroup(
                    id = 1,
                    totalTasks = 1
                ),
                tasksGroup(
                    id = 2,
                    totalTasks = 1
                )
            )

            assertThat(result, `is`(expected))
        }

    @Test
    fun `given 2 tasks with no group and two other groups, get all returns 1 task group with no id with both tasks and the two other empty ones`() =
        runBlocking {
            coEvery { mockedTaskDao.getAll() } returns flow {
                emit(
                    listOf(
                        taskEntity(id = 1, groupId = null),
                        taskEntity(id = 2, groupId = null),
                    )
                )
            }
            coEvery { mockedTaskGroupDao.getAll() } returns flow {
                emit(
                    listOf(
                        taskGroupEntity(id = 1),
                        taskGroupEntity(id = 2),
                    )
                )
            }

            val result = out.allTaskGroups.first()

            val expected = listOf(
                defaultTaskGroup(
                    totalTasks = 2
                ),
                tasksGroup(
                    id = 1,
                    totalTasks = 0
                ),
                tasksGroup(
                    id = 2,
                    totalTasks = 0
                )
            )

            assertThat(result, `is`(expected))
        }

    @Test
    fun `given a complete scenario on db get all returns correct tasks`() = runBlocking {
        coEvery { mockedTaskDao.getAll() } returns flow {
            emit(
                listOf(
                    taskEntity(id = 1, groupId = null),
                    taskEntity(id = 2, groupId = null),
                    taskEntity(id = 3, groupId = 1),
                    taskEntity(id = 4, groupId = 1),
                    taskEntity(id = 5, groupId = 2),
                    taskEntity(id = 6, groupId = 2),
                )
            )
        }
        coEvery { mockedTaskGroupDao.getAll() } returns flow {
            emit(
                listOf(
                    taskGroupEntity(id = 1),
                    taskGroupEntity(id = 2),
                )
            )
        }

        val result = out.allTaskGroups.first()

        val expected = listOf(
            tasksGroup(
                id = null,
                groupName = null,
                totalTasks = 2
            ),
            tasksGroup(
                id = 1,
                totalTasks = 2
            ),
            tasksGroup(
                id = 2,
                totalTasks = 2
            )
        )

        assertThat(result, `is`(expected))
    }
}