package backend;

/**
 * @author : hyb
 * @date ：2024-06-05 - 23:39
 * @desc :
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/posts")
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        postRepository.save(post);
        return ResponseEntity.ok(post);
    }
}
