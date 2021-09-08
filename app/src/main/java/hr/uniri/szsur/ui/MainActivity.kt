package hr.uniri.szsur.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.api.Places
import hr.uniri.szsur.R
import hr.uniri.szsur.data.repository.PlacesRepository


class MainActivity : BaseThemeActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(applicationContext, getString(R.string.google_maps_key))
        PlacesRepository.client = Places.createClient(this)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContentView(R.layout.activity_main)
    }

}