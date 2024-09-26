package tosbik.ao.parayonetimi.data.model

sealed class AppState<T> {
    class Loading<T> : AppState<T>()
    data class Success<T>(val data: T) : AppState<T>()
    data class Error<T>(val message: String) : AppState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> error(message: String) = Error<T>(message)
    }
}