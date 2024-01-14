package com.catexplorer.data

import com.google.gson.annotations.SerializedName

data class CatBreedInfo(
    @SerializedName("weight")
    var weight: Weight?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("cfa_url")
    var cfa_url: String?,
    @SerializedName("vetstreet_url")
    var vetstreet_url: String?,
    @SerializedName("vcahospitals_url")
    var vcahospitals_url: String?,
    @SerializedName("temperament")
    var temperament: String?,
    @SerializedName("origin")
    var origin: String?,
    @SerializedName("country_codes")
    var country_codes: String?,
    @SerializedName("country_code")
    var country_code: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("life_span")
    var life_span: String?,
    @SerializedName("indoor")
    var indoor: Int?,
    @SerializedName("lap")
    var lap: Int?,
    @SerializedName("alt_names")
    var alt_names: String?,
    @SerializedName("adaptability")
    var adaptability: Int?,
    @SerializedName("affection_level")
    var affection_level: Int?,
    @SerializedName("child_friendly")
    var child_friendly: Int?,
    @SerializedName("dog_friendly")
    var dog_friendly: Int?,
    @SerializedName("energy_level")
    var energy_level: Int?,
    @SerializedName("grooming")
    var grooming: Int?,
    @SerializedName("health_issues")
    var health_issues: Int?,
    @SerializedName("intelligence")
    var intelligence: Int?,
    @SerializedName("shedding_level")
    var shedding_level: Int?,
    @SerializedName("social_needs")
    var social_needs: Int?,
    @SerializedName("stranger_friendly")
    var stranger_friendly: Int?,
    @SerializedName("vocalisation")
    var vocalisation: Int?,
    @SerializedName("experimental")
    var experimental: Int?,
    @SerializedName("hairless")
    var hairless: Int?,
    @SerializedName("natural")
    var natural: Int?,
    @SerializedName("rare")
    var rare: Int?,
    @SerializedName("rex")
    var rex: Int?,
    @SerializedName("suppressed_tail")
    var suppressed_tail: Int?,
    @SerializedName("short_legs")
    var short_legs: Int?,
    @SerializedName("wikipedia_url")
    var wikipedia_url: String?,
    @SerializedName("hypoallergenic")
    var hypoallergenic: Int?,
    @SerializedName("reference_image_id")
    var reference_image_id: String?
)

data class Weight(
    @SerializedName("imperial")
    var imperial: String?,
    @SerializedName("metric")
    var metric: String?
)