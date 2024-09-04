package com.vsp.socialnest.utils

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadImage(uri: Uri, folderName: String, callback: (String?) -> Unit) {
    var imageUrl: String? = null
    FirebaseStorage.getInstance()
        .getReference(folderName)
        .child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                callback(imageUrl)
            }
        }
}

fun uploadVideo(
    uri: Uri,
    folderName: String,
    progressDialog: ProgressDialog,
    callback: (String?) -> Unit
) {
    var imageUrl: String? = null

    // Use the passed ProgressDialog instance
    progressDialog.setTitle("Uploading...")
    progressDialog.setCancelable(false) // Optional: prevents dismissing the dialog by pressing back button
    progressDialog.show()

    FirebaseStorage.getInstance()
        .getReference(folderName)
        .child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { uri ->
                imageUrl = uri.toString()
                progressDialog.dismiss() // Dismiss the progress dialog
                callback(imageUrl)
            }
        }
        .addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
            progressDialog.setMessage("Uploaded $progress%") // Set progress message
        }
        .addOnFailureListener {
            progressDialog.dismiss() // Dismiss the dialog in case of failure
            callback(null) // Notify callback of failure
        }
}
