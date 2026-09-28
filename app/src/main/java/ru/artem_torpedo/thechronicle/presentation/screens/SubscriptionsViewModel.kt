@file:OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)

package ru.artem_torpedo.thechronicle.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.artem_torpedo.thechronicle.domain.entity.Article
import ru.artem_torpedo.thechronicle.domain.useCases.mainScreen.AddNewSubscriptionUseCase
import ru.artem_torpedo.thechronicle.domain.useCases.mainScreen.DeleteArticlesForTopicsUseCase
import ru.artem_torpedo.thechronicle.domain.useCases.mainScreen.DeleteSubscriptionUseCase
import ru.artem_torpedo.thechronicle.domain.useCases.mainScreen.GetAllSubscriptionsUseCase
import ru.artem_torpedo.thechronicle.domain.useCases.mainScreen.GetArticlesForTopicsUseCase
import ru.artem_torpedo.thechronicle.domain.useCases.mainScreen.UpdateAllArticlesUseCase
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SubscriptionsViewModel @Inject constructor(
    val addNewSubscriptionUseCase: AddNewSubscriptionUseCase,
    val deleteArticlesForTopicsUseCase: DeleteArticlesForTopicsUseCase,
    val deleteSubscriptionUseCase: DeleteSubscriptionUseCase,
    val getAllSubscriptionsUseCase: GetAllSubscriptionsUseCase,
    val getArticlesForTopicsUseCase: GetArticlesForTopicsUseCase,
    val updateAllArticlesUseCase: UpdateAllArticlesUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState>
        get() = _state.asStateFlow()

    init {
        getSubscriptions()
        observerSelectedTopics()
    }

    fun processCommand(command: Command) {
        when (command) {
            Command.AddSubscription -> {
                viewModelScope.launch {
                    val topic = state.value.query.trim()
                    _state.update {
                        it.copy(query = "")
                    }
                    addNewSubscriptionUseCase(topic)
                }
            }

            Command.DeleteArticles -> {
                viewModelScope.launch {
                    deleteArticlesForTopicsUseCase(state.value.selectedTopics)
                }
            }

            is Command.DeleteSubscription -> {
                viewModelScope.launch {
                    deleteSubscriptionUseCase(command.topic)
                }
            }

            is Command.InputTopic -> {
                _state.update {
                    it.copy(query = command.query)
                }
            }

            Command.RefreshData -> {
                viewModelScope.launch {
                    updateAllArticlesUseCase()
                }
            }

            is Command.ToggleTopicSelection -> {
                val newSubscriptions = state.value.subscriptions.toMutableMap()
                val value = newSubscriptions[command.topic] ?: true
                newSubscriptions[command.topic] = !value
                _state.update {
                    it.copy(subscriptions = newSubscriptions)
                }
            }
        }
    }

    private fun getSubscriptions() {
        viewModelScope.launch {
            getAllSubscriptionsUseCase().collect { subs ->
                _state.update { previousState ->
                    val newSubscriptions = subs.associateWith { topic ->
                        previousState.subscriptions[topic] ?: true
                    }
                    previousState.copy(subscriptions = newSubscriptions)
                }
            }
        }
    }

    private fun observerSelectedTopics() {
        viewModelScope.launch {
            state
                .map { it.selectedTopics }
                .distinctUntilChanged()
                .debounce(200.milliseconds)
                .flatMapLatest { topics ->
                    getArticlesForTopicsUseCase(topics)
                }.collect { articles ->
                    _state.update { previousState ->
                        previousState.copy(articles = articles)
                    }
                }
        }
    }
}


sealed interface Command {

    data class InputTopic(val query: String) : Command

    data object AddSubscription : Command

    data object RefreshData : Command

    data object DeleteArticles : Command

    data class ToggleTopicSelection(val topic: String) : Command

    data class DeleteSubscription(val topic: String) : Command
}

data class MainScreenState(
    val query: String = "",
    val subscriptions: Map<String, Boolean> = emptyMap(),
    val articles: List<Article> = emptyList(),
) {
    val buttonState: Boolean
        get() = query.isNotBlank()

    val selectedTopics: List<String>
        get() = subscriptions.filter {
            it.value
        }.map {
            it.key
        }

    val articlesCount: Int
        get() = articles.size

    val selectedTopicsCount: Int
        get() = selectedTopics.size
}