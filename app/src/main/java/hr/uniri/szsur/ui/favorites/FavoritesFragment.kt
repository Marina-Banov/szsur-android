package hr.uniri.szsur.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import hr.uniri.szsur.R
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.databinding.FragmentFavoritesBinding
import hr.uniri.szsur.ui.MainFragmentDirections
import hr.uniri.szsur.util.CreateNotification
import hr.uniri.szsur.util.SharedPreferenceUtils


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    companion object {
        private const val RECEIVE_NOTIFICATIONS = "RECEIVE_NOTIFICATIONS"
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        binding.lifecycleOwner = this

        val viewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
        binding.viewModel = viewModel

        binding.searchFilter.selectedTags.observe(viewLifecycleOwner, {
            viewModel.updateFavorites(it)
        })

        binding.searchFilter.searchQuery.observe(viewLifecycleOwner, {
            viewModel.updateFavorites(it)
        })

        UserRepository.user.observe(viewLifecycleOwner, {
            viewModel.filterFavorites()
            binding.favoritesRecyclerView.adapter!!.notifyDataSetChanged()
        })

        binding.favoritesRecyclerView.adapter = FavoritesAdapter({
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToEventDetailsFragment(it)
            )
        }, {
            if (it.published) {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToSurveyResultsDetailsFragment(it)
                )
            } else {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToSurveyDetailsFragment(it)
                )
            }
        }, this::sendNotification)

        return binding.root
    }

    private fun sendNotification() {
        if (SharedPreferenceUtils.getBoolean(RECEIVE_NOTIFICATIONS, true) == true) {
            CreateNotification.createNotificationChannel(activity)
        }
    }
}