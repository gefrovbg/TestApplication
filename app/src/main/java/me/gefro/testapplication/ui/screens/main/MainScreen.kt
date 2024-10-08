package me.gefro.testapplication.ui.screens.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import me.gefro.testapplication.R
import me.gefro.testapplication.ui.components.RepositoryItemView
import me.gefro.testapplication.ui.screens.downloaded.DownloadedFilesRoute
import me.gefro.testapplication.ui.theme.LocalNavController
import me.gefro.testapplication.ui.theme.LocalThemeResources
import me.gefro.testapplication.ui.tools.SafeArea
import me.gefro.testapplication.ui.tools.clearFocusOnKeyboardDismiss
import me.gefro.testapplication.ui.tools.noRippleClickable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun MainScreen(
    viewModel: MainScreenViewModel = koinViewModel(),
    safeArea: SafeArea = koinInject()
){

    val search by viewModel.search.collectAsState()

    val topSafeArea by safeArea.top.collectAsState()
    val bottomSafeArea by safeArea.bottom.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    val refresh by viewModel.refreshing.collectAsState()

    val refreshState = rememberPullRefreshState(
        refreshing = refresh,
        onRefresh = {

        }
    )

    val uriHandler = LocalUriHandler.current

    val navController = LocalNavController.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .noRippleClickable {
                keyboardController?.hide()
            },
        topBar = {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(
                    bottomEnd = 12.dp,
                    bottomStart = 12.dp
                ),
                colors = CardColors(
                    containerColor = LocalThemeResources.current.secondary,
                    contentColor = Color.Unspecified,
                    disabledContainerColor = Color.Unspecified,
                    disabledContentColor = Color.Unspecified,
                )
            ){
                Spacer(modifier = Modifier.height(topSafeArea.dp))
                Row(
                    modifier = Modifier
                        .padding(
                            vertical = 12.dp,
                            horizontal = 16.dp
                        )
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = search,
                        onValueChange = {
                            if (it != search) {
                                viewModel.setSearch(it)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .clearFocusOnKeyboardDismiss(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            },
                        ),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(R.drawable.ic_download_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .noRippleClickable {
                                navController.navigate(DownloadedFilesRoute)
                            },
                        contentScale = ContentScale.FillHeight
                    )
                }

            }
        }
    ) { padding ->

        val searchedListRepositories = viewModel.searchedListRepositories?.collectAsLazyPagingItems()
        val lazyListState by viewModel.lazyListState.collectAsState()

        LaunchedEffect(padding) {
            Log.d("padding", "$padding")
        }

        LaunchedEffect(lazyListState.isScrollInProgress) {
            if(lazyListState.isScrollInProgress)
                keyboardController?.hide()
        }

        LaunchedEffect(searchedListRepositories?.loadState){
            val state = searchedListRepositories?.loadState
            state?.let {
                (viewModel::setRefreshing) (
                    state.refresh == LoadState.Loading ||
                    state.append == LoadState.Loading ||
                    state.prepend == LoadState.Loading
                )
            }

        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = padding.calculateTopPadding() + 12.dp, bottom = padding.calculateBottomPadding() + 12.dp)
            ) {
                if (searchedListRepositories != null) {
                    if (searchedListRepositories.itemCount > 0) {
                        items(searchedListRepositories.itemCount) { index ->
                            searchedListRepositories[index]?.let { item ->
                                RepositoryItemView(
                                    item = item,
                                    onClickItem = {
                                        val url = item.html_url
                                        url?.let {
                                            uriHandler.openUri(uri = url)
                                        }
                                    },
                                    onClickDownload = viewModel::download
                                )
                            }
                        }
                    }
                    when (searchedListRepositories.loadState.append) {
                        is LoadState.Error -> {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ){
                                    Text(
                                        text = "Error"
                                    )
                                }
                            }
                        }
                        is LoadState.NotLoading, LoadState.Loading -> Unit
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = refresh,
                state = refreshState,
                modifier = Modifier
                    .padding(top = padding.calculateTopPadding())
            )
        }
    }
}