package org.wm.auth.controller;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.util.WebUtils.ERROR_EXCEPTION_ATTRIBUTE;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/08/26 15:49
 * @since 1.0
**/
@RestController
@RequestMapping("/sample")
public class SampleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

    private static final List<String> PEOPLE = Arrays.asList("suzy", "mike");

    private final ObservationRegistry registry;

    private final Tracer tracer;

    SampleController(ObservationRegistry registry, Tracer tracer) {
        this.registry = registry;
        this.tracer = tracer;
    }

    @GetMapping("/")
    public String span() {
        String traceId = this.tracer.currentSpan().context().traceId();
        LOGGER.info("<ACCEPTANCE_TEST> <TRACE:{}> Hello from producer", traceId);
        return traceId;
    }

    @GetMapping("/trouble")
    String trouble() {
        LOGGER.info("<TEST_MARKER> 3,2,1... Boom!");
        throw new IllegalStateException("Noooooo!");
    }

    @GetMapping("/people")
    List<String> allPeople() {
        return Observation.createNotStarted("allPeople", registry).observe(slowDown(() -> PEOPLE));
    }

    @GetMapping("/greet/{name}")
    Map<String, String> greet(@PathVariable String name) {
        Observation observation = Observation.createNotStarted("greeting", registry).start();
        try (Observation.Scope scope = observation.openScope()) {
            if (foundByName(name)) {
                // only 2 names are valid (low cardinality)
                observation.lowCardinalityKeyValue("greeting.name", name);
                observation.event(Observation.Event.of("greeted"));
                return fetchDataSlowly(() -> Map.of("greeted", name));
            }
            else {
                observation.lowCardinalityKeyValue("greeting.name", "N/A");
                observation.highCardinalityKeyValue("greeting.name", name);
                observation.event(Observation.Event.of("failed"));
                throw new IllegalArgumentException("Invalid name!");
            }
        }
        catch (Exception exception) {
            observation.error(exception);
            throw exception;
        }
        finally {
            observation.stop();
        }
    }

    @ExceptionHandler(Throwable.class)
    ProblemDetail handleThrowable(HttpServletRequest request, Throwable error) {
        LOGGER.error(error.toString());
        request.setAttribute(ERROR_EXCEPTION_ATTRIBUTE, error);

        ProblemDetail problemDetail = ProblemDetail.forStatus(INTERNAL_SERVER_ERROR);
        problemDetail.setTitle(INTERNAL_SERVER_ERROR.getReasonPhrase());
        problemDetail.setDetail(error.toString());

        return problemDetail;
    }

    private boolean foundByName(String name) {
        return PEOPLE.contains(name);
    }

    private <T> Supplier<T> slowDown(Supplier<T> supplier) {
        return () -> {
            try {
                LOGGER.info("<TEST_MARKER> Fetching the data");
                if (Math.random() < 0.02) { // huge latency, less frequent
                    Thread.sleep(1_000);
                }
                Thread.sleep(((int) (Math.random() * 100)) + 100); // +base latency
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return supplier.get();
        };
    }

    private <T> T fetchDataSlowly(Supplier<T> supplier) {
        return slowDown(supplier).get();
    }

}
