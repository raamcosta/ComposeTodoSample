package racosta.samples.todolib.entities

data class Task(
    val id: Long,
    val title: String,
    val description: String?,
    val remindMe: Long?,
    val dueDate: Long?,
    val isDone: Boolean,
    val groupId: Long?,
)
