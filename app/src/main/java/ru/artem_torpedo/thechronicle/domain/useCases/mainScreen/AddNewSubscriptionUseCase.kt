package ru.artem_torpedo.thechronicle.domain.useCases.mainScreen

import ru.artem_torpedo.thechronicle.domain.iRepository.NewsRepository
import javax.inject.Inject

class AddNewSubscriptionUseCase @Inject constructor(
    val repository: NewsRepository,
) {
    suspend operator fun invoke(topic: String) {
        repository.addNewSubscription(topic)
        repository.updateArticlesForNewTopic(topic)
    }
}