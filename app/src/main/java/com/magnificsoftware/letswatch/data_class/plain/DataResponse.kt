package com.magnificsoftware.letswatch.data_class.plain

/**
 * A common interface for serialized response data received from an [ApiUrls.main] HTTP API call
 * @property status Whether the response data received has data for successful response
 * @property message The message which describes response result's outcome. Shows error message if response has error.
 * */
interface DataResponse {
    val status: Boolean
    val message: String
}