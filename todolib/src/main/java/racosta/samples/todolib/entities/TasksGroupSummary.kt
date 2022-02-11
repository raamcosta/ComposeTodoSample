package racosta.samples.todolib.entities

data class TasksGroupSummary(
    val id: Long? = null,
    val name: String? = null,
    val totalTasks: Int,
    val tasksDone: Int,
)