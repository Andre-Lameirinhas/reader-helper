package sideprojects.readerhelper.controllers

import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import sideprojects.readerhelper.domain.Commitment
import sideprojects.readerhelper.domain.CommitmentStatus
import sideprojects.readerhelper.dto.CreateCommitmentDTO
import java.sql.Timestamp
import java.time.Instant

@SpringBootTest
@Rollback
@FlywayTest
class CommitmentControllerTests {

    @Autowired
    private lateinit var commitmentController: CommitmentController

    private companion object {
        const val USER_ID = 0

        val CREATE_COMMITMENT_DTO = CreateCommitmentDTO(
            bookTitle = "test_book",
            bookTotalPages = 100,
            deadline = Timestamp.valueOf("2030-12-26 01:19:51")
        )
    }

    @FlywayTest(invokeCleanDB = true)
    @Test
    fun `Returns status 201 when starting a commitment without another commitment in progress`() {
        val result = commitmentController.start(USER_ID, CREATE_COMMITMENT_DTO)

        assertEquals(201, result.statusCodeValue)
    }

    @Test
    fun `Returns the commitment started when starting a commitment without another commitment in progress`() {
        val result = commitmentController.start(USER_ID, CREATE_COMMITMENT_DTO)

        val createdCommitment = Commitment(
            id = 0,
            userId = 0,
            status = CommitmentStatus.IN_PROGRESS,
            bookTitle = "test_book",
            bookTotalPages = 100,
            bookCurrentPage = 0,
            startDate = Timestamp.from(Instant.now()),
            deadline = Timestamp.valueOf("2030-12-26 01:19:51")
        )

        assertEquals(createdCommitment, result.body)
    }

    @Test
    fun `Returns status 400 when starting a commitment with another commitment in progress`() {
        val result = commitmentController.start(USER_ID, CREATE_COMMITMENT_DTO)

        assertEquals(400, result.statusCodeValue)
    }
}
