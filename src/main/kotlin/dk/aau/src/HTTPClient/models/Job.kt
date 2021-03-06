/**
* Offloading
* Semester project for 7th semester at Aalborg University
*
* The version of the OpenAPI document: 1.0.0
* 
*
* NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
* https://openapi-generator.tech
* Do not edit the class manually.
*/
package org.openapitools.client.models


import com.squareup.moshi.Json

/**
 * 
 * @param id 
 * @param name 
 * @param jobpath 
 * @param timestamp 
 * @param status 
 * @param employer 
 * @param answersNeeded 
 * @param workersAssigned 
 * @param timeoutInMinutes 
 * @param confidenceLevel 
 */

data class Job (
    @Json(name = "id")
    val id: kotlin.Long? = null,
    @Json(name = "name")
    val name: kotlin.String? = null,
    @Json(name = "jobpath")
    val jobpath: kotlin.String? = null,
    @Json(name = "timestamp")
    val timestamp: kotlin.Long? = null,
    @Json(name = "status")
    val status: kotlin.String? = null,
    @Json(name = "employer")
    val employer: kotlin.String? = null,
    @Json(name = "answersNeeded")
    val answersNeeded: kotlin.Int? = null,
    @Json(name = "workersAssigned")
    val workersAssigned: kotlin.Int? = null,
    @Json(name = "timeoutInMinutes")
    val timeoutInMinutes: kotlin.Int? = null,
    @Json(name = "confidenceLevel")
    val confidenceLevel: kotlin.Double? = null
)

