package engine;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebQuizRepository extends PagingAndSortingRepository<WebQuiz, Integer> {

    List<WebQuiz> findAll();

    void deleteById(int id);

    WebQuiz findById(int id);


}
