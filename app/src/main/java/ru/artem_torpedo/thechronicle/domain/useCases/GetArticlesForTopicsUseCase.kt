package ru.artem_torpedo.thechronicle.domain.useCases

import kotlinx.coroutines.flow.Flow
import ru.artem_torpedo.thechronicle.domain.entity.Article
import ru.artem_torpedo.thechronicle.domain.iRepository.NewsRepository
import javax.inject.Inject

class GetArticlesForTopicsUseCase @Inject constructor(
    val repository: NewsRepository,
) {
    operator fun invoke(topics: List<String>): Flow<List<Article>> {
        return repository.getArticlesForTopics(topics)
    }
}