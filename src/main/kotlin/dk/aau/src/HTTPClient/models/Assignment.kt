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
 * @param jobid 
 * @param jobfile 
 */

data class Assignment (
    @Json(name = "jobid")
    val jobid: kotlin.Long? = null,
    @Json(name = "jobfile")
    val jobfile: kotlin.ByteArray? = null
)

