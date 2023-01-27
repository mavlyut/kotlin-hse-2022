package homework03.json.topic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class TopicInfo(@JsonProperty("data") val data: TopicInfoData) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    internal data class TopicInfoData(
        @JsonProperty("accounts_active") val accountsActive: Int,
        @JsonProperty("public_description") val publicDescription: String,
        @JsonProperty("created") val created: Long
    )
}
