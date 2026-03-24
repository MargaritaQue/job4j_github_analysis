package ru.job4j.github.analysis.dto.rest.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GitCommitResponse {

    @JsonProperty("commit")
    CommitData commit;

    @JsonProperty("sha")
    String sha;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class CommitData {
        @JsonProperty("message")
        private String message;
        @JsonProperty("author")
        private AuthorData author;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class AuthorData {
        @JsonProperty("name")
        private String name;
        @JsonProperty("date")
        private OffsetDateTime date;
    }

    public Commit toEntity(Repository repository) {
        Commit commit = new Commit();
        commit.setAuthor(this.commit.getAuthor().getName());
        commit.setDate(this.commit.getAuthor().getDate() != null
                ? this.commit.getAuthor().getDate().toLocalDateTime()
                : null);
        commit.setMessage(this.commit.getMessage());
        commit.setSha(this.getSha());
        commit.setRepository(repository);
        return commit;
    }
}
