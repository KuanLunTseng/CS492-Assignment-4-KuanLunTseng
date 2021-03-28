package com.kuanluntseng.cs_492_weather

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kuanluntseng.cs_492_weather.data.FavoriteLocation
import com.kuanluntseng.cs_492_weather.data.LoadingStatus
import com.kuanluntseng.cs_492_weather.data.Weather
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(),
    WeatherAdapter.OnSearchClickListener,
    SharedPreferences.OnSharedPreferenceChangeListener,
    AddLocationDialogFragment.AddLocationDialogListener,
    FavoriteLocationsAdapter.OnLocationClickListener {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var searchProgressBar: ProgressBar
    private lateinit var weatherRecyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var favoriteLocationsRecyclerView: RecyclerView
    private lateinit var favoriteLocationsAdapter: FavoriteLocationsAdapter
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var favoriteLocationsViewModel: FavoriteLocationsViewModel
    private lateinit var addLocationTextView: TextView
    private var isFavorited: Boolean = false
    private lateinit var menu: Menu
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchProgressBar = findViewById(R.id.serach_progressbar)
        drawerLayout = findViewById(R.id.drawer_layout)

        weatherRecyclerView = findViewById(R.id.search_recyclerview)
        weatherAdapter = WeatherAdapter(this)
        weatherRecyclerView.adapter = weatherAdapter
        weatherRecyclerView.layoutManager = LinearLayoutManager(this)

        favoriteLocationsRecyclerView = findViewById(R.id.favorite_location_recyclerview)
        favoriteLocationsAdapter = FavoriteLocationsAdapter(this)
        favoriteLocationsRecyclerView.adapter = favoriteLocationsAdapter
        favoriteLocationsRecyclerView.layoutManager = LinearLayoutManager(this)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        addLocationTextView = findViewById(R.id.add_location_button)

        addLocationTextView.setOnClickListener {
            showAddLocationDialog()
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        favoriteLocationsViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))
                .get(FavoriteLocationsViewModel::class.java)

        favoriteLocationsViewModel.getAllFavoriteLocations().observe(this,
            { favoriteLocationsAdapter.updateFavoriteLocations(it) })

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        weatherViewModel.getSearchResults().observe(this,
            {
                val units = getUnits()
                weatherAdapter.updateSearchResults(it, units!!)
            })

        weatherViewModel.getLoadingStatus().observe(this,
            { loadingStatus ->
                when (loadingStatus) {
                    LoadingStatus.LOADING -> searchProgressBar.visibility = VISIBLE
                    LoadingStatus.SUCCESS -> {
                        searchProgressBar.visibility = INVISIBLE
                        weatherRecyclerView.visibility = VISIBLE
                    }
                    else -> {
                        searchProgressBar.visibility = INVISIBLE
                        weatherRecyclerView.visibility = INVISIBLE
                        Toast.makeText(
                            this@MainActivity,
                            "Connection is not stable...",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
        doWeatherSearch()
    }

    fun showAddLocationDialog() {
        val dialog = AddLocationDialogFragment(this)
        dialog.show(supportFragmentManager, "AddLocationDialogFragment")
    }

    private fun doWeatherSearch(): Unit {
        val location = getLocation()
        val units = getUnits()
        title = location
        weatherViewModel.loadSearchResults(location!!, units!!)
    }

    fun getLocation(): String {
        val location = sharedPreferences.getString(
            "pref_location",
            getString(R.string.pref_location_default)
        )
        return location.toString()
    }

    fun getUnits(): String {
        val units = sharedPreferences.getString(
            "pref_units",
            getString(R.string.pref_units_default)
        )
        return units.toString()
    }

    fun getCurrentTime(): String {
        val rawDate = DateTimeFormatter
            .ofPattern("MM-dd HH:mm")
            .withZone(ZoneOffset.ofHours(-8))
            .format(Instant.now())
        return rawDate
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.weather_main, menu)
        this.menu = menu
        updateMenu()
        return true;
    }

    fun updateMenu() {
        favoriteLocationsViewModel.getFavoriteLocationByName(getLocation())
            .observe(this) {
                val menuItem = menu.findItem(R.id.action_add_favorite)
                when (it) {
                    null -> {
                        isFavorited = false
                        menuItem.setIcon(R.drawable.ic_bookmark_border)
                    }
                    else -> {
                        isFavorited = true
                        menuItem.setIcon(R.drawable.ic_bookmark_added)
                    }
                }
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_map -> {
                viewForecastCityInMap()
                true
            }
            R.id.action_add_favorite -> {
                toggleFavoriteLocation(item)
                true
            }
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun toggleFavoriteLocation(menuItem: MenuItem) {
        menuItem?.let {
            isFavorited = !isFavorited
            menuItem.setChecked(isFavorited)
            val favoriteLocation = FavoriteLocation(getLocation(), getCurrentTime())
            when (isFavorited) {
                true -> {
                    menuItem.setIcon(R.drawable.ic_bookmark_added)
                    favoriteLocationsViewModel.insertFavoriteLocation(favoriteLocation)
                }
                else -> {
                    menuItem.setIcon(R.drawable.ic_bookmark_border)
                    favoriteLocationsViewModel.deleteFavoriteLocation(favoriteLocation)
                }
            }
        }
    }

    override fun onSearchClicked(location: String, weather: Weather) {
        val intent = Intent(this, WeatherDetailActivity::class.java)
        intent.putExtra(WeatherDetailActivity.EXTRA_WEATHER, weather)
        intent.putExtra(WeatherDetailActivity.EXTRA_WEATHER_LOCATION, location)
        startActivity(intent)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.d(
            TAG,
            "Shared preference changed, key: " + key + ", value: " + sharedPreferences?.getString(
                key, ""
            )
        )
        val favoriteLocation = FavoriteLocation(getLocation(), getCurrentTime())
        favoriteLocationsViewModel.insertFavoriteLocation(favoriteLocation)
        doWeatherSearch()
        updateMenu()
    }

    override fun onDestroy() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }

    fun viewForecastCityInMap() {
        val location = getLocation()
        val gmmIntentUri = Uri.parse(String.format("geo:0,0?q=%s", location))
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        try {
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.action_map_error), Toast.LENGTH_LONG).show()
        }
    }

    override fun onDialogPositiveClick(location: String) {
        val favoriteLocation = FavoriteLocation(location, getCurrentTime())
        favoriteLocationsViewModel.insertFavoriteLocation(favoriteLocation)
        drawerLayout.closeDrawers()
        val editor = sharedPreferences.edit()
        editor.putString("pref_location", location)
        editor.apply()
        doWeatherSearch()
    }

    override fun onLocationClicked(location: String) {
        drawerLayout.closeDrawers()
        val editor = sharedPreferences.edit()
        editor.putString("pref_location", location)
        editor.apply()
        doWeatherSearch()
    }
}
