package ru.artem_torpedo.thechronicle.domain.useCases.mainScreen

import ru.artem_torpedo.thechronicle.domain.iRepository.NewsRepository
import javax.inject.Inject

class DeleteArticlesForTopicsUseCase @Inject constructor(
    val repository: NewsRepository,
) {
    suspend operator fun invoke(topics: List<String>) {
        repository.deleteArticlesForTopics(topics)
    }
}