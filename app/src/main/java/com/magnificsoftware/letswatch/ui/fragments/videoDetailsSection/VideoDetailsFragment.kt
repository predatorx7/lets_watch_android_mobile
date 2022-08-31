package com.magnificsoftware.letswatch.ui.fragments.videoDetailsSection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.magnificsoftware.letswatch.R
import com.magnificsoftware.letswatch.databinding.VideoDetailsBinding

class VideoDetailsFragment() : Fragment(R.layout.video_details) {
    private var _binding: VideoDetailsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VideoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.videoName.text = "video.name"
        binding.videoShortDescription.text = "video.description"
        binding.videoDescription.text = "video.longDescription"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}