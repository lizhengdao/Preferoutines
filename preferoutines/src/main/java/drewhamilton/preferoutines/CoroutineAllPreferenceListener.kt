package drewhamilton.preferoutines

import android.content.SharedPreferences
import kotlinx.coroutines.channels.SendChannel

internal class CoroutineAllPreferenceListener(
    override val channel: SendChannel<Map<String, *>>
) : CoroutinePreferenceListener<Map<String, *>> {

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        channel.offer(sharedPreferences.all)
    }
}
