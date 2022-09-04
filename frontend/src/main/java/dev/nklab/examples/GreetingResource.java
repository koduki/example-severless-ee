package dev.nklab.examples;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import javax.ws.rs.client.ClientBuilder;
import java.net.http.*;
import java.net.URI;
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

@Path("/hello")
public class GreetingResource {

    public static class LoginResponse {
        public final String userId;
        public final String token;

        public LoginResponse(String userId, String token) {
            this.userId = userId;
            this.token = token;
        }
    }

    @Inject
    JsonWebToken jwt;

    @Inject
    FrancophoneService service;

    @Inject
    DistributedTracer dt;

    @GET()
    @Path("permit-all")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@Context SecurityContext ctx, @Context HttpHeaders headers)
            throws IOException, java.lang.InterruptedException {
        var context = dt.getContext(headers);
        try (Scope scope = context.makeCurrent()) {
            Span span = dt.getTracer().spanBuilder("call say hello8").startSpan();
            try {
                System.out.println(service.bonjour());
                var client = HttpClient.newHttpClient();
                var request = HttpRequest
                        .newBuilder(URI.create("https://backend-q2m6ttsgja-du.a.run.app/hello2/permit-all"))
                        // .header("accept", "application/json")
                        .GET();
                dt.inject(context, request);
                var response = client.send(request.build(), HttpResponse.BodyHandlers.ofString());

                // the response:
                System.out.println(response.body());
            } finally {
                span.end();
            }
        }

        return getResponseString(ctx);
    }

    @GET
    @Path("roles-allowed")
    @RolesAllowed({ "User", "Admin" })
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRolesAllowed(@Context SecurityContext ctx) {
        return getResponseString(ctx) + ", birthdate: " + jwt.getClaim("birthdate").toString();
    }

    private String getResponseString(SecurityContext ctx) {
        String name;
        if (ctx.getUserPrincipal() == null) {
            name = "anonymous";
        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
        } else {
            name = ctx.getUserPrincipal().getName();
        }
        return String.format("hello8 + %s,"
                + " isHttps: %s,"
                + " authScheme: %s,"
                + " hasJWT: %s",
                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }
}