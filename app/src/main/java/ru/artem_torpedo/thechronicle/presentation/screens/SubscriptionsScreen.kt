@file:OptIn(ExperimentalMaterial3Api::class)

package ru.artem_torpedo.thechronicle.presentation.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import ru.artem_torpedo.thechronicle.R
import ru.artem_torpedo.thechronicle.presentation.utils.convertToDate

@Composable
fun SubscriptionsScreen(
    viewModel: SubscriptionsViewModel = hiltViewModel(),
) {
    val activityContext = LocalContext.current

    val stateValue = viewModel.state.collectAsState().value


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = {
                    Text(
                        text = stringResource(R.string.my_news),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                },
                actions = {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clip(CircleShape)
                            .clickable {
                                viewModel.processCommand(Command.RefreshData)
                            },
                        painter = painterResource(R.drawable.ic_refresh),
                        contentDescription = "Get new articles"
                    )
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clip(CircleShape)
                            .clickable {
                                viewModel.processCommand(Command.DeleteArticles)
                            },
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "Delete articles on the screen"
                    )
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clip(CircleShape)
                            .clickable {
                                // TODO: Settings
                            },
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = "Settings"
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            contentPadding = paddingValues
        ) {
            item {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = stateValue.query,
                    onValueChange = {
                        viewModel.processCommand(Command.InputTopic(it))
                    },
                    label = {
                        Text(text = stringResource(R.string.what_interests_you))
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = stateValue.buttonState,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = {
                        viewModel.processCommand(Command.AddSubscription)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = "Add subscription icon"
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 2.dp),
                        text = stringResource(R.string.add_subscription),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (stateValue.subscriptions.isEmpty()) {
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.no_subscriptions),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light
                    )
                }
            } else {
                item {
                    Text(
                        text = stringResource(
                            R.string.subscriptions,
                            stateValue.selectedTopicsCount
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        stateValue.subscriptions.forEach { (topic, status) ->
                            item(key = topic) {
                                FilterChip(
                                    selected = status,
                                    onClick = {
                                        viewModel.processCommand(Command.ToggleTopicSelection(topic))
                                    },
                                    label = {
                                        Text(text = topic)
                                    },
                                    trailingIcon = {
                                        Icon(
                                            modifier = Modifier.clickable {
                                                viewModel.processCommand(
                                                    Command.DeleteSubscription(topic)
                                                )
                                            },
                                            painter = painterResource(R.drawable.ic_close),
                                            contentDescription = "Delete topic",
                                            tint = if (status) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(
                                        width = 2.dp,
                                        brush = Brush.sweepGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.tertiary,
                                                MaterialTheme.colorScheme.primary
                                            )
                                        )
                                    ),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalDivider(
                        thickness = 2.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                }

                item {
                    if (stateValue.articles.isEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.no_articles_for_selected_topics),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Light
                        )
                    } else {

                        Text(
                            text = stringResource(R.string.articles, stateValue.articlesCount),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider(
                            thickness = 2.dp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(
                items = stateValue.articles,
                key = { article -> article.articleUrl }
            ) { article ->
                Column(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(12.dp)
                ) {
                    article.imageUrl?.also {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp, max = 200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            model = article.imageUrl,
                            contentDescription = "Article image",
                            contentScale = ContentScale.FillWidth
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        fontWeight = FontWeight.ExtraBold,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = article.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = article.sourceName,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = article.publishedAt.convertToDate(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = 0.6f
                            )
                        )
                    }
                    Row(
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Button(
                            modifier = Modifier.weight(10f),
                            shape = RoundedCornerShape(32.dp),
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, article.articleUrl.toUri())
                                activityContext.startActivity(intent)
                            },
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_two_pager),
                                contentDescription = "Go to article"
                            )
                            Text(
                                text = "Read"
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Button(
                            modifier = Modifier.weight(10f),
                            shape = RoundedCornerShape(32.dp),
                            onClick = {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "${article.title}.\n\n${article.articleUrl}"
                                    )
                                }
                                val chooser = Intent.createChooser(intent,null)
                                activityContext.startActivity(chooser)
                            },
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_share),
                                contentDescription = "Go to article"
                            )
                            Text(
                                text = "Share"
                            )
                        }
                    }
                }
            }
        }
    }
}