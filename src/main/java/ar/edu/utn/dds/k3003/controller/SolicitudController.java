package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import ar.edu.utn.dds.k3003.app.SolicitudModificacionRequestDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {

    private final Fachada fachada;

    @Autowired
    public SolicitudController(Fachada fachada) {
        this.fachada = fachada;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudDTO>> buscarPorHecho(@RequestParam("hecho") String hechoId) {
        return ResponseEntity.ok(fachada.buscarSolicitudXHecho(hechoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> buscarPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(fachada.buscarSolicitudXId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SolicitudDTO> agregar(@RequestBody SolicitudDTO dto) {
        try {
            return ResponseEntity.ok(fachada.agregar(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping
    public ResponseEntity<SolicitudDTO> modificar(@RequestBody SolicitudModificacionRequestDTO body) {
        try {
            return ResponseEntity.ok(
                    fachada.modificar(body.getId(), body.getEstado(), body.getDescripcion())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
