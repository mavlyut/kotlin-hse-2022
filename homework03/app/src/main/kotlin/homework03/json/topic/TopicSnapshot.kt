package homework03.json.topic

data class TopicSnapshot(
    val online: Int,
    val description: String,
    val dateCreated: Long,
    val discussions: List<Discussion>,
    val dateTaken: Long = System.currentTimeMillis()
)
