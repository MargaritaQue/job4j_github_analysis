package ru.job4j.github.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.job4j.github.analysis.model.Commit;

import java.util.List;

public interface CommitRepository extends JpaRepository<Commit, Long> {

    @Query("select c.sha from Commit c where c.repository.id = :repositoryId")
    List<String> findAllShaByRepositoryId(@Param("repositoryId") Long repositoryId);

    List<Commit> findAllByRepositoryId(Long repositoryId);
}
