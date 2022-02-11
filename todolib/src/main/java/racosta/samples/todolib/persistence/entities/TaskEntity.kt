package racosta.samples.todolib.persistence.entities

interface TaskEntity {
    val id: Long
    val title: String
    val description: String?
    val remindMe: Long?
    val dueDate: Long?
    val isDone: Boolean
    val groupId: Long?
}