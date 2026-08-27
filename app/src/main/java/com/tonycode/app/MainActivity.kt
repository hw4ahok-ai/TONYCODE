package com.tonycode.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var input: EditText
    private lateinit var password: EditText
    private lateinit var result: EditText
    private var decryptMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(8, 16, 17))
            setPadding(24, 24, 24, 24)
        }

        val scroll = ScrollView(this)

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val title = TextView(this).apply {
            text = "TONYCODE"
            textSize = 28f
            setTextColor(Color.WHITE)
        }

        content.addView(title)

        val subtitle = TextView(this).apply {
            text = "Ubah teks menjadi teks rahasia dan kembalikan lagi."
            textSize = 14f
            setTextColor(Color.LTGRAY)
        }

        content.addView(subtitle)

        val encryptButton = MaterialButton(this).apply {
            text = "CONVERT"
        }

        val decryptButton = MaterialButton(this).apply {
            text = "CONVERT BACK"
        }

        val modeRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        modeRow.addView(
            encryptButton,
            LinearLayout.LayoutParams(0, 140, 1f)
        )

        modeRow.addView(
            decryptButton,
            LinearLayout.LayoutParams(0, 140, 1f)
        )

        content.addView(modeRow)

        input = EditText(this).apply {
            hint = "Tulis atau paste teks di sini..."
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
            minHeight = 300
        }

        content.addView(input)

        password = EditText(this).apply {
            hint = "Password"
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)

            inputType =
                InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        content.addView(password)

        val actionButton = MaterialButton(this).apply {
            text = "CONVERT (ENCRYPT)"
        }

        content.addView(actionButton)

        result = EditText(this).apply {
            hint = "Hasil akan muncul di sini..."
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
            minHeight = 300
            isFocusable = false
            setTextIsSelectable(true)
        }

        content.addView(result)

        val copyButton = MaterialButton(this).apply {
            text = "COPY"
        }

        val pasteButton = MaterialButton(this).apply {
            text = "PASTE"
        }

        val shareButton = MaterialButton(this).apply {
            text = "SHARE"
        }

        val clearButton = MaterialButton(this).apply {
            text = "CLEAR"
        }

        content.addView(copyButton)
        content.addView(pasteButton)
        content.addView(shareButton)
        content.addView(clearButton)

        encryptButton.setOnClickListener {
            decryptMode = false
            actionButton.text = "CONVERT (ENCRYPT)"
        }

        decryptButton.setOnClickListener {
            decryptMode = true
            actionButton.text = "CONVERT BACK (DECRYPT)"
        }

        actionButton.setOnClickListener {
            try {
                val text = input.text.toString().trim()
                val key = password.text.toString()

                val output = if (decryptMode) {
                    Crypto.decrypt(text, key)
                } else {
                    Crypto.encrypt(text, key)
                }

                result.setText(output)

            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    e.message ?: "Operasi gagal.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        copyButton.setOnClickListener {
            val clipboard =
                getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager

            clipboard.setPrimaryClip(
                ClipData.newPlainText(
                    "TONYCODE",
                    result.text.toString()
                )
            )

            Toast.makeText(
                this,
                "Hasil disalin",
                Toast.LENGTH_SHORT
            ).show()
        }

        pasteButton.setOnClickListener {
            val clipboard =
                getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager

            val clip = clipboard.primaryClip

            if (clip != null && clip.itemCount > 0) {
                input.setText(
                    clip.getItemAt(0)
                        .coerceToText(this)
                )
            }
        }

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"

                putExtra(
                    Intent.EXTRA_TEXT,
                    result.text.toString()
                )
            }

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Bagikan TONYCODE"
                )
            )
        }

        clearButton.setOnClickListener {
            input.text.clear()
            password.text.clear()
            result.text.clear()
        }

        scroll.addView(content)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
    }
}
