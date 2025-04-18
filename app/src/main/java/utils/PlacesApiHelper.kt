package utils

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

suspend fun fetchNearbyGasStations(lat: Double, lng: Double): List<Pair<String, LatLng>> {
    val apiKey = "AIzaSyBKk-8iCt2J07BkRXk9Bk3bzQu8gC1eZ2U"
    val radius = 3000
    val type = "gas_station"
    val urlStr =
        "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$lat,$lng&radius=$radius&type=$type&key=$apiKey"

    return withContext(Dispatchers.IO) {
        val result = mutableListOf<Pair<String, LatLng>>()
        try {
            val response = URL(urlStr).readText()
            val json = JSONObject(response)
            val results = json.getJSONArray("results")

            for (i in 0 until results.length()) {
                val item = results.getJSONObject(i)
                val name = item.getString("name")
                val location = item.getJSONObject("geometry").getJSONObject("location")
                val latResult = location.getDouble("lat")
                val lngResult = location.getDouble("lng")
                result.add(name to LatLng(latResult, lngResult))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        result
    }
}
