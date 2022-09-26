package dev.nklab.examples;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.google.cloud.opentelemetry.trace.*;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.context.propagation.*;

import dev.nklab.examples.dto.*;

@Path("/api")
public class FirestoreResource {

    @Inject
    PersonService service;

    @Inject
    ArticleService articleService;

    // @Inject
    // DistributedTracer dt;
    @GET()
    @Path("firestore")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String firestore(@Context SecurityContext ctx, @Context HttpHeaders headers)
            throws IOException, ExecutionException, InterruptedException {
        // var context = dt.getContext(headers);
        var msg = "";
        // try (Scope scope = context.makeCurrent()) {
        // Span span = dt.getTracer().spanBuilder("call firestore").startSpan();
        // try {
        // msg = service.run();
        // } finally {
        // span.end();
        // }
        // }

        return msg;
    }

    @GET()
    @Path("post")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String post(@Context SecurityContext ctx, @Context HttpHeaders headers)
            throws IOException, ExecutionException, InterruptedException {
        var msg = articleService.post();
        return msg;
    }

    @GET()
    @Path("comment/{parentid}")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String comment(@PathParam("parentid") final String parentid)
            throws IOException, ExecutionException, InterruptedException {
        var msg = articleService.reply(parentid);
        return msg;
    }

    @GET()
    @Path("list/{user}")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public List<ArticleDTO> list(@PathParam("user") final String user)
            throws IOException, ExecutionException, InterruptedException {
        var msg = articleService.list(user);
        return msg;
    }

    @GET()
    @Path("search")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public List<ArticleDTO> search()
            throws IOException, ExecutionException, InterruptedException {
        var msg = articleService.search();
        return msg;
    }

    @GET()
    @Path("tags")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> tags()
            throws IOException, ExecutionException, InterruptedException {
        var msg = articleService.tags();
        return msg;
    }
}
