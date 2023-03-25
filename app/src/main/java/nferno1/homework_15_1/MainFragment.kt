package nferno1.homework_15_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import nferno1.homework_15_1.databinding.FragmentMainBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION", "UNCHECKED_CAST")
class MainFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: MainFragmentViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val wordDAO: WordDAO =
                    (requireActivity().application as App).db.wordDao()
                return MainFragmentViewModel(wordDAO) as T
            }
        }
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bAdd.setOnClickListener {
            val newNumber = binding.newString.text.toString()
            lifecycleScope.launch {
                viewModel.onAddBtn(newNumber)
            }
        }
        binding.bClear.setOnClickListener {
            lifecycleScope.launch {
                viewModel.onCleanBtn()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.allWords.collect {
                binding.firstWords.text = it.joinToString(
                    separator = "\n"
                )
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.allWordsLiveData.observe(
                viewLifecycleOwner
            ) {
                binding.firstWords.text = it.joinToString(
                    separator = "\n"
                )
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                when (it) {
                    State.Loading -> {
                        binding.bAdd.isEnabled = false
                        binding.inputLayout.error = null
                    }
                    State.Success -> {
                        binding.bAdd.isEnabled = true
                        binding.inputLayout.error = null
                    }
                    is State.Error -> {
                        binding.bAdd.isEnabled = true
                        binding.inputLayout.error = it.msg

                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.error.collect {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}