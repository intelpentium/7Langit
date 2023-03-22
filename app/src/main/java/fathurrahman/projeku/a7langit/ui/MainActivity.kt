package fathurrahman.projeku.a7langit.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.location.*
import fathurrahman.projeku.a7langit.common.UiState
import fathurrahman.projeku.a7langit.databinding.ActivityMainBinding
import fathurrahman.projeku.a7langit.ext.getDateNow
import fathurrahman.projeku.a7langit.ext.hideKeyboard
import fathurrahman.projeku.a7langit.ext.observe
import fathurrahman.projeku.a7langit.ext.toast
import fathurrahman.projeku.a7langit.modules.base.BaseActivity
import fathurrahman.projeku.a7langit.services.entity.ResponseWeather
import fathurrahman.projeku.a7langit.services.entity.ResponseWeatherHourItem
import fathurrahman.projeku.a7langit.ui.adapter.AdapterMain
import fathurrahman.projeku.a7langit.viewmodel.MainViewModel
import fathurrahman.projeku.a7langit.viewmodel.RepoViewModel
import java.util.*

class MainActivity : BaseActivity(){
    lateinit var mBinding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private val viewModelRepo by viewModels<RepoViewModel>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE){
        AdapterMain(::onClick)
    }

    private val LOCATION_PERMISSION_CODE = 100
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initView()
        handleState()
        getLastLocation()
    }

    private fun initView() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mBinding.search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                getApi(mBinding.search.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }

        mBinding.ivSearch.setOnClickListener {
            hideKeyboard()
            getApi(mBinding.search.text.toString())
        }

        val favoriteActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    mBinding.search.setText(it.data?.getStringExtra("name").toString())
                    getApi(it.data?.getStringExtra("name").toString())
//                    fetchHomepageData()
                }
            }

        mBinding.ivFavorite.setOnClickListener {
            val intent = Intent(this, CityActivity::class.java)
//            intent.putExtra("type", BarJadwalRequest.kalibrasi)
            favoriteActivity.launch(intent)
        }


        mBinding.layoutMain.rv.adapter = adapter


        // buttonn favorite
        mBinding.btnFavorite.setOnClickListener {
            val name = mBinding.search.text.toString().lowercase()

            if (status != "") {
                viewModelRepo.updateFavorite(name, if (status == "1") "0" else "1")

                if(status == "1"){
                    toast("Favorite berhasil dihapus")
                    status = ""
                }else{
                    toast("Favorite berhasil ditambahkan")
                }
            }else{
                viewModelRepo.setFavorite(name, "1")
                toast("Favorite berhasil ditambahkan")
            }
        }


        // button refresh empty
        mBinding.layoutEmpty.btnSubmit.setOnClickListener {
            getApi(mBinding.search.text.toString())
        }
    }

    private fun handleState() {
        observe(viewModel.responseWeather){
            if(it != null){
                setData(it)
            }
        }

        observe(viewModel.responseWeatherHour){
            if(it != null){
                adapter.clearData()
                adapter.insertData(it.list)
            }
        }

        observe(viewModel.loadState){
            when (it){
                UiState.Loading -> mBinding.progress.progress.isVisible = true
                UiState.Success -> {
                    mBinding.progress.progress.isVisible = false

                    mBinding.layoutMain.root.isVisible = true
                    mBinding.layoutEmpty.root.isVisible = false
                }
                is UiState.Error -> {
                    mBinding.progress.progress.isVisible = false
//                    requireActivity().toast(it.message)
                    Log.e("BISMILLAH", it.message)

                    mBinding.layoutMain.root.isVisible = false
                    mBinding.layoutEmpty.root.isVisible = true

//                    if(it.message == "akses tidak sah" || it.message == "Unauthorized access"){
//                        viewModelRepo.logout()
//                        startActivity(Intent(requireActivity(), OnboardActivity::class.java))
//                    }else {
//                        mBinding.lineAbsen.isVisible = true
//                    }
                }
            }
        }
    }

    private fun setData(data: ResponseWeather) {

        val suhu = data.main.temp.toInt()

        mBinding.layoutMain.tvCityInfo.text = data.name
        mBinding.layoutMain.tvInfo.text = data.weather[0].description
        mBinding.layoutMain.tvSuhu.text = "$suhu \u2103"
        mBinding.layoutMain.tvKelembapan.text = data.main.humidity+" %"
        mBinding.layoutMain.tvAngin.text = data.wind.speed+" mtr/dtk"

        mBinding.layoutMain.tvDate.text = getDateNow()
    }

    private fun onClick(data: ResponseWeatherHourItem) {

    }

    private fun getApi(subdistrict: String){
        viewModel.weather(subdistrict)
        viewModel.weatherHour(subdistrict)

        mBinding.btnFavorite.setImageResource(fathurrahman.projeku.a7langit.R.drawable.ic_star2)

        getFavorite(subdistrict.lowercase())
    }



    private fun getFavorite(name: String){
        observe(viewModelRepo.getFavoriteName(name)) {
            if (it != null) {
                status = it.status
                mBinding.btnFavorite.setImageDrawable(getDrawable(
                    if (status == "1") fathurrahman.projeku.a7langit.R.drawable.ic_star
                    else fathurrahman.projeku.a7langit.R.drawable.ic_star2)
                )
            }else{
                status = ""
            }
        }
    }


    //    ============== Location ================
    private fun getData(lat: Double, lon: Double){

        val geocoder = Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(lat, lon, 1)
        val alamat = address[0].locality

        val subdistrict = alamat.replace("Kecamatan", "").trim().lowercase()

        mBinding.search.setText(subdistrict)

        getApi(subdistrict)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient!!.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        getData(location.latitude, location.longitude)
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()!!
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location: Location = locationResult.lastLocation
            getData(location.latitude, location.longitude)
//            Log.d("BISMILLAH2",location.latitude.toString())
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                assistant!!.start()
                getLastLocation()
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        getLastLocation()
//    }
}