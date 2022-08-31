package com.magnificsoftware.letswatch.ui.component.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.databinding.DialogLayoutBinding

class LetsWatchDialog(
    private val title: String?,
    private val description: String?,
    private val primaryButtonData: DialogButtonData?,
    private val secondaryButtonData: DialogButtonData?,
) : DialogFragment(R.layout.dialog_layout) {

    private var _binding: DialogLayoutBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLayoutBinding.inflate(inflater, container, false)

        setupView()
        setupDialog()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun setupView() {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
    }

    private fun setupDialog() {
        if (title.isNullOrBlank()) {
            binding.dialogTitle.visibility = View.GONE
        } else {
            binding.dialogTitle.text = title
        }

        if (description.isNullOrBlank()) {
            binding.dialogDescription.visibility = View.GONE
        } else {
            binding.dialogDescription.text = description
        }

        if (primaryButtonData == null) {
            binding.btnPositive.visibility = View.GONE
        } else {
            binding.btnPositive.text = primaryButtonData.buttonText
            binding.btnPositive.setOnClickListener {
                dialog!!.dismiss()
                primaryButtonData.onClick?.invoke()
            }
        }

        if (secondaryButtonData == null) {
            binding.btnCancel.visibility = View.GONE
        } else {
            binding.btnCancel.text = secondaryButtonData.buttonText
            binding.btnCancel.setOnClickListener {
                dialog!!.dismiss()
                secondaryButtonData.onClick?.invoke()
            }
        }
    }
}