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
fun PhotoGrid() {
    val userPhotos = samplePhotos.filter { it.userImage }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(userPhotos) { photo ->
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = photo.imageRes),
                    contentDescription = photo.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = photo.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = photo.description,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = photo.date,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Row(
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    photo.hashtags.forEach { tag ->
                        Text(
                            text = tag,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }
        }
    }
}