/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package homework03

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Task 4
fun main(vararg args: String) {
    val start = System.currentTimeMillis()
    runBlocking(Dispatchers.IO) {
        if (args.isEmpty()) {
            println("Usage: ...names\nNames of subreddits were expected (e.g. Kotlin, Cpp or Python)")
        }
        args.forEach {
            launch {
                RedditClient().parseSubreddit(it)
            }
        }
    }
    println((System.currentTimeMillis() - start) / 1000)
}
