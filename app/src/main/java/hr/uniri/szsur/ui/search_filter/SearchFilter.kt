package hr.uniri.szsur.ui.search_filter

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.google.android.material.chip.Chip
import hr.uniri.szsur.R
import hr.uniri.szsur.data.repository.EnumsRepository
import hr.uniri.szsur.databinding.LayoutSearchFilterBinding
import hr.uniri.szsur.ui.settings.SettingsActivity
import java.util.ArrayList

class SearchFilter(ctx: Context, attributeSet: AttributeSet? = null):
    LinearLayout(ctx, attributeSet) {

    private var binding: LayoutSearchFilterBinding

    private val _selectedTags = MutableLiveData<ArrayList<String>>()
    val selectedTags: LiveData<ArrayList<String>>
        get() = _selectedTags

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String>
        get() = _searchQuery

    init {
        orientation = VERTICAL

        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_search_filter, this, true)
        binding.lifecycleOwner = ctx as LifecycleOwner

        binding.profileButton.setOnClickListener {
            val intent = Intent(ctx, SettingsActivity::class.java)
            startActivity(ctx, intent, null)
        }

        _selectedTags.value = ArrayList()

        EnumsRepository.tags.observe(ctx, Observer {

            binding.filterText.visibility = if (it.size == 0) GONE else VISIBLE

            for (tag in it) {
                val chip = inflater.inflate(R.layout.layout_chip, binding.filter, false) as Chip
                chip.text = tag
                chip.setOnClickListener {
                    _selectedTags.value?.apply {
                        if (chip.isChecked) { add(tag) } else { remove(tag) }
                    }
                    // Live data is not updated simply by updating the ArrayList
                    // Must also update the reference
                    _selectedTags.value = _selectedTags.value
                }
                binding.filter.addView(chip)
            }
        })

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                _searchQuery.value = query!!
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                _searchQuery.value = query!!
                binding.search.clearFocus()
                return true
            }
        })
    }
}