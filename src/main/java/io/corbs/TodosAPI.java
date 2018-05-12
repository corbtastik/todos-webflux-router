package io.corbs;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class TodosAPI {
    @Bean
    public RouterFunction<ServerResponse> routes(TodosHandler handler) {
        return route(GET("/"), handler::listTodos)
                .andRoute(POST("/"), handler::createTodo)
                .andRoute(DELETE("/"), handler::clean)
                .andRoute(GET("/{id}"), handler::getTodo)
                .andRoute(PATCH("/{id}"), handler::update)
                .andRoute(DELETE("/{id}"), handler::remove);
    }
}
