package com.example.MemoSnap.ui
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.MemoSnap.R
import java.io.File
import java.io.FileOutputStream

class MyPhotosFragment : Fragment() {
    private lateinit var photosContainer: LinearLayout

    private lateinit var previewImage: ImageView
    private lateinit var btnSubmitPhoto: Button
    private var selectedImageUri: Uri? = null
    private var capturedBitmap: Bitmap? = null
    // For picking from gallery
    private val CAMERA_PERMISSION_REQUEST_CODE = 1001
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            previewImage.setImageURI(it)
            btnSubmitPhoto.isEnabled = true
        }
    }

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bitmap = result.data?.extras?.get("data") as? Bitmap
            capturedBitmap = bitmap
            previewImage.setImageBitmap(bitmap)
            btnSubmitPhoto.isEnabled = true
        }
    }
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoLauncher.launch(cameraIntent)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveImageToInternalStorage(bitmap: Bitmap, filename: String): String? {
        return try {
            val file = File(requireContext().filesDir, filename)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun loadRecentPhotos() {
        val container = view?.findViewById<GridLayout>(R.id.recentPhotoContainer)
        container?.removeAllViews()

        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val imageSize = (screenWidth - 5 * 8) / 4  // 4列，5个间隔

        val files = requireContext().filesDir.listFiles()
        val recentFiles = files?.sortedByDescending { it.lastModified() }

        recentFiles?.forEach { file ->
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val imageView = ImageView(requireContext())
            imageView.setImageBitmap(bitmap)

            val params = ViewGroup.MarginLayoutParams(imageSize, imageSize)
            params.setMargins(8, 8, 8, 8)
            imageView.layoutParams = params
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP


            imageView.setOnClickListener {
                showImagePreview(Uri.fromFile(file))
            }

            container?.addView(imageView)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_my_photos, container, false)

        val btnUploadPhoto = view.findViewById<Button>(R.id.btnUploadPhoto)
        val btnTakePhoto = view.findViewById<Button>(R.id.btnTakePhoto)
        previewImage = view.findViewById(R.id.previewImage)
        btnSubmitPhoto = view.findViewById(R.id.btnSubmitPhoto)
        btnUploadPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnTakePhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            } else {
                openCamera()
            }
        }
        btnSubmitPhoto.setOnClickListener {
            val savedPath: String? = when {
                selectedImageUri != null -> {
                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedImageUri)
                    saveImageToInternalStorage(bitmap, "img_${System.currentTimeMillis()}.png")
                }
                capturedBitmap != null -> {
                    saveImageToInternalStorage(capturedBitmap!!, "img_${System.currentTimeMillis()}.png")
                }
                else -> null
            }

            if (savedPath != null) {
                Toast.makeText(requireContext(), "Photo saved!", Toast.LENGTH_SHORT).show()
                loadRecentPhotos() // Step ③: Implement this next
            } else {
                Toast.makeText(requireContext(), "Failed to save photo", Toast.LENGTH_SHORT).show()
            }

            selectedImageUri = null
            capturedBitmap = null
            previewImage.setImageDrawable(null)
            btnSubmitPhoto.isEnabled = false
        }

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRecentPhotos() // Safely call this here to ensure view is ready
    }
    private fun saveToRecent(photoUri: Uri) {
        val prefs = requireContext().getSharedPreferences("recent_photos", 0)
        val key = "recent_list"

        val path = File(photoUri.path ?: return).absolutePath // 更保险

        val currentSet = prefs.getStringSet(key, mutableSetOf())?.toMutableList() ?: mutableListOf()
        currentSet.remove(path)
        currentSet.add(0, path)

        val limitedSet = currentSet.take(20).toSet()
        prefs.edit().putStringSet(key, limitedSet).apply()
    }
    private fun showImagePreview(photoUri: Uri) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_image_preview_with_comment, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.fullscreenImageView)
        val commentButton = dialogView.findViewById<Button>(R.id.btnComment)
        val commentText = dialogView.findViewById<TextView>(R.id.tvPhotoComment)

        imageView.setImageURI(photoUri)

        val existingComment = getCommentForImage(photoUri)
        if (!existingComment.isNullOrEmpty()) {
            commentText.text = "Comment: $existingComment"
            commentText.visibility = View.VISIBLE
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Close", null)
            .create()

        commentButton.setOnClickListener {
            showCommentDialog(photoUri) {
                commentText.text = "Comment: $it"
                commentText.visibility = View.VISIBLE
            }
        }
        saveToRecent(photoUri)


        dialog.show()
    }
    private fun showCommentDialog(photoUri: Uri, onCommentSaved: (String) -> Unit) {
        val input = EditText(requireContext())
        input.hint = "Enter your comment"
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

        AlertDialog.Builder(requireContext())
            .setTitle("Add Comment")
            .setView(input)
            .setPositiveButton("Submit") { _, _ ->
                val comment = input.text.toString()
                saveCommentForImage(photoUri, comment)
                Toast.makeText(requireContext(), "Comment saved", Toast.LENGTH_SHORT).show()
                onCommentSaved(comment)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun saveCommentForImage(photoUri: Uri, comment: String) {
        val prefs = requireContext().getSharedPreferences("photo_comments", 0)
        val editor = prefs.edit()
        val key = photoUri.lastPathSegment ?: return
        editor.putString(key, comment)
        editor.apply()
    }

    private fun getCommentForImage(photoUri: Uri): String? {
        val prefs = requireContext().getSharedPreferences("photo_comments", 0)
        val key = photoUri.lastPathSegment ?: return null
        return prefs.getString(key, null)
    }
}