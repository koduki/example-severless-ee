package dev.nklab.examples;

import javax.enterprise.context.ApplicationScoped;

// import org.eclipse.microprofile.opentracing.Traced;

// @Traced
@ApplicationScoped
public class FrancophoneService {

    public String bonjour() {
   

        return "bonjour";
    }
}