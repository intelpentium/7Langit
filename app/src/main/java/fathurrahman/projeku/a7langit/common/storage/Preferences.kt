package fathurrahman.projeku.a7langit.common.storage

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(private val prefs: SharedPreferences) {

    var userid: String by PreferenceData(prefs, "userid", "")
    var username: String by PreferenceData(prefs, "username", "")
    var logintype: String by PreferenceData(prefs, "logintype", "")
    var commid: String by PreferenceData(prefs, "commid", "")
    var token: String by PreferenceData(prefs, "token", "")
    var status: String by PreferenceData(prefs, "status", "")
    var distributor: String by PreferenceData(prefs, "distributor", "")
    var cabang: String by PreferenceData(prefs, "cabang", "")
    var nama: String by PreferenceData(prefs, "nama", "")
    var kode: String by PreferenceData(prefs, "kode", "")
    var alamat: String by PreferenceData(prefs, "alamat", "")

    fun clear() {
        prefs.edit().clear().apply()
    }
}