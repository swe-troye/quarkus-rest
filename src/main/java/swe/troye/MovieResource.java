package swe.troye;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import swe.troye.entities.Movie;

@Path("movies")
@Tag(name = "Movie Resource", description = "Movie RESTful APIs")
public class MovieResource {

    public static List<Movie> movies = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getMovies", summary = "Get Movies", description = "Get all movies inside the list")
    @APIResponse(responseCode = "200", description = "Operation completed", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response getMovies() {
        return Response.ok(movies).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(operationId = "getMovie", summary = "Get Movie", description = "Get a movie inside the list")
    @APIResponse(responseCode = "200", description = "Operation completed", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response getMovie(@PathParam("id") Long id) {
        Optional<Movie> movieToFind = movies.stream().filter(movie -> movie.getId().equals(id)).findFirst();

        return movieToFind.isPresent() ? Response.ok(movieToFind.get()).build()
                : Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(operationId = "countMovies", summary = "Count Movies", description = "Size of the movie list")
    @APIResponse(responseCode = "200", description = "Operation completed", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @Path("size")
    public Integer countMovies() {
        return movies.size();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "createMovies", summary = "Create a new Movie", description = "Create a new movie to add to the list")
    @APIResponse(responseCode = "200", description = "Movie created", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response createMovie(
            @RequestBody(description = "Movie to create", required = true, content = @Content(schema = @Schema(implementation = Movie.class))) Movie movie) {
        movies.add(movie);
        return Response.status(Response.Status.CREATED).entity(movies).build();
    }
    // @POST
    // @Produces(MediaType.APPLICATION_JSON)
    // @Consumes(MediaType.APPLICATION_JSON)
    // public Response createMovie(Movie movie) {
    // movies.add(movie);
    // return Response.ok(movies).build();
    // }

    @PUT
    @Path("{id}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "updatetMovies", summary = "Update Movies", description = "Update movies inside the list")
    @APIResponse(responseCode = "200", description = "Movie updated", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response updateMovie(
            @Parameter(description = "Movie id", required = true) @PathParam("id") Long id,
            @Parameter(description = "Movie title", required = true) @PathParam("title") String title) {

        movies = movies.stream().map(movie -> {
            if (movie.getId().equals(id)) {
                movie.setTitle(title);
            }

            return movie;
        }).collect(Collectors.toList());

        return Response.ok(movies).build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Operation(operationId = "deleteMovies", summary = "Delete an existing Movie", description = "Delete a movies inside the list")
    @APIResponse(responseCode = "204", description = "Movie deleted", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "400", description = "Movie not valid", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    public Response deleteMovie(@PathParam("id") Long id) {
        Optional<Movie> movieToDelete = movies.stream().filter(movie -> movie.getId().equals(id)).findFirst();

        boolean removed = false;
        if (movieToDelete.isPresent()) {
            removed = movies.remove(movieToDelete.get());
        }

        return removed ? Response.noContent().build() : Response.status(Response.Status.BAD_REQUEST).build();
    }

}
