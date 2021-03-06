package io.javabrains.moviecatalogservice.resources;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    //gets @bean from app + @Autowired
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){



        //get all rated movie IDs
        //puting object Rating in List
        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/"+ userId, UserRating.class );

        return ratings.getUserRating().stream().map(rating-> {

                Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+ rating.getMovieId(),Movie.class);

//webclient example                Movie movie = webClientBuilder.build()
//                                .get()
//                                .uri("http://localhost:8082/movies/"+ rating.getMovieId())
//                                .retrieve().bodyToMono(Movie.class).block();

                //body to mono -> convert to movie instance

                return new CatalogItem(movie.getName(), "Test", rating.getRating());

                }).collect(Collectors.toList());

        //For each movie ID, call movie info service and get details

        //put them all together

    }
}
