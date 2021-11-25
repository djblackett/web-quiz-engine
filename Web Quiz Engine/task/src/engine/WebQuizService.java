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
public class WebQuizService {

    private final WebQuizRepository webQuizRepository;

    @Autowired
    public WebQuizService(WebQuizRepository webQuizRepository) {
        this.webQuizRepository = webQuizRepository;
    }

    List<WebQuiz> getAllQuizzes() {
        return webQuizRepository.findAll();
    }

    WebQuiz newWebQuiz(WebQuiz webQuiz) {
        webQuizRepository.save(webQuiz);
        return webQuiz;
    }

    void deleteQuiz(int id) {
        webQuizRepository.deleteById(id);
    }

    WebQuiz findById(int id) {
        return webQuizRepository.findById(id);
    }

    public Page<WebQuiz> getAllQuizzes(Integer page, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(page, pageSize, Sort.by(sortBy));
        return webQuizRepository.findAll(paging);
    }

}
