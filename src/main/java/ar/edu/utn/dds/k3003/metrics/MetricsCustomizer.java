package ar.edu.utn.dds.k3003.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MetricsCustomizer {

    public MetricsCustomizer(MeterRegistry registry) {
        registry.config().commonTags("application", "solicitudes");
    }
}
