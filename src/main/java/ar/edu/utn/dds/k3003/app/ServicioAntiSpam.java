package ar.edu.utn.dds.k3003.app;

import org.springframework.stereotype.Service;

@Service
public class ServicioAntiSpam {

    public void validar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto no puede estar vacío");
        }
    }
}
