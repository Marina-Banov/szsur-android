package hr.uniri.szsur.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import hr.uniri.szsur.R
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.databinding.FragmentHomeBinding
import hr.uniri.szsur.ui.MainFragmentDirections
import hr.uniri.szsur.util.CreateNotification


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var userRepository = UserRepository.getInstance(FirebaseFirestore.getInstance())
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val RECEIVE_NOTIFICATIONS = "RECEIVE_NOTIFICATIONS"
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel

        binding.searchFilter.selectedTags.observe(viewLifecycleOwner, {
            viewModel.updateEvents(it)
        })

        binding.searchFilter.searchQuery.observe(viewLifecycleOwner, {
            viewModel.updateEvents(it)
        })

        binding.homeRecyclerView.adapter = HomeAdapter (
            {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToEventDetailsFragment(it)
                )
            },
            this::sendNotification
        )

        userRepository.user.observe(viewLifecycleOwner, {
            binding.homeRecyclerView.adapter!!.notifyDataSetChanged()
        })

        return binding.root
    }

    private fun sendNotification() {
        if (sharedPreferences.getBoolean(RECEIVE_NOTIFICATIONS, true)) {
            CreateNotification.createNotificationChannel(activity)
        }
    }
}



