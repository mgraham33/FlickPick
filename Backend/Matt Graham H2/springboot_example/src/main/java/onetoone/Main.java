package onetoone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetoone.Movies.Movie;
import onetoone.Movies.MovieRepository;
import onetoone.Users.User;
import onetoone.Users.UserRepository;

@SpringBootApplication
//@EnableJpaRepositories
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users with their machines
    /**
     * 
     * @param userRepository repository for the User entity
     * @param movieRepository repository for the Laptop entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, MovieRepository movieRepository) {
        return args -> {
            User user1 = new User("John Doe", "john_doe@gmail.com");
            User user2 = new User("Jane Doe", "janed@hotmail.com");
            User user3 = new User("Bob Bill", "bb@outlook.com");
            Movie movie1 = new Movie( 15, 2, "Miracle", 2004, "Sport/Drama");
            Movie movie2 = new Movie( 50, 1, "Top Gun", 1986, "Action/Adventue");
            Movie movie3 = new Movie( 7, 2, "Jurassic Park", 1993, "Sci-fi/Action");
            user1.setMovie(movie1);
            user2.setMovie(movie2);
            user3.setMovie(movie3);
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

        };
    }

}
