package ru.job4j.github.analysis.dto.rest.responses;

import org.junit.jupiter.api.Test;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GitCommitResponseTest {

    @Test
    void toEntityMapsFieldsAndRepository() {
        GitCommitResponse r = new GitCommitResponse();
        r.setSha("abc123");
        GitCommitResponse.CommitData cd = new GitCommitResponse.CommitData();
        GitCommitResponse.AuthorData ad = new GitCommitResponse.AuthorData();
        ad.setName("Jane");
        OffsetDateTime dt = OffsetDateTime.parse("2026-01-02T15:30:00Z");
        ad.setDate(dt);
        cd.setMessage("fix bug");
        cd.setAuthor(ad);
        r.setCommit(cd);

        Repository repo = new Repository();
        repo.setId(5L);
        repo.setName("r");
        repo.setUrl("https://github.com/o/r");

        Commit entity = r.toEntity(repo);

        assertEquals("abc123", entity.getSha());
        assertEquals("fix bug", entity.getMessage());
        assertEquals("Jane", entity.getAuthor());
        assertEquals(dt.toLocalDateTime(), entity.getDate());
        assertEquals(5L, entity.getRepository().getId());
    }
}
