package onetoone;

import onetoone.Messages.MessagesRepository;
import onetoone.Reviews.ReviewsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import onetoone.Movies.MoviesRepository;
import onetoone.Users.UsersRepository;
import onetoone.Friends.FriendsRepository;
import onetoone.Genres.GenresRepository;
import onetoone.Links.LinksRepository;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Author: Chris Costa and Matt Graham
 */

@SpringBootApplication
@EnableSwagger2
@EnableJpaRepositories
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /**
     * 
     * @param usersRepository repository for the User entity
     * @param moviesRepository repository for the Movies entity
     * @param linksRepository repository for the Links entity
     * @param genresRepository repository for the Genres entity
     * @param reviewsRepository repository for the Reviews Entity
     * @param friendsRepository repository for the Friends entity
     * @param messagesRepository repository for the Messages entity
     * Creates a commandLine runner to enter dummy data into the database
     **/
    @Bean
    CommandLineRunner init(UsersRepository usersRepository, MoviesRepository moviesRepository,
                           LinksRepository linksRepository, GenresRepository genresRepository,
                           ReviewsRepository reviewsRepository, FriendsRepository friendsRepository,
                           MessagesRepository messagesRepository) {
        return args -> {
        };
    }
}
