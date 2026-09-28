package ru.artem_torpedo.thechronicle.domain.useCases.mainScreen

import ru.artem_torpedo.thechronicle.domain.iRepository.NewsRepository
import javax.inject.Inject

class UpdateAllArticlesUseCase @Inject constructor(
    val repository: NewsRepository,
) {
    suspend operator fun invoke() {
        repository.updateAllArticles()
    }
}