package engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CompletedQuiz {

    private int id;

    @JsonIgnore
    private String username;

    @JsonIgnore
    @Id
    @GeneratedValue
    private int completedQuizId;

    private LocalDateTime completedAt;


    public CompletedQuiz(int id, String username, LocalDateTime completedAt) {
        this.id = id;
        this.username = username;
        this.completedAt = completedAt;
    }
}

