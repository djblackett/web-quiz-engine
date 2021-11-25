package engine;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import engine.user.User;
import engine.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;


@RestController
public class WebQuizEngineController {

    Gson gson = new Gson();

    private final WebQuizService webQuizService;
    private final UserService userService;
    private final CompletedQuizService completedQuizService;

    @Autowired
    public WebQuizEngineController(WebQuizService webQuizService, UserService userService, CompletedQuizService completedQuizService) {
        this.webQuizService = webQuizService;
        this.userService = userService;
        this.completedQuizService = completedQuizService;
    }


    @PostMapping("api/quizzes/{id}/solve")
    ResponseEntity<String> submitAnswer(@PathVariable("id") int id, @RequestBody String body) {
        //List<Integer> answer;
        JsonObject obj1 = JsonParser.parseString(body).getAsJsonObject();
        Type listType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        List<Integer> answer = new Gson().fromJson(obj1.get("answer"), listType);
        JsonElement element = obj1.get("answer");
        if (element.isJsonArray()) {
            System.out.println("is json array");
        }
        //System.out.println("Original answer array: " + Arrays.toString(answer));
        try {
            WebQuiz currentQuiz = webQuizService.findById(id);
            System.out.println(currentQuiz.toString());

            if (answer == null) {
                answer = new ArrayList<>();
            }
            if (currentQuiz.getAnswer() == null) {
                currentQuiz.setAnswer(new ArrayList<>());
            }

            List<Integer> answerList = new ArrayList<>(answer);

            Collections.sort(answerList);
            List<Integer> sortedQuizAnswers = currentQuiz.getAnswer();
            Collections.sort(sortedQuizAnswers);
            JsonObject obj;
            System.out.println("Actual quiz answers: " + sortedQuizAnswers);
            System.out.println("Submitted answer: " + answerList);
            if (answerList.equals(sortedQuizAnswers)) {
                obj = JsonParser.parseString("{\"success\":true,\"feedback\":\"Congratulations, you're right!\"}").getAsJsonObject();

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication.getName();
                //User currentUser = userService.getUser(username).get();
                //currentUser.getCompletedQuizzes().add();
                completedQuizService.saveCompletedQuiz(new CompletedQuiz(currentQuiz.getId(), username, LocalDateTime.now()));



            } else {
                obj = JsonParser.parseString("{\"success\":false,\"feedback\":\"Wrong answer! Please, try again.\"}").getAsJsonObject();
            }

            return new ResponseEntity<>(obj.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/api/quizzes")
    WebQuiz createWebQuiz(@RequestBody String body) {
        try {
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            System.out.println(json.toString());
            //json.remove("id");

            WebQuiz quiz = gson.fromJson(json.toString(), WebQuiz.class);
            //quizzes.add(quiz);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            quiz.setUsername(currentPrincipalName);
            System.out.println(quiz.toString());
            return webQuizService.newWebQuiz(quiz);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/quizzes/{id}")
    ResponseEntity<WebQuiz> getQuizById(@PathVariable int id) {
        try {
            //int quizId = Integer.parseInt(id);
            WebQuiz quiz = webQuizService.findById(id);
            if (quiz != null) {
                return new ResponseEntity<>(quiz, HttpStatus.OK);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/quizzes")
    ResponseEntity<Page<WebQuiz>> getAllQuizzes(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        Page<WebQuiz> list = webQuizService.getAllQuizzes(page, pageSize, sortBy);

        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }




    @DeleteMapping("/api/quizzes/{id}")
    ResponseEntity<String> deleteQuiz(@PathVariable(value = "id") int id) {

        try {
            WebQuiz currentQuiz = webQuizService.findById(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();

            if (currentPrincipalName.equals(currentQuiz.getUsername())) {
                webQuizService.deleteQuiz(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/quizzes/completed")
    ResponseEntity<Page<CompletedQuiz>> getAllCompletedQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "completedAt") String sortBy)
    {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        //User currentUser = userService.getUser(username).get();
        Page<CompletedQuiz> list = completedQuizService.getAllCompletedQuizzes(username, page, pageSize, sortBy);

        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

}
