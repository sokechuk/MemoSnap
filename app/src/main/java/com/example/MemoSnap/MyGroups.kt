package com.example.MemoSnap.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale

@Composable
fun MyGroups() {
    val grouped = samplePhotos.groupBy { it.group }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = false
    ) {
        grouped.forEach { (groupTitle, images) ->
            item {
                GroupContainer(
                    title = groupTitle,
                    images = images
                )
            }
        }
    }
}

@Composable
fun GroupContainer(title: String, images: List<PhotoItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            userScrollEnabled = false
        ) {
            items(images) { photo ->
                Image(
                    painter = painterResource(id = photo.imageRes),
                    contentDescription = photo.title,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}