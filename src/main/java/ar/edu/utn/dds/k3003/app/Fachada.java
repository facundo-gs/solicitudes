package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.RestClient.FuenteRestClient;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.repository.SolicitudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class Fachada {

    private final SolicitudRepository solicitudRepository;
    private final ServicioAntiSpam servicioAntiSpam;
    private final FuenteRestClient fuenteRestClient;

   public Fachada(SolicitudRepository solicitudRepository, ServicioAntiSpam servicioAntiSpam, FuenteRestClient fuenteRestClient) {
        this.solicitudRepository = solicitudRepository;
        this.servicioAntiSpam = servicioAntiSpam;
        this.fuenteRestClient = fuenteRestClient;
    }


    public SolicitudDTO agregar(SolicitudDTO dto) {
        validarHecho(dto.hechoId());
        servicioAntiSpam.validar(dto.descripcion());

        Solicitud solicitud = new Solicitud(dto.hechoId(), dto.descripcion());
        solicitud = solicitudRepository.save(solicitud);

        return toResponseDTO(solicitud);
    }


    public SolicitudDTO modificar(String solicitudId, EstadoSolicitudBorradoEnum estado, String descripcion) {
        Solicitud solicitud = obtenerPorId(Long.parseLong(solicitudId));
        solicitud.setDescripcion(descripcion);
        solicitud.setEstado(estado);
        solicitud = solicitudRepository.save(solicitud);
        return toResponseDTO(solicitud);
    }


    public List<SolicitudDTO> buscarSolicitudXHecho(String hechoId) {
        return solicitudRepository.findByHechoId(hechoId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SolicitudDTO> buscarSolicitudes() {
        return solicitudRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    public SolicitudDTO buscarSolicitudXId(String id) {
        return toResponseDTO(obtenerPorId(Long.parseLong(id)));
    }

    public List<SolicitudDTO> buscarSolicitudesXEstado(String estado) {
        return obtenerPorEstado(estado).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public boolean estaActivo(String id) {
        return obtenerPorId(Long.parseLong(id)).getEstado() == EstadoSolicitudBorradoEnum.CREADA;
    }

    private Solicitud obtenerPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Solicitud no encontrada"));
    }

    private List<Solicitud> obtenerPorEstado(String estado) {
        try {
            return solicitudRepository.findByEstado(EstadoSolicitudBorradoEnum.valueOf(estado));
        } catch (Exception e) {
            throw new NoSuchElementException("Solicitud no encontrada");
        }
    }

    private void validarHecho(String hechoId) {
        try {
            fuenteRestClient.findHechoById(hechoId);
        }catch (RuntimeException e) {
            throw new IllegalArgumentException("El hecho no existe");
        }
    }

    private SolicitudDTO toResponseDTO(Solicitud solicitud) {
        return new SolicitudDTO(
                solicitud.getId().toString(),
                solicitud.getDescripcion(),
                solicitud.getEstado(),
                solicitud.getHechoId()
        );
    }
}
