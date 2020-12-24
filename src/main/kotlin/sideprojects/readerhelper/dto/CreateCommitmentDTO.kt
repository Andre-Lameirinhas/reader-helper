package sideprojects.readerhelper.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp

data class CreateCommitmentDTO(
    @JsonProperty("book_title")
    val bookTitle: String,
    @JsonProperty("book_total_pages")
    val bookTotalPages: Int,
    @JsonProperty("deadline")
    val deadline: Timestamp
)
