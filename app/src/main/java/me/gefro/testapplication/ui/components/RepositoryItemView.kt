package me.gefro.testapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.gefro.domain.models.github.RepositoriesItemDto
import me.gefro.testapplication.R
import me.gefro.testapplication.ui.theme.LocalThemeResources

@Composable
internal fun RepositoryItemView(
    item: RepositoriesItemDto,
    onClickItem:() -> Unit,
    onClickDownload:(item: RepositoriesItemDto) -> Unit,
    showDownload: Boolean = true
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClickItem, enabled = !item.html_url.isNullOrBlank())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
            if (showDownload) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val repo = item.name
                        val owner = item.owner?.login
                        if (repo != null && owner != null) {
                            onClickDownload(item)
                        }
                    },
                    enabled = !item.owner?.login.isNullOrBlank() && !item.name.isNullOrBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Download",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 18.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}