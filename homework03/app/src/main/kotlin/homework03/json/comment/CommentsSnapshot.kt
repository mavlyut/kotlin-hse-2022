package homework03.json.comment

data class CommentsSnapshot(
    val postId: String,
    val comments: List<SingleComment>,
    val dateTaken: Long = System.currentTimeMillis()
) {
    data class SingleComment(
        val id: String,
        val author: String?,
        val dateCreated: Long,
        val votesFor: Int,
        val votesAgainst: Int,
        val text: String?,
        val replyTo: String,
        val depth: Int
    )
}
