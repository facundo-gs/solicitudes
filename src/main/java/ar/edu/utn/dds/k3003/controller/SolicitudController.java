package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import ar.edu.utn.dds.k3003.dto.SolicitudModificacionRequestDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final Fachada fachada;

    @Autowired
    public SolicitudController(Fachada fachada) {
        this.fachada = fachada;
    }

    @Timed(value = "solicitudes.buscarPorHecho.time", description = "Time spent searching by hecho")
    @Counted(value = "solicitudes.buscarPorHecho.count", description = "Count of searches by hecho")
    @GetMapping(params= "hecho")
    public ResponseEntity<List<SolicitudDTO>> buscarPorHecho(@RequestParam("hecho") String hechoId) {
        return ResponseEntity.ok(fachada.buscarSolicitudXHecho(hechoId));
    }

    @Timed(value = "solicitudes.buscarSolicitudes.time", description = "Time spent searching all solicitudes")
    @Counted(value = "solicitudes.buscarSolicitudes.count", description = "Count of searches for all solicitudes")
    @GetMapping
    public ResponseEntity<List<SolicitudDTO>> buscarSolicitudes() {
        return ResponseEntity.ok(fachada.buscarSolicitudes());
    }

    @Timed(value = "solicitudes.buscarPorId.time", description = "Time spent searching by id")
    @Counted(value = "solicitudes.buscarPorId.count", description = "Count of searches by id")
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(fachada.buscarSolicitudXId(id));
    }

    @Timed(value = "solicitudes.buscarPorEstado.time", description = "Time spent searching by estado")
    @Counted(value = "solicitudes.buscarPorEstado.count", description = "Count of searches by estado")
    @GetMapping("/estado/{tipo}")
        public ResponseEntity<List<SolicitudDTO>> buscarPorEstado(@PathVariable String tipo) {
        return ResponseEntity.ok(fachada.buscarSolicitudesXEstado(tipo));
    }

    @Timed(value = "solicitudes.agregar.time", description = "Time spent creating solicitud")
    @Counted(value = "solicitudes.agregar.count", description = "Count of created solicitudes")
    @PostMapping
        public ResponseEntity<SolicitudDTO> agregar(@RequestBody SolicitudDTO dto) {
        return ResponseEntity.ok(fachada.agregar(dto));
    }

    @Timed(value = "solicitudes.modificar.time", description = "Time spent modifying solicitud")
    @Counted(value = "solicitudes.modificar.count", description = "Count of modified solicitudes")
    @PatchMapping
    public ResponseEntity<SolicitudDTO> modificar(@RequestBody SolicitudModificacionRequestDTO body) {
        return ResponseEntity.ok(fachada.modificar(body.getId(), body.getEstado(), body.getDescripcion()));
    }

    @GetMapping("/activo/{hechoId}")
    public ResponseEntity<Boolean> estaActivo(@PathVariable String hechoId) {
        return ResponseEntity.ok(fachada.hechoActivo(hechoId));
    }

    @PostMapping("/borrarTodo")
    public ResponseEntity<String> borrarTodo() {
        return ResponseEntity.ok(fachada.borrarTodo());
    }

}
