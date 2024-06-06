package backend;

/**
 * @author : hyb
 * @date ：2024-06-05 - 23:37
 * @desc :
 */
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
