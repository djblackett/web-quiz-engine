package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompletedQuizService {

    private final CompletedQuizRepository completedQuizRepository;

    @Autowired
    public CompletedQuizService(CompletedQuizRepository completedQuizRepository) {
        this.completedQuizRepository = completedQuizRepository;
    }

    public void saveCompletedQuiz(CompletedQuiz completedQuiz) {
        completedQuizRepository.save(completedQuiz);
    }

    public Page<CompletedQuiz> getAllCompletedQuizzes(String username, int page, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy).descending());


        return completedQuizRepository.findCompletedQuizzesByUsername(username, paging);
    }
}

