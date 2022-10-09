package com.txd.library.picker.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.txd.library.picker.databinding.FragmentPickerBinding

/**
 *
 *
 * @ClassName:      PickerFragment
 * @CreateDate:     2022/9/19
 */
class PickerFragment : Fragment() {
    lateinit var binding: FragmentPickerBinding

    var title = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title = arguments?.getString(KEY_TITLE) ?: ""
        binding.tvPicker.text = title
    }

    companion object {
        const val KEY_TITLE = "KET_TITLE"

        fun newInstance(title: String): PickerFragment {
            val fragment = PickerFragment()
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            fragment.arguments = args
            return fragment
        }

    }

}