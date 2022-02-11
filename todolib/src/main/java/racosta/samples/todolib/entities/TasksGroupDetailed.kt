package racosta.samples.todolib.entities

data class TasksGroupDetailed(
    val id: Long? = null,
    val name: String? = null,
    val tasks: List<Task>
)
