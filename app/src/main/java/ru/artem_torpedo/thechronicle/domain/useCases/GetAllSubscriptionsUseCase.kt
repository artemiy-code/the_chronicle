package ru.artem_torpedo.thechronicle.domain.useCases

import kotlinx.coroutines.flow.Flow
import ru.artem_torpedo.thechronicle.domain.iRepository.NewsRepository
import javax.inject.Inject

class GetAllSubscriptionsUseCase @Inject constructor(
    val repository: NewsRepository,
) {
    operator fun invoke(): Flow<List<String>> {
        return repository.getAllSubscriptions()
    }
}