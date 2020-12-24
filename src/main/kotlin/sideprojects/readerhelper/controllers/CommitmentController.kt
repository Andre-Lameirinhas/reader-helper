package sideprojects.readerhelper.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sideprojects.readerhelper.domain.Commitment
import sideprojects.readerhelper.domain.CommitmentStatus
import sideprojects.readerhelper.dto.CreateCommitmentDTO
import sideprojects.readerhelper.repositories.CommitmentRepository

@RestController
@RequestMapping("/commitment")
class CommitmentController(
    private val commitmentRepository: CommitmentRepository
) {

    @GetMapping("/current")
    fun current(
        @RequestParam("user_id") userId: Int
    ): ResponseEntity<Commitment> {
        val currentCommitment = commitmentRepository.getCurrentCommitment(userId)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        return ResponseEntity.ok(currentCommitment)
    }

    @PostMapping("/start")
    fun start(
        @RequestParam("user_id") userId: Int,
        @RequestBody createCommitmentDTO: CreateCommitmentDTO
    ): ResponseEntity<Commitment> {
        if (hasCurrentCommitment(userId)) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val newCommitment = Commitment(
            userId = userId,
            bookTitle = createCommitmentDTO.bookTitle,
            bookTotalPages = createCommitmentDTO.bookTotalPages,
            deadline = createCommitmentDTO.deadline
        )

        val createdCommitment = commitmentRepository.save(newCommitment)

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommitment)
    }

    @PutMapping("/complete")
    fun complete(
        @RequestParam("user_id") userId: Int
    ): ResponseEntity<String> {
        val currentCommitment = commitmentRepository.getCurrentCommitment(userId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No current commitment")

        commitmentRepository.save(currentCommitment.copy(status = CommitmentStatus.COMPLETED))

        return ResponseEntity.ok("Commitment completed")
    }

    @PutMapping("/resign")
    fun resign(
        @RequestParam("user_id") userId: Int
    ): ResponseEntity<String> {
        val currentCommitment = commitmentRepository.getCurrentCommitment(userId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No current commitment")

        commitmentRepository.save(currentCommitment.copy(status = CommitmentStatus.RESIGNED))

        return ResponseEntity.ok("Commitment resigned")
    }

    @PutMapping("/update_page")
    fun updateCurrentPage(
        @RequestParam("user_id") userId: Int,
        @RequestParam("page") page: Int
    ): ResponseEntity<String> {
        val currentCommitment = commitmentRepository.getCurrentCommitment(userId)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No current commitment")

        if (page <= currentCommitment.bookCurrentPage || page > currentCommitment.bookTotalPages) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid page number")
        }

        commitmentRepository.save(currentCommitment.copy(bookCurrentPage = page))

        return ResponseEntity.ok("Current page updated")
    }

    @GetMapping("/history")
    fun history(
        @RequestParam("user_id") userId: Int
    ): ResponseEntity<List<Commitment>> {
        val commitmentHistory = commitmentRepository.listPastCommitments(userId)

        return ResponseEntity.ok(commitmentHistory)
    }

    private fun hasCurrentCommitment(userId: Int): Boolean {
        return commitmentRepository.getCurrentCommitment(userId) != null
    }
}
