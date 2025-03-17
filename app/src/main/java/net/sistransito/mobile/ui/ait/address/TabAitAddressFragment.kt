package net.sistransito.mobile.ui.ait.address

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rey.material.widget.CheckBox
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.sistransito.R
import net.sistransito.mobile.fragment.BasePickerFragment
import net.sistransito.mobile.utility.Routine
import java.io.IOException
import java.text.Normalizer
import java.util.*

@AndroidEntryPoint
class TabAitAddressFragment : BasePickerFragment() {

    private var _binding: View? = null
    private val binding get() = _binding!!
    
    private val viewModel: AitAddressViewModel by viewModels()
    
    private lateinit var cityAutoComplete: AutoCompleteTextView
    private lateinit var etAddressInfraction: EditText
    private lateinit var etCityCode: EditText
    private lateinit var etAitDate: EditText
    private lateinit var etAitTime: EditText
    private lateinit var etState: EditText
    private lateinit var llCityState: LinearLayout
    private lateinit var tvLocationGps: TextView
    private lateinit var tvClearAddress: TextView
    private lateinit var tvClearData: TextView
    private lateinit var tvSaveData: TextView
    private lateinit var cbConfirm: CheckBox

    private lateinit var cityAdapter: ArrayAdapter<String>

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        fun newInstance() = TabAitAddressFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflater.inflate(R.layout.ait_address_fragment, null, false)
        initializeViews()
        setupListeners()
        observeViewModel()
        return binding
    }

    private fun initializeViews() {
        binding.apply {
            llCityState = findViewById(R.id.ll_auto_state)
            cityAutoComplete = findViewById(R.id.auto_complete_city)
            etCityCode = findViewById(R.id.et_auto_city_code)
            etState = findViewById(R.id.et_auto_address_state)
            etAddressInfraction = findViewById(R.id.et_ait_address)
            etAitDate = findViewById(R.id.btn_ait_pj_date)
            etAitTime = findViewById(R.id.btn_ait_pj_time)
            tvSaveData = findViewById(R.id.ait_fab)
            cbConfirm = findViewById(R.id.cb_ait_confirm)
            tvLocationGps = findViewById(R.id.tv_location_gps)
            tvClearData = findViewById(R.id.tv_clear_data)
            tvClearAddress = findViewById(R.id.tv_clear_address)
        }
        setInitialVisibility()
    }

    private fun setInitialVisibility() {
        llCityState.visibility = LinearLayout.GONE
        tvClearAddress.visibility = View.GONE
    }

    private fun setupListeners() {
        val listeners = mapOf(
            tvLocationGps to View.OnClickListener { getLocationAndSetAddress() },
            tvClearAddress to View.OnClickListener { clearAddressFields() },
            tvClearData to View.OnClickListener { clearCityFieldsAndVisibility() },
            cbConfirm to View.OnClickListener { toggleSaveDataVisibility() },
            tvSaveData to View.OnClickListener { saveDataIfValid() }
        )

        listeners.forEach { (view, listener) -> view.setOnClickListener(listener) }

        etAddressInfraction.addTextChangedListener(ChangeText(R.id.et_ait_address))
        etAitDate.setOnClickListener { addPicker(R.id.btn_ait_pj_date, "date") }
        etAitTime.setOnClickListener { addPicker(R.id.btn_ait_pj_time, "time") }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    handleUiState(state)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cities.collectLatest { cities ->
                    updateCityAdapter(cities)
                }
            }
        }
    }

    private fun handleUiState(state: AitAddressUiState) {
        when (state) {
            is AitAddressUiState.Error -> showToast(state.message)
            is AitAddressUiState.CitySelected -> handleCitySelected(state.city)
            else -> Unit
        }
    }

    private fun updateCityAdapter(cities: List<CityEntity>) {
        val cityNames = cities.map { it.name }
        cityAdapter = ArrayAdapter(requireContext(), R.layout.custom_autocompletar, R.id.autoCompleteItem, cityNames)
        cityAutoComplete.setAdapter(cityAdapter)

        cityAutoComplete.setOnItemClickListener { _, _, position, _ ->
            cities.getOrNull(position)?.let { city ->
                viewModel.selectCity(city)
            }
        }
    }

    private fun toggleSaveDataVisibility() {
        tvSaveData.visibility = if (cbConfirm.isChecked) View.VISIBLE else View.GONE
        if (cbConfirm.isChecked) {
            Routine.closeKeyboard(tvSaveData, activity)
        }
    }

    private fun saveDataIfValid() {
        if (viewModel.validateInputs()) {
            if (!DatabaseCreator.getInfractionDatabaseAdapter(activity).updateAitDataAddress(aitData)) {
                Routine.showAlert(resources.getString(R.string.update_erro), activity)
            } else {
                (activity as? AitActivity)?.setTabActual(3)
            }
        }
    }

    private fun clearAddressFields() {
        clearEditText(etAddressInfraction)
        clearEditText(cityAutoComplete)
        tvClearAddress.visibility = View.GONE
        tvLocationGps.visibility = View.VISIBLE
        clearEditTextError(etAddressInfraction)
        setCityStateVisibility(false)
    }

    private fun clearCityFieldsAndVisibility() {
        clearEditText(cityAutoComplete)
        setCityStateVisibility(false)
    }

    private fun clearEditTextError(editText: EditText?) {
        editText?.error = null
    }

    private fun getLocationAndSetAddress() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGpsEnabled) {
            Toast.makeText(context, "GPS desativado", Toast.LENGTH_SHORT).show()
            return
        }

        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation
            .addOnSuccessListener(requireActivity()) { location ->
                if (location != null) {
                    setAddressFromLocation(location)
                } else {
                    Toast.makeText(context, "Localização não disponível", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setAddressFromLocation(location: Location) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses.isNullOrEmpty()) {
                showToast("Endereço não encontrado")
                return
            }

            val address = addresses[0]
            val cityName = determineCityName(address)
            val addressText = buildAddressText(address)

            updateAddressFields(addressText, cityName)
            viewModel.searchCities(cityName)

            toggleLocationViewsVisibility()
        } catch (e: IOException) {
            handleGeocoderError(e)
        }
    }

    private fun determineCityName(address: Address): String {
        var cityName = address.locality ?: address.subAdminArea ?: "CIDADE DESCONHECIDA"
        cityName = normalizeString(cityName).uppercase(Locale.getDefault())
        if (cityName.isEmpty()) {
            cityName = "CIDADE DESCONHECIDA"
        }
        logCityName(cityName)
        return cityName
    }

    private fun buildAddressText(address: Address): String {
        val thoroughfare = address.thoroughfare
        val subThoroughfare = address.subThoroughfare
        val subLocality = address.subLocality
        val featureName = address.featureName

        val addressText = StringBuilder()

        if (!thoroughfare.isNullOrEmpty()) {
            addressText.append(thoroughfare.trim())
        } else if (!featureName.isNullOrEmpty()) {
            addressText.append(featureName.trim())
        } else {
            return "Endereço não identificado"
        }

        if (!subThoroughfare.isNullOrEmpty() && !thoroughfare.contains(subThoroughfare)) {
            addressText.append(", ").append(subThoroughfare.trim())
        }

        val neighborhood = if (!subLocality.isNullOrEmpty()) subLocality.trim()
        else if (!featureName.isNullOrEmpty() && featureName != subThoroughfare) featureName.trim()
        else null

        if (!neighborhood.isNullOrEmpty() && !addressText.toString().contains(neighborhood)) {
            addressText.append(" - ").append(neighborhood)
        }

        return addressText.toString()
    }

    private fun updateAddressFields(addressText: String, cityName: String) {
        etAddressInfraction.setText(addressText)
        cityAutoComplete.setText(cityName)
        cityAutoComplete.dismissDropDown()
        viewModel.updateAddress(addressText)
    }

    private fun handleCitySelected(city: CityEntity) {
        etCityCode.setText(city.code)
        etState.setText(city.state)
        setCityStateVisibility(true)
    }

    private fun clearCityFields() {
        etCityCode.setText("")
        etState.setText("")
    }

    private fun toggleLocationViewsVisibility() {
        tvLocationGps.visibility = LinearLayout.GONE
        tvClearAddress.visibility = LinearLayout.VISIBLE
    }

    private fun logCityName(cityName: String) {
        Log.d("CidadeRaw", "Locality: $cityName")
        Log.d("CidadeNormalized", "Após normalização: $cityName")
        Log.d("CidadeFinal", "Após maiúsculas: $cityName")
        if (cityName.isEmpty()) {
            Log.e("CidadeErro", "cityName está vazio após processamento")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleGeocoderError(e: IOException) {
        e.printStackTrace()
        showToast("Erro ao obter endereço: ${e.message}")
    }

    private fun normalizeString(input: String?): String {
        if (input.isNullOrBlank()) return ""
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
        return normalized.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndSetAddress()
            } else {
                Toast.makeText(context, "Permissão de localização negada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearEditText(editText: EditText) {
        editText.setText("")
        editText.requestFocus()
    }

    private fun setCityStateVisibility(show: Boolean) {
        val visibility = if (show) LinearLayout.VISIBLE else LinearLayout.GONE
        llCityState.visibility = visibility
        if (show) {
            Routine.closeKeyboard(cityAutoComplete, activity)
            cityAutoComplete.requestFocus()
        } else {
            etCityCode.isEnabled = false
            etState.isEnabled = false
        }
    }

    private inner class ChangeText(private val id: Int) : TextWatcher {
        override fun afterTextChanged(edit: Editable) {
            val s = edit.toString().trim()
            if (id == R.id.et_ait_address) {
                viewModel.updateAddress(s)
            }
        }

        override fun beforeTextChanged(arg0: CharSequence?, arg1: Int, arg2: Int, arg3: Int) {}
        override fun onTextChanged(arg0: CharSequence?, arg1: Int, arg2: Int, arg3: Int) {}
    }

    override fun handlePickerResult(selectedValue: String, viewId: Int) {
        when (viewId) {
            R.id.btn_ait_pj_date -> {
                etAitDate.setText(selectedValue)
                viewModel.updateDateTime(selectedValue, etAitTime.text.toString())
            }
            R.id.btn_ait_pj_time -> {
                etAitTime.setText(selectedValue)
                viewModel.updateDateTime(etAitDate.text.toString(), selectedValue)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 