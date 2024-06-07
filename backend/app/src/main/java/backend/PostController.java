package backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostRepository postRepository;

    @PostMapping("/posts")
    public ResponseEntity<Post> addPost(@RequestParam("title") String title,
                                        @RequestParam("content") String content,
                                        @RequestParam("username") String username,
                                        @RequestParam("images") List<MultipartFile> images) {
        try {
            List<PostImage> postImages = new ArrayList<>();
            for (MultipartFile file : images) {
                // 将文件内容转换为 Base64 字符串
                String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

                PostImage postImage = new PostImage();
                postImage.setImageUrl(base64Image);
                postImages.add(postImage);
            }

            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);
            post.setUsername(username);
            post.setImages(postImages);

            Post savedPost = postRepository.save(post);
            logger.info("Post saved successfully: {}", savedPost);
            return ResponseEntity.ok(savedPost);
        } catch (IOException e) {
            logger.error("Error saving post", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(posts);
    }
}
