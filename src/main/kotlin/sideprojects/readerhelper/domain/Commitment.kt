package sideprojects.readerhelper.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import java.sql.Timestamp
import java.util.Date

data class Commitment(
    @Id
    @JsonProperty("id")
    val id: Int? = null,
    @JsonProperty("user_id")
    val userId: Int,
    @JsonProperty("status")
    val status: CommitmentStatus = CommitmentStatus.IN_PROGRESS,
    @JsonProperty("book_title")
    val bookTitle: String,
    @JsonProperty("book_total_pages")
    val bookTotalPages: Int,
    @JsonProperty("book_current_page")
    val bookCurrentPage: Int = 0,
    @JsonProperty("start_date")
    val startDate: Timestamp = Timestamp(Date().time),
    @JsonProperty("deadline")
    val deadline: Timestamp
)

enum class CommitmentStatus {
    IN_PROGRESS, COMPLETED, FAILED, RESIGNED
}
