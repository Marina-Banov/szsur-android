package hr.uniri.szsur.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import hr.uniri.szsur.R
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.databinding.FragmentHomeBinding
import hr.uniri.szsur.ui.MainFragmentDirections
import hr.uniri.szsur.util.CreateNotification
import hr.uniri.szsur.util.SharedPreferenceUtils


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    companion object {
        private const val RECEIVE_NOTIFICATIONS = "RECEIVE_NOTIFICATIONS"
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this

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

        UserRepository.user.observe(viewLifecycleOwner, {
            binding.homeRecyclerView.adapter!!.notifyDataSetChanged()
        })

        return binding.root
    }

    private fun sendNotification() {
        if (SharedPreferenceUtils.getBoolean(RECEIVE_NOTIFICATIONS, true) == true) {
            CreateNotification.createNotificationChannel(activity)
        }
    }
}



