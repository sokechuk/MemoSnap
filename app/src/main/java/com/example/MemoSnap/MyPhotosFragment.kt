package com.example.MemoSnap.ui
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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

        val files = requireContext().filesDir.listFiles()
        val recentFiles = files?.sortedByDescending { it.lastModified() }?.take(20)

        recentFiles?.forEach { file ->
            val imageView = ImageView(requireContext())
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            imageView.setImageBitmap(bitmap)
            val size = resources.displayMetrics.widthPixels / 4 - 16 // adjust for padding/margin
            val params = ViewGroup.MarginLayoutParams(size, size)
            params.setMargins(8, 8, 8, 8)
            imageView.layoutParams = params

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
}