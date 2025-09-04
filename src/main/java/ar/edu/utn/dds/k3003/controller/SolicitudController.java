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

    @GetMapping(params= "hecho")
    public ResponseEntity<List<SolicitudDTO>> buscarPorHecho(@RequestParam("hecho") String hechoId) {
        return ResponseEntity.ok(fachada.buscarSolicitudXHecho(hechoId));
    }

    @GetMapping
    public ResponseEntity<List<SolicitudDTO>> buscarSolicitudes() {
        return ResponseEntity.ok(fachada.buscarSolicitudes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> buscarPorId(@PathVariable String id) {
            return ResponseEntity.ok(fachada.buscarSolicitudXId(id));
    }

    @GetMapping("/estado/{tipo}")
    public ResponseEntity<List<SolicitudDTO>> buscarPorEstado(@PathVariable String tipo) {
            return ResponseEntity.ok(fachada.buscarSolicitudesXEstado(tipo));
    }

    @PostMapping
    public ResponseEntity<SolicitudDTO> agregar(@RequestBody SolicitudDTO dto) {
            return ResponseEntity.ok(fachada.agregar(dto));
    }

    @PatchMapping
    public ResponseEntity<SolicitudDTO> modificar(@RequestBody SolicitudModificacionRequestDTO body) {
            return ResponseEntity.ok(fachada.modificar(body.getId(), body.getEstado(), body.getDescripcion()));
    }
}
