package math.question.task.di


import math.question.task.repository.QuestionRepo
import org.koin.dsl.module


val repositoryModule = module {
    factory { QuestionRepo(get()) }

}