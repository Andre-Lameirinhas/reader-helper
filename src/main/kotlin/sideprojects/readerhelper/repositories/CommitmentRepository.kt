package sideprojects.readerhelper.repositories

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import sideprojects.readerhelper.domain.Commitment

@Repository
interface CommitmentRepository : CrudRepository<Commitment, Int> {

    @Query(
        """
        SELECT *
        FROM commitment
        WHERE user_id = :userId AND status = 'IN_PROGRESS'
    """
    )
    fun getCurrentCommitment(userId: Int): Commitment?

    @Query(
        """
        SELECT *
        FROM commitment
        WHERE user_id = :userId AND status != 'IN_PROGRESS'
    """
    )
    fun listPastCommitments(userId: Int): List<Commitment>
}
