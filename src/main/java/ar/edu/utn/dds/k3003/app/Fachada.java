package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.repository.SolicitudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class Fachada implements ar.edu.utn.dds.k3003.facades.FachadaSolicitudes {

    private final SolicitudRepository solicitudRepository;
    private final ServicioAntiSpam servicioAntiSpam;
    private FachadaFuente fachadaFuente;

    public Fachada(SolicitudRepository solicitudRepository, ServicioAntiSpam servicioAntiSpam) {
        this.solicitudRepository = solicitudRepository;
        this.servicioAntiSpam = servicioAntiSpam;
    }

    @Override
    public SolicitudDTO agregar(SolicitudDTO dto) {
        // validarHecho(dto.hechoId());
        servicioAntiSpam.validar(dto.descripcion());

        Solicitud solicitud = new Solicitud(dto.hechoId(), dto.descripcion());
        solicitud = solicitudRepository.save(solicitud);

        return toDTO(solicitud);
    }

    @Override
    public SolicitudDTO modificar(String solicitudId, EstadoSolicitudBorradoEnum estado, String descripcion) {
        Solicitud solicitud = obtenerPorId(Long.parseLong(solicitudId));
        solicitud.setDescripcion(descripcion);
        solicitud.setEstado(estado);
        solicitud = solicitudRepository.save(solicitud);
        return toDTO(solicitud);
    }

    @Override
    public List<SolicitudDTO> buscarSolicitudXHecho(String hechoId) {
        return solicitudRepository.findByHechoId(hechoId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SolicitudDTO buscarSolicitudXId(String id) {
        return toDTO(obtenerPorId(Long.parseLong(id)));
    }

    @Override
    public boolean estaActivo(String id) {
        return obtenerPorId(Long.parseLong(id)).getEstado() == EstadoSolicitudBorradoEnum.CREADA;
    }

    @Override
    public void setFachadaFuente(FachadaFuente fachadaFuente) {
        this.fachadaFuente = fachadaFuente;
    }

    private Solicitud obtenerPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada"));
    }

    private void validarHecho(String hechoId) {
        if (fachadaFuente == null || fachadaFuente.buscarHechoXId(hechoId) == null) {
            throw new IllegalArgumentException("El hecho no existe");
        }
    }

    private SolicitudDTO toDTO(Solicitud solicitud) {
        return new SolicitudDTO(
                solicitud.getId().toString(),
                solicitud.getDescripcion(),
                solicitud.getEstado(),
                solicitud.getHechoId()
        );
    }
}
