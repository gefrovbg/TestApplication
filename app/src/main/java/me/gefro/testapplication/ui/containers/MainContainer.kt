package me.gefro.testapplication.ui.containers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import me.gefro.testapplication.R
import me.gefro.testapplication.services.DownloadService
import me.gefro.testapplication.ui.screens.downloaded.DownloadedFilesRoute
import me.gefro.testapplication.ui.screens.downloaded.DownloadedFilesScreen
import me.gefro.testapplication.ui.screens.main.MainRoute
import me.gefro.testapplication.ui.screens.main.MainScreen
import me.gefro.testapplication.ui.theme.LocalNavController
import me.gefro.testapplication.ui.theme.LocalThemeResources
import me.gefro.testapplication.ui.tools.formatBinarySize
import org.koin.compose.koinInject

@Composable
internal fun MainContainer(
    downloadService: DownloadService = koinInject()
){

    val navController = LocalNavController.current

    val downloadingFile by downloadService.downloadingFile.collectAsState()

    NavHost(
        navController = navController,
        startDestination = MainRoute,
    ){
        composable<MainRoute> {
            MainScreen()
        }

        composable<DownloadedFilesRoute> {
            DownloadedFilesScreen()
        }
    }

    downloadingFile?.let {
        val item = it.first
        Dialog(
            onDismissRequest = {

            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            ),
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.05f)),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = item.name ?: "",
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 18.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.description ?: "",
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 14.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.owner?.login ?: "",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 12.sp,
                                    lineHeight = 14.sp,
                                    fontWeight = FontWeight.W500
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = (item.watchers ?: "").toString(),
                                        maxLines = 1,
                                        fontSize = 12.sp,
                                        lineHeight = 14.sp,
                                        fontWeight = FontWeight.W500
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Image(
                                        painter = painterResource(R.drawable.ic_star_rate_24),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(LocalThemeResources.current.onBackground),
                                        modifier = Modifier.height(18.dp),
                                    )
                                }
                            }
                            val done = it.second.first
                            val size = it.second.second
                            Text(
                                text = if (size > 0L) "${formatBinarySize(done)}/${formatBinarySize(size)}" else formatBinarySize(done)
                            )
                            if (size > 0L) {
                                Spacer(modifier = Modifier.height(8.dp))
                                val value = done / ((size ?: 1L) / 100f)
                                LinearProgressIndicator(
                                    progress = {
                                        value / 100
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    color = LocalThemeResources.current.onSurface,
                                    strokeCap = StrokeCap.Round
                                )
                            }
                        }
                    }
                }
            }
        )
    }

}