package homework03

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.soywiz.korio.async.async
import homework03.RedditClientExceptions.*
import homework03.json.comment.CommentInfo
import homework03.json.comment.CommentsSnapshot
import homework03.json.comment.CommentsSnapshot.SingleComment
import homework03.json.topic.Discussion
import homework03.json.topic.DiscussionInfo
import homework03.json.topic.TopicInfo
import homework03.json.topic.TopicSnapshot
import homework03.serialization.serializeAndWrite
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.nio.channels.UnresolvedAddressException

sealed class RedditClientExceptions(message: String) : RuntimeException(message) {
    class AccessDeniedException(site: String) : RedditClientExceptions("Can't get request from $site")
    class NodeNotFoundException(node: String) : RedditClientExceptions("Node $node expected, but not found")
    class BadExitStatusException(exit: Int) : RedditClientExceptions("Invalid response received: $exit exit status")
}

class RedditClient {
    companion object {
        private val objectMapper = ObjectMapper()
        private val client = HttpClient(CIO) {
            HttpResponseValidator {
                validateResponse { response ->
                    if (response.status.value !in 200..299)
                        throw BadExitStatusException(response.status.value)
                }
            }
        }
        private const val domainName = "https://www.reddit.com"
    }

    private suspend fun request(link: String) = try {
        client.get("$link.json").body<String>()
    } catch (e: UnresolvedAddressException) {
        throw AccessDeniedException(link)
    }

    // Task 1
    suspend fun getTopic(name: String): TopicSnapshot = coroutineScope {
        val jsonParseJob = async {
            val jsonData = request("$domainName/r/$name/")
            objectMapper.readValue(jsonData, DiscussionInfo::class.java).data.children.map { it.data }
        }
        val aboutJsonJob = async {
            val aboutJson = request("$domainName/r/$name/about")
            objectMapper.readValue(aboutJson, TopicInfo::class.java).data
        }
        val discussions = jsonParseJob.await()
        val info = aboutJsonJob.await()
        TopicSnapshot(
            online = info.accountsActive,
            description = info.publicDescription,
            dateCreated = info.created,
            discussions = discussions
        )
    }

    // Task 2
    suspend fun getComments(title: String): CommentsSnapshot {
        val json = request(title)
        val tree = objectMapper.readTree(json)
        val children = tree[1]["data"]["children"] ?: throw NodeNotFoundException("data.children")
        val comments = ArrayList<SingleComment>()
        for (i in 0 until children.size())
            parseComment(children[i], comments)
        return CommentsSnapshot(
            comments = comments.toList(),
            postId = tree[0]["data"]["children"][0]["data"]["id"].asText()
        )
    }

    private fun parseComment(tree: JsonNode, finalList: ArrayList<SingleComment>, replyTo: String = "", depth: Int = 0) {
        val comment = objectMapper.treeToValue(tree, CommentInfo::class.java).data
        finalList.add(SingleComment(
            dateCreated = comment.created,
            votesFor = comment.ups,
            votesAgainst = comment.downs,
            text = comment.body,
            author = comment.author,
            replyTo = replyTo,
            depth = depth,
            id = comment.id
        ))
        val parentId = finalList.last().id
        if ((tree["data"]["replies"] ?: return).size() == 0)
            return
        val replies = tree["data"]["replies"]["data"]["children"] ?: throw NodeNotFoundException("replies.data.children")
        for (i in 0 until replies.size())
            parseComment(replies[i], finalList, parentId, depth + 1)
    }

    // Task 3
    suspend fun parseSubreddit(name: String) = coroutineScope {
        val discussions = getTopic(name).discussions
        launch {
            serializeAndWrite(data = discussions, klass = Discussion::class, file = "$name--subjects.csv")
        }
        launch {
            val comments = discussions.map { getComments("$domainName${it.permalink}").comments }.flatten()
            serializeAndWrite(data = comments, klass = SingleComment::class, file = "$name--comments.csv")
        }
    }
}
