package racosta.samples.todolib.entities

data class NewTask(
    val title: String,
    val remindMe: Long? = null,
    val dueDate: Long? = null,
    val groupId: Long? = null
)