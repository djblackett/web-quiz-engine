package engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompletedQuizRepository extends PagingAndSortingRepository<CompletedQuiz, Integer> {

    Page<CompletedQuiz> findCompletedQuizzesByUsername(String username, Pageable pageable);

}
