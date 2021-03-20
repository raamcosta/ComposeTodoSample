package racosta.samples.composetodo.ui.viewmodels

import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import racosta.samples.composetodo.*
import racosta.samples.composetodo.todologic.entities.NewTaskGroup
import racosta.samples.composetodo.todologic.entities.TasksGroupSummary
import racosta.samples.composetodo.todologic.usecases.AddNewTasksUseCase
import racosta.samples.composetodo.todologic.usecases.GetAllTaskGroupsUseCase
import racosta.samples.composetodo.ui.screens.taskgroup.TaskGroupArguments
import racosta.samples.composetodo.ui.screens.taskgroup.TaskGroupScreen
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var out: HomeViewModel

    private val mockedGetAllTaskGroupsUseCase: GetAllTaskGroupsUseCase = mockk()
    private val mockedAddNewTasksUseCase: AddNewTasksUseCase = mockk()

    private val hotFlowAllTaskGroups = MutableSharedFlow<List<TasksGroupSummary>>()

    @Before
    fun setUp() = runBlocking(testDispatcher) {
        every { mockedGetAllTaskGroupsUseCase.allTaskGroups } returns hotFlowAllTaskGroups

        mockLogger()
        mockViewModelScope(testDispatcher)

        out = HomeViewModel(
            mockedGetAllTaskGroupsUseCase,
            mockedAddNewTasksUseCase
        )

        clearRecordedCalls(mockedGetAllTaskGroupsUseCase)
    }

    @Test
    fun `when user clicks add new task group button correct use case should be called`() =
        runBlocking {
            coEvery { mockedAddNewTasksUseCase.addNewTaskGroup(any()) } returns 1L

            (out.newTaskGroupName as MutableStateFlow).value = validNewGroupName
            out.onAddNewTasksGroupClick()

            coVerify {
                mockedAddNewTasksUseCase.addNewTaskGroup(NewTaskGroup(validNewGroupName))
                mockedGetAllTaskGroupsUseCase wasNot Called
            }
        }

    @Test
    fun `when user inputs a valid new group name, add button should be enabled`() {
        out.onNewTaskGroupNameChanged(" ")

        assertThat(out.newTasksGroupButtonEnabled.value, isEqualTo(false))

        out.onNewTaskGroupNameChanged(validNewGroupName)

        assertThat(out.newTasksGroupButtonEnabled.value, isEqualTo(true))
    }

    @Test
    fun `when user inputs a new group name, new group name state is`() {
        out.onNewTaskGroupNameChanged("abc")

        assertThat(out.newTaskGroupName.value, isEqualTo("abc"))

        out.onNewTaskGroupNameChanged("qweqwe")

        assertThat(out.newTaskGroupName.value, isEqualTo("qweqwe"))
    }

    @Test
    fun `when view model is initialized, the first emitted value is set on the state`() = runBlocking(testDispatcher) {
        //we need to launch a new job to collect, the state only updates if someone is subscribed
        val job = launch {
            out.taskGroups.collect {  }
        }

        val first = mutableListOf(tasksGroup(1, "first", 1, 1))
        hotFlowAllTaskGroups.emit(first)
        assertThat(out.taskGroups.value, isEqualTo(first))

        val second = first.toMutableList().apply { add(tasksGroup(2, "second", 1, 1)) }
        hotFlowAllTaskGroups.emit(second)
        assertThat(out.taskGroups.value, isEqualTo(second))

        hotFlowAllTaskGroups.emit(first)
        assertThat(out.taskGroups.value, isEqualTo(first))

        job.cancel()
    }

    @Test
    fun `when task group clicked, navigator is called with correct parameters`() {
        out.navigator = mockk(relaxed = true)

        out.onTaskGroupClick(3)

        verify {
            out.navigator!!.goTo(TaskGroupScreen, TaskGroupArguments(3))
        }
    }

    companion object {

        private const val validNewGroupName = "NEW GROUP NAME"

        private val mainThreadSurrogate =
            Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            Dispatchers.setMain(mainThreadSurrogate)
        }

        @AfterClass
        @JvmStatic
        fun afterAll() {
            Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
            mainThreadSurrogate.close()
        }
    }
}