package racosta.samples.composetodo.todologic.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import racosta.samples.composetodo.persistence.daos.TaskDao
import racosta.samples.composetodo.persistence.daos.TaskGroupDao
import racosta.samples.composetodo.persistence.entities.TaskEntity
import racosta.samples.composetodo.persistence.entities.TaskGroupEntity
import racosta.samples.composetodo.todologic.entities.TasksGroupSummary

class GetAllTaskGroupsUseCase(
    private val taskDao: TaskDao,
    private val taskGroupDao: TaskGroupDao
) {

    val allTaskGroups: Flow<List<TasksGroupSummary>>
        get() = taskGroupDao.getAll().combine(taskDao.getAll(), ::combine)

    private fun combine(
        taskGroups: List<TaskGroupEntity>,
        tasks: List<TaskEntity>
    ): List<TasksGroupSummary> {
        val taskEntities: Map<Long?, List<TaskEntity>> = tasks.groupBy { it.groupId }

        val result = mutableListOf<TasksGroupSummary>()

        if (taskEntities.containsKey(null)) {
            val tasksWithNoGroup = taskEntities[null]!!
            result.add(TasksGroupSummary(totalTasks = tasksWithNoGroup.size, tasksDone = tasksWithNoGroup.count { it.isDone }))
        } else {
            result.add(TasksGroupSummary(totalTasks = 0, tasksDone = 0))
        }

        taskGroups.forEach { taskGroupEntity ->
            val tasksForGroup = taskEntities[taskGroupEntity.id] ?: emptyList()
            result.add(
                TasksGroupSummary(
                    taskGroupEntity.id,
                    taskGroupEntity.name,
                    tasksForGroup.size,
                    tasksForGroup.count { it.isDone }
                )
            )
        }

        return result
    }

//    private suspend fun combine(): List<TasksGroupSummary> = withContext(Dispatchers.IO) {
//        val taskEntities: Map<Long?, List<TaskEntity>> = taskDao.getAll().first().groupBy { it.groupId }
//
//        val result = mutableListOf<TasksGroupSummary>()
//
//        if (taskEntities.containsKey(null)) {
//            val taskWithNoGroup = taskEntities[null]!!
//            result.add(TasksGroupSummary(totalTasks = taskWithNoGroup.size, tasksDone = taskWithNoGroup.count { it.isDone }))
//        } else {
//            result.add(TasksGroupSummary(totalTasks = 0, tasksDone = 0))
//        }
//
//        this@toTasksGroups.forEach { taskGroupEntity ->
//            val tasks = taskEntities[taskGroupEntity.id] ?: emptyList()
//            result.add(
//                TasksGroupSummary(
//                    taskGroupEntity.id,
//                    taskGroupEntity.name,
//                    tasks.size,
//                    tasks.count { it.isDone }
//                )
//            )
//        }
//
//        result
//    }
}