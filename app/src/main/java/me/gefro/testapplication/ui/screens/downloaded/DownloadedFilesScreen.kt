package me.gefro.testapplication.ui.screens.downloaded

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.gefro.testapplication.R
import me.gefro.testapplication.ui.components.RepositoryItemView
import me.gefro.testapplication.ui.theme.LocalNavController
import me.gefro.testapplication.ui.theme.LocalThemeResources
import me.gefro.testapplication.ui.tools.SafeArea
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
internal fun DownloadedFilesScreen(
    viewModel: DownloadedFilesScreenViewModel = koinViewModel(),
    safeArea: SafeArea = koinInject()
){

    LaunchedEffect(Unit) {
        (viewModel::loadData)()
    }

    val list by viewModel.listFromDb.collectAsState()

    val navController = LocalNavController.current

    val topSafeArea by safeArea.top.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
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
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back_24),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Downloaded Repositories",
                        maxLines = 1,
                        fontSize = 22.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
    ){ padding ->
        LaunchedEffect(padding) {
            Log.d("padding", "$padding")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = padding.calculateTopPadding() + 12.dp, bottom = padding.calculateBottomPadding() + 12.dp)
        ) {
            items(list){ item ->
                RepositoryItemView(
                    item = item.convertToRepo(),
                    onClickDownload = {},
                    onClickItem = {},
                    showDownload = false
                )
            }
        }
    }

}