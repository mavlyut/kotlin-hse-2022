package homework03.json.topic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
internal data class DiscussionInfo(@JsonProperty("data") val data: DiscussionData) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    internal data class DiscussionData(@JsonProperty("children") val children: List<DiscussionChild>) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        internal data class DiscussionChild(@JsonProperty("data") val data: Discussion)
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Discussion(
    @JsonProperty("id") val id: String,
    @JsonProperty("permalink") val permalink: String,
    @JsonProperty("author") val author: String,
    @JsonProperty("created") val created: Long,
    @JsonProperty("ups") val ups: Int,
    @JsonProperty("downs") val downs: Int,
    @JsonProperty("title") val title: String,
    @JsonProperty("selftext") val selftext: String?,
    @JsonProperty("selftext_html") val selftextHTML: String?
)
