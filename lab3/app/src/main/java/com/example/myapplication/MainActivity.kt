package com.example.myapplication
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import ksg.mso.filehelp.ScopedStorageHelper


class MainActivity : AppCompatActivity() {

    private lateinit var editTextFileContent: EditText
    private lateinit var textViewFilePath: TextView
    private lateinit var buttonOpenFile: Button
    private lateinit var buttonSaveFile: Button

    private var currentFileUri: Uri? = null
    private val scopedStorageHelper = ScopedStorageHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Ustawienie układu XML

        editTextFileContent = findViewById(R.id.editTextFileContent)
        textViewFilePath = findViewById(R.id.textViewFilePath)
        buttonOpenFile = findViewById(R.id.buttonOpenFile)
        buttonSaveFile = findViewById(R.id.buttonSaveFile)

        buttonOpenFile.setOnClickListener {
            scopedStorageHelper.pickFileToOpen(openFileLauncher, "text/plain")
        }

        buttonSaveFile.setOnClickListener {
            currentFileUri?.let {
                saveFile(it)
            } ?: pickFileToSave()
        }
    }

    private val openFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                uri?.let {
                    currentFileUri = it
                    textViewFilePath.text = "Plik: $it"
                    readAndDisplayFile(it)
                }
            }
        }


    private fun readAndDisplayFile(uri: Uri) {
        val contentResolver = contentResolver
        val fileContent = scopedStorageHelper.readFile(contentResolver, uri)
        editTextFileContent.setText(fileContent?.toString(Charsets.UTF_8))
    }


    private fun saveFile(uri: Uri) {
        val contentResolver = contentResolver
        val textToSave = editTextFileContent.text.toString().toByteArray()
        scopedStorageHelper.writeToFile(contentResolver, uri, textToSave)
    }

    // Uruchamianie zapisywania pliku, jeśli użytkownik nie otworzył jeszcze pliku
    private fun pickFileToSave() {
        scopedStorageHelper.pickFileToSave(saveFileLauncher, "text/plain", "nowy_plik.txt")
    }

    // Obsługa wyboru pliku do zapisu
    private val saveFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                uri?.let {
                    currentFileUri = it
                    textViewFilePath.text = "Plik zapisany: $it"
                    saveFile(it)
                }
            }
        }
}
