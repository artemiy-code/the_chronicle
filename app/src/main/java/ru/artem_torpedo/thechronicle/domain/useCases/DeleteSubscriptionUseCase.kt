package ru.artem_torpedo.thechronicle.domain.useCases

import ru.artem_torpedo.thechronicle.domain.iRepository.NewsRepository
import javax.inject.Inject

class DeleteSubscriptionUseCase @Inject constructor(
    val repository: NewsRepository,
) {
    suspend operator fun invoke(topic: String) {
        repository.deleteSubscription(topic)
    }
}