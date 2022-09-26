package dev.nklab.examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

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
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.context.propagation.*;
import io.opentelemetry.context.Context;

import javax.ws.rs.core.HttpHeaders;
import java.net.http.*;

@ApplicationScoped
class DistributedTracer {
    TextMapGetter<HttpHeaders> getter = new TextMapGetter<HttpHeaders>() {
        @Override
        public String get(HttpHeaders headers, String s) {
            assert headers != null;
            return headers.getHeaderString(s);
        }

        @Override
        public Iterable<String> keys(HttpHeaders headers) {
            var keys = new ArrayList<String>();
            var requestHeaders = headers.getRequestHeaders();
            requestHeaders.forEach((k, v) -> {
                keys.add(k);
            });
            return keys;
        }
    };

    TextMapSetter<HttpRequest.Builder> setter = new TextMapSetter<HttpRequest.Builder>() {
        @Override
        public void set(HttpRequest.Builder builder, String key, String value) {
            builder.header(key, value);
        }
    };

    private OpenTelemetry openTelemetry;

    void onStartup(@Observes StartupEvent event) throws IOException {
        // TraceExporter googleExporter = TraceExporter.createWithConfiguration(
        //         TraceConfiguration.builder().setProjectId("sandbox-svc-dev-8rra").build());

        // SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
        //         .addSpanProcessor(SimpleSpanProcessor.create(googleExporter))
        //         .build();

        // openTelemetry = OpenTelemetrySdk.builder()
        //         .setTracerProvider(tracerProvider)
        //         .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
        //         .buildAndRegisterGlobal();

    }

    public Tracer getTracer() {
        return openTelemetry.getTracer("io.opentelemetry.example.TraceExporterExample");
    }

    public Context getContext(HttpHeaders headers) {
        return openTelemetry.getPropagators().getTextMapPropagator()
                .extract(Context.current(), headers, getter);
    }

    public void inject(Context context, HttpRequest.Builder builder) {
        openTelemetry.getPropagators().getTextMapPropagator().inject(Context.current(), builder, setter);
    }
}