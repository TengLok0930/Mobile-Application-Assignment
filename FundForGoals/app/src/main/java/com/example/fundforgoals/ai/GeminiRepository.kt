package com.example.fundforgoals.ai

import com.example.fundforgoals.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup

class GeminiRepository {

    private val client = HttpClient {
        expectSuccess = true

        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 15_000
            socketTimeoutMillis = 30_000
        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }
    }

    suspend fun generateOverview(details: String): String {
        val initialPrompt = buildOverviewPrompt(details)

        val initialResponse: GeminiResponse = client.post(
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent"
        ) {
            contentType(ContentType.Application.Json)
            header("x-goog-api-key", BuildConfig.GEMINI_API_KEY)

            setBody(
                GeminiRequest(
                    contents = listOf(
                        GeminiContent(
                            parts = listOf(
                                GeminiPart(text = initialPrompt)
                            )
                        )
                    ),
                    tools = listOf(
                        GeminiTool(
                            urlContext = GeminiUrlContext()
                        )
                    ),
                    generationConfig = GeminiGenerationConfig(
                        maxOutputTokens = 120,
                        temperature = 0.2
                    )
                )
            )
        }.body()

        val retrievalStatus = initialResponse.firstUrlRetrievalStatus()
        val generatedText = initialResponse.extractText()
            ?.trim()
            .orEmpty()

        if (retrievalStatus == "URL_RETRIEVAL_STATUS_SUCCESS" && generatedText.isNotBlank()) {
            return generatedText
        }

        val extractedUrl = extractFirstUrl(details)
        if (extractedUrl != null) {
            val websiteText = runCatching { fetchWebsiteText(extractedUrl) }.getOrNull()

            if (!websiteText.isNullOrBlank()) {
                val fallbackPrompt = buildFetchedContentPrompt(
                    details = details,
                    websiteUrl = extractedUrl,
                    websiteText = websiteText
                )

                val fallbackResponse: GeminiResponse = client.post(
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent"
                ) {
                    contentType(ContentType.Application.Json)
                    header("x-goog-api-key", BuildConfig.GEMINI_API_KEY)

                    setBody(
                        GeminiRequest(
                            contents = listOf(
                                GeminiContent(
                                    parts = listOf(
                                        GeminiPart(text = fallbackPrompt)
                                    )
                                )
                            ),
                            generationConfig = GeminiGenerationConfig(
                                maxOutputTokens = 120,
                                temperature = 0.2
                            )
                        )
                    )
                }.body()

                return fallbackResponse.extractText()
                    ?.trim()
                    ?.takeIf { it.isNotBlank() }
                    ?: fallbackSummary(details, retrievalStatus)
            }
        }

        return generatedText.ifBlank {
            fallbackSummary(details, retrievalStatus)
        }
    }

    private fun buildOverviewPrompt(details: String): String {
        return """
            You are reviewing a registration or approval request for FundForGoals.

            If the request contains a public URL:
            - Use accessible content from that URL when available.
            - Return 3 short bullet points explaining why the applicant appears suitable.
            - Do not claim the URL was inaccessible unless retrieval actually failed.

            If the request does not contain a public URL:
            - Summarize the request for admin review.

            Output rules:
            - Return exactly 3 bullet points.
            - Maximum 15 words per bullet point.
            - Do not add a title.
            - Use concise, neutral language.
            - Do not invent facts beyond the request text or accessible URL content.

            Request:
            $details
        """.trimIndent()
    }

    private fun buildFetchedContentPrompt(
        details: String,
        websiteUrl: String,
        websiteText: String
    ): String {
        return """
            You are reviewing a registration or approval request for FundForGoals.

            Gemini URL retrieval did not succeed automatically for this URL:
            $websiteUrl

            Use the fetched website text below together with the submitted request details.
            Return exactly 3 short bullet points explaining why the applicant appears suitable.
            Maximum 15 words per bullet.
            Do not add a title.
            Do not invent facts.

            Request details:
            $details

            Website text:
            $websiteText
        """.trimIndent()
    }

    private fun fallbackSummary(
        details: String,
        retrievalStatus: String?
    ): String {
        val statusText = retrievalStatus ?: "URL_RETRIEVAL_STATUS_UNSPECIFIED"
        return """
            - Automatic URL review failed with status $statusText.
            - Applicant details were reviewed from submitted request text.
            - Manual admin verification is recommended.
        """.trimIndent()
    }

    private fun extractFirstUrl(text: String): String? {
        val regex = Regex("""https?://[^\s)]+""")
        return regex.find(text)?.value
    }

    private suspend fun fetchWebsiteText(url: String): String {
        val html = client.get(url).bodyAsText()
        val document = Jsoup.parse(html)
        return document.body().text()
            .replace(Regex("\\s+"), " ")
            .trim()
            .take(6000)
    }
}

private fun GeminiResponse.extractText(): String? {
    return candidates
        .asSequence()
        .flatMap { candidate -> candidate.content.parts.asSequence() }
        .mapNotNull { it.text }
        .firstOrNull()
}

private fun GeminiResponse.firstUrlRetrievalStatus(): String? {
    return candidates
        .asSequence()
        .flatMap { it.urlContextMetadata?.urlMetadata.orEmpty().asSequence() }
        .mapNotNull { it.urlRetrievalStatus }
        .firstOrNull()
}

@Serializable
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val tools: List<GeminiTool>? = null,
    val generationConfig: GeminiGenerationConfig? = null
)

@Serializable
data class GeminiContent(
    val parts: List<GeminiPart>
)

@Serializable
data class GeminiPart(
    val text: String
)

@Serializable
data class GeminiTool(
    @SerialName("url_context")
    val urlContext: GeminiUrlContext? = null
)

@Serializable
class GeminiUrlContext

@Serializable
data class GeminiResponse(
    val candidates: List<GeminiCandidate> = emptyList()
)

@Serializable
data class GeminiCandidate(
    val content: GeminiCandidateContent = GeminiCandidateContent(),
    @SerialName("url_context_metadata")
    val urlContextMetadata: GeminiUrlContextMetadata? = null
)

@Serializable
data class GeminiCandidateContent(
    val parts: List<GeminiTextPart> = emptyList()
)

@Serializable
data class GeminiTextPart(
    val text: String? = null
)

@Serializable
data class GeminiUrlContextMetadata(
    @SerialName("url_metadata")
    val urlMetadata: List<GeminiUrlMetadata> = emptyList()
)

@Serializable
data class GeminiUrlMetadata(
    @SerialName("retrieved_url")
    val retrievedUrl: String? = null,
    @SerialName("url_retrieval_status")
    val urlRetrievalStatus: String? = null
)

@Serializable
data class GeminiGenerationConfig(
    @SerialName("maxOutputTokens")
    val maxOutputTokens: Int = 120,
    val temperature: Double = 0.2
)