package net.sistransito.mobile.ait

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rey.material.widget.CheckBox
import net.sistransito.R
import net.sistransito.mobile.database.DatabaseCreator
import net.sistransito.mobile.database.PrepopulatedDBOpenHelper
import net.sistransito.mobile.database.SetttingDatabaseHelper
import net.sistransito.mobile.fragment.BasePickerFragment
import net.sistransito.mobile.utility.Routine
import java.io.IOException
import java.text.Normalizer
import java.util.*

class TabAitAddressFragment : BasePickerFragment() {

    private var view: View? = null
    private lateinit var cityAutoComplete: AutoCompleteTextView
    private lateinit var aitData: AitData
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

    private var cityArray: List<String> = listOf()
    private var codeArray: List<String> = listOf()
    private var stateArray: List<String> = listOf()
    private lateinit var cityAdapter: ArrayAdapter<String>

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        @JvmStatic
        fun newInstance() = TabAitAddressFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.ait_address_fragment, null, false)
        initializedView()
        getAitObject()

        addPicker(R.id.btn_ait_pj_date, "date")
        addPicker(R.id.btn_ait_pj_time, "time")

        return view
    }

    private fun initializedView() {
        initializeViews()
        setInitialVisibility()
        setupListeners()
        setCityAutoComplete()
    }

    private fun initializeViews() {
        view?.let { v ->
            llCityState = v.findViewById(R.id.ll_auto_state)
            cityAutoComplete = v.findViewById(R.id.auto_complete_city)
            etCityCode = v.findViewById(R.id.et_auto_city_code)
            etState = v.findViewById(R.id.et_auto_address_state)
            etAddressInfraction = v.findViewById(R.id.et_ait_address)
            etAitDate = v.findViewById(R.id.btn_ait_pj_date)
            etAitTime = v.findViewById(R.id.btn_ait_pj_time)
            tvSaveData = v.findViewById(R.id.ait_fab)
            cbConfirm = v.findViewById(R.id.cb_ait_confirm)
            tvLocationGps = v.findViewById(R.id.tv_location_gps)
            tvClearData = v.findViewById(R.id.tv_clear_data)
            tvClearAddress = v.findViewById(R.id.tv_clear_address)
        }
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
            tvSaveData to View.OnClickListener { saveDataIfValid() },
            etAitDate to this@TabAitAddressFragment,
            etAitTime to this@TabAitAddressFragment
        )

        listeners.forEach { (view, listener) -> view.setOnClickListener(listener) }
        etAddressInfraction.addTextChangedListener(ChangeText(R.id.et_ait_address))

    }

    private fun toggleSaveDataVisibility() {
        tvSaveData.visibility = if (cbConfirm.isChecked) View.VISIBLE else View.GONE
        if (cbConfirm.isChecked) {
            Routine.closeKeyboard(tvSaveData, activity)
        }
    }

    private fun saveDataIfValid() {
        if (checkInput()) {
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
            updateCityFields(cityName)

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
        val thoroughfare = address.thoroughfare // Rua
        val subThoroughfare = address.subThoroughfare // Número
        val subLocality = address.subLocality // Bairro
        val featureName = address.featureName // Ponto de referência (fallback)

        val addressText = StringBuilder()

        // Adiciona a rua
        if (!thoroughfare.isNullOrEmpty()) {
            addressText.append(thoroughfare.trim())
        } else if (!featureName.isNullOrEmpty()) {
            addressText.append(featureName.trim())
        } else {
            return "Endereço não identificado"
        }

        // Adiciona o número, se não estiver duplicado
        if (!subThoroughfare.isNullOrEmpty() && !thoroughfare.contains(subThoroughfare)) {
            addressText.append(", ").append(subThoroughfare.trim())
        }

        // Adiciona o bairro
        val neighborhood = if (!subLocality.isNullOrEmpty()) subLocality.trim()
        else if (!featureName.isNullOrEmpty() && featureName != subThoroughfare) featureName.trim()
        else null

        if (!neighborhood.isNullOrEmpty() && !addressText.toString().contains(neighborhood)) {
            addressText.append(" - ").append(neighborhood)
        }

        val finalAddress = addressText.toString()
        Log.d("AddressText", "Endereço final: $finalAddress")
        return finalAddress
    }

    private fun updateAddressFields(addressText: String, cityName: String) {
        etAddressInfraction.setText(addressText)
        cityAutoComplete.setText(cityName)
        cityAutoComplete.dismissDropDown()
        aitData.city = cityName
    }

    private fun updateCityFields(cityName: String) {
        val position = cityArray.indexOf(cityName)
        if (position != -1) {
            aitData.cityCode = codeArray[position]
            etCityCode.setText(aitData.cityCode)
            aitData.state = stateArray[position]
            etState.setText(aitData.state)
            setCityStateVisibility(true)
        } else {
            Log.d("Cidade", "Cidade não encontrada na lista: $cityName")
            showToast("Cidade não está na lista pré-definida")
            clearCityFields()
            setCityStateVisibility(false)
        }
    }

    private fun clearCityFields() {
        etCityCode.setText("")
        etState.setText("")
        aitData.cityCode = ""
        aitData.state = ""
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

    private fun setCityAutoComplete() {
        cityArray = mutableListOf()
        codeArray = mutableListOf()
        stateArray = mutableListOf()

        val cursor = DatabaseCreator.getSettingDatabaseAdapter(activity).settingCursor
        val stateColumnIndex = cursor.getColumnIndex(SetttingDatabaseHelper.SETTING_STATE)
        val stateCity = if (stateColumnIndex != -1) cursor.getString(stateColumnIndex) else null

        val myCursor = if (stateCity != null) {
            DatabaseCreator.getPrepopulatedDBOpenHelper(activity).getInitialsStateCursor(stateCity)
        } else {
            DatabaseCreator.getPrepopulatedDBOpenHelper(activity).cityCursor
        }

        myCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val cityNameIndex = cursor.getColumnIndex(PrepopulatedDBOpenHelper.CITY_NAME)
                val stateIndex = cursor.getColumnIndex(PrepopulatedDBOpenHelper.STATE)
                val cityCodeIndex = cursor.getColumnIndex(PrepopulatedDBOpenHelper.CITY_CODE)

                do {
                    if (cityNameIndex != -1) {
                        (cityArray as MutableList).add(cursor.getString(cityNameIndex))
                    }
                    if (stateIndex != -1) {
                        (stateArray as MutableList).add(cursor.getString(stateIndex))
                    }
                    if (cityCodeIndex != -1) {
                        (codeArray as MutableList).add(cursor.getString(cityCodeIndex))
                    }
                } while (cursor.moveToNext())
            }
        }

        cityAdapter = ArrayAdapter(requireContext(), R.layout.custom_autocompletar, R.id.autoCompleteItem, cityArray)
        cityAutoComplete.setAdapter(cityAdapter)

        cityAutoComplete.setOnItemClickListener { _, _, position, _ ->
            aitData.city = cityArray[position]
            val realPosition = cityArray.indexOf(cityArray[position])

            aitData.cityCode = codeArray[realPosition]
            etCityCode.setText(aitData.cityCode)

            aitData.state = stateArray[realPosition]
            etState.setText(aitData.state)

            setCityStateVisibility(true)
        }
    }

    private fun checkInput(): Boolean {
        if (!isEditTextValid(etAddressInfraction, R.string.alert_insert_address)) return false
        if (!isEditTextValid(cityAutoComplete, R.string.alert_insert_city_name)) return false
        if (!isEditTextValid(etAitDate, R.string.alert_insert_date)) return false
        if (!isEditTextValid(etAitTime, R.string.alert_insert_time)) return false
        return true
    }

    private fun isEditTextValid(editText: EditText, errorMessageResId: Int): Boolean {
        if (editText == null) return false

        val text = editText.text.toString()
        if (TextUtils.isEmpty(text)) {
            val errorMessage = resources.getString(errorMessageResId)
            editText.error = errorMessage
            editText.requestFocus()
            return false
        }
        return true
    }

    private fun getAitObject() {
        aitData = AitObject.getAitData()

        if (aitData.isStoreFullData) {
            getRecomandedUpdate()
            setNewAitView()
        } else {
            initializedSelectetItems()
        }
    }

    private fun initializedSelectetItems() {
        // Implementação vazia conforme original
    }

    private fun getRecomandedUpdate() {
        etCityCode.setText(aitData.cityCode)
        etState.setText(aitData.state)
        etAddressInfraction.setText(aitData.address)
    }

    private inner class ChangeText(private val id: Int) : TextWatcher {
        override fun afterTextChanged(edit: Editable) {
            val s = edit.toString().trim()
            if (id == R.id.et_ait_address) {
                aitData.address = s
            }
        }

        override fun beforeTextChanged(arg0: CharSequence?, arg1: Int, arg2: Int, arg3: Int) {}
        override fun onTextChanged(arg0: CharSequence?, arg1: Int, arg2: Int, arg3: Int) {}
    }

    override fun handlePickerResult(selectedValue: String, viewId: Int) {
        when (viewId) {
            R.id.btn_ait_pj_date -> {
                aitData.aitDate = selectedValue
                etAitDate.setText(aitData.aitDate)
            }
            R.id.btn_ait_pj_time -> {
                aitData.aitTime = selectedValue
                etAitTime.setText(aitData.aitTime)
            }
        }
    }

    private fun setNewAitView() {
        cityAutoComplete.visibility = AutoCompleteTextView.GONE
        etCityCode.visibility = EditText.GONE
        etState.visibility = EditText.GONE
        etAitDate.visibility = Button.GONE
        etAitTime.visibility = Button.GONE
    }
} 