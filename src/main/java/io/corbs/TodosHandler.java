package io.corbs;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class TodosHandler {

    private Map<Integer, Todo> todos = new HashMap<>();

    private static Integer seq = 0;

    public Mono<ServerResponse> listTodos(ServerRequest request) {
        Flux<Todo> response = Flux.fromIterable(todos.values());
        return ok().contentType(MediaType.APPLICATION_JSON).body(response, Todo.class);
    }

    public Mono<ServerResponse> createTodo(ServerRequest request) {
        Mono<Todo> todo = request.bodyToMono(Todo.class);
        Mono<Todo> result = todo.map(it -> {
            it.setId(seq++);
            return it;
        }).map(it -> {
            todos.put(it.getId(), it);
            return it;
        }).map(it -> todos.get(it.getId()));
        return ok().contentType(MediaType.APPLICATION_JSON).body(result, Todo.class);
    }

    public Mono<ServerResponse> clean(ServerRequest request) {
        todos.clear();
        return ok().build();
    }

    public Mono<ServerResponse> getTodo(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return ok().contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(todos.get(id)), Todo.class);
    }

    public Mono<ServerResponse> remove(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        todos.remove(id);
        return ok().build();
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        Mono<Todo> todo = request.bodyToMono(Todo.class);
        if(!todos.containsKey(id)) {
            return ok().build();
        }
        Todo old = todos.get(id);
        Mono<Todo> result = todo.map(it -> {
            old.setCompleted(it.getCompleted());
            return it;
        }).map(it -> {
            if(!StringUtils.isEmpty(it.getTitle())){
                old.setTitle(it.getTitle());
            }
            return it;
        }).map(it -> {
            if(it.getOrder() > -1) {
                old.setOrder(it.getOrder());
            }
            return old;
        });
        return ok().contentType(MediaType.APPLICATION_JSON).body(result, Todo.class);
    }
}
