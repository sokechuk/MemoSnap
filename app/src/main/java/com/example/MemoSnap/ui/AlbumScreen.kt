package com.example.MemoSnap.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import coil.compose.rememberAsyncImagePainter
import com.example.MemoSnap.data.AlbumWithPhotos

@Composable
fun AlbumScreen(
    albumsWithPhotos: List<AlbumWithPhotos>,
    onUploadClicked: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onUploadClicked,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = "Upload")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(albumsWithPhotos) { album ->
                Text(
                    text = album.album.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(album.photos) { photo ->
                        Image(
                            painter = rememberAsyncImagePainter(Uri.parse(photo.uri)),
                            contentDescription = "Photo ${photo.id}",
                            modifier = Modifier
                                .size(120.dp)
                                .aspectRatio(1f)
                        )
                    }
                }

                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }
    }
}
