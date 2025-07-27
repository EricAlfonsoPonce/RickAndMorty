package es.ericalfonsoponce.presentation.compose.navigation

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.toRoute
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        bundle.getString(key)?.let<String, T>(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = Uri.encode(json.encodeToString(value))

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}
sealed class Screens{

    @Serializable
    data object HomeScreen: Screens()

    @Serializable
    data class CharacterDetailScreen(val character: CharacterShow): Screens() {
        companion object {
            val typeMap = mapOf(
                typeOf<CharacterShow>() to serializableType<CharacterShow>())

            fun from(savedStateHandle: SavedStateHandle) =
                savedStateHandle.toRoute<CharacterDetailScreen>(typeMap)
        }
    }
}