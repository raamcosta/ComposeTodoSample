package racosta.samples.composetodo.ui.viewmodels

import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
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
import racosta.samples.composetodo.ui.screens.TaskGroupScreen
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var out: HomeViewModel

    private val mockedGetAllTaskGroupsUseCase: GetAllTaskGroupsUseCase = mockk()
    private val mockedAddNewTasksUseCase: AddNewTasksUseCase = mockk()

    @Before
    fun setUp() = runBlocking(testDispatcher) {
        every { mockedGetAllTaskGroupsUseCase.allTaskGroups } returns flow {}

        mockLogger()
        mockViewModelLaunch(testDispatcher)

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

            out.newTaskGroupName.value = validNewGroupName
            out.onAddNewTasksGroupClick()

            coVerify {
                mockedAddNewTasksUseCase.addNewTaskGroup(NewTaskGroup(validNewGroupName))
                mockedGetAllTaskGroupsUseCase wasNot Called
            }
        }

    @Test
    fun `when user inputs a valid new group name, add button should be enabled`() {
        out.onNewTaskGroupNameChanged(" ")

        assertThat(out.newTaskGroupButtonEnabled.value, isEqualTo(false))

        out.onNewTaskGroupNameChanged(validNewGroupName)

        assertThat(out.newTaskGroupButtonEnabled.value, isEqualTo(true))
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
        val hotFlow = MutableSharedFlow<List<TasksGroupSummary>>()
        every { mockedGetAllTaskGroupsUseCase.allTaskGroups } returns hotFlow

        out = HomeViewModel(
            mockedGetAllTaskGroupsUseCase,
            mockedAddNewTasksUseCase
        )

        val first = mutableListOf(tasksGroup(1, "first", 1, 1))
        hotFlow.emit(first)
        assertThat(out.taskGroups.value, isEqualTo(first))

        val second = first.toMutableList().apply { add(tasksGroup(2, "second", 1, 1)) }
        hotFlow.emit(second)
        assertThat(out.taskGroups.value, isEqualTo(second))

        hotFlow.emit(first)
        assertThat(out.taskGroups.value, isEqualTo(first))
    }

    @Test
    fun `when task group clicked, navigator is called with correct parameters`() {
        out.navigator = mockk(relaxed = true)

        out.onTaskGroupClick(3)

        verify {
            out.navigator!!.goTo(TaskGroupScreen.createRoute(3))
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