package racosta.samples.composetodo.todologic.usecases

import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import racosta.samples.composetodo.isEqualTo
import racosta.samples.composetodo.persistence.daos.TaskDao
import racosta.samples.composetodo.persistence.daos.TaskGroupDao
import racosta.samples.composetodo.task
import racosta.samples.composetodo.taskEntity
import racosta.samples.composetodo.taskGroupEntity
import racosta.samples.composetodo.todologic.entities.TasksGroupDetailed

class GetTaskGroupDetailedForIdUseCaseTest {

    private lateinit var out: GetTaskGroupDetailedForIdUseCase

    private val mockedTasksDao: TaskDao = mockk()
    private val mockedTaskGroupDao: TaskGroupDao = mockk()

    @Before
    fun setUp() {
        out = GetTaskGroupDetailedForIdUseCase(
            mockedTasksDao,
            mockedTaskGroupDao
        )
    }

    @Test
    fun `given 2 tasks for the wanted id emits task details with 2 tasks`() = runBlocking {
        val groupId = 1L
        every { mockedTasksDao.getAllForGroupId(groupId) } returns flowOf(
            listOf(
                taskEntity(1, groupId = groupId),
                taskEntity(2, groupId = groupId)
            )
        )

        every { mockedTaskGroupDao.getForId(groupId) } returns flowOf(
            taskGroupEntity(groupId)
        )

        val resultsList = out.taskGroupDetailed(groupId).first()
        val expected = TasksGroupDetailed(
            groupId,
            "",
            listOf(
                task(1, groupId = groupId),
                task(2, groupId = groupId)
            )
        )

        assertThat(resultsList, isEqualTo(expected))
    }

    @Test
    fun `given 2 tasks with no group emits no group task details with 2 tasks`() = runBlocking {
        val groupId = null
        every { mockedTasksDao.getAllForGroupId(groupId) } returns flowOf(
            listOf(
                taskEntity(1, groupId = groupId),
                taskEntity(2, groupId = groupId)
            )
        )

        val resultsList = out.taskGroupDetailed(groupId).first()
        val expected = TasksGroupDetailed(
            groupId,
            null,
            listOf(
                task(1, groupId = groupId),
                task(2, groupId = groupId)
            )
        )

        verify { mockedTaskGroupDao wasNot Called }
        assertThat(resultsList, isEqualTo(expected))
    }
}