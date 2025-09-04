package ar.edu.utn.dds.k3003.dto;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SolicitudModificacionRequestDTO {
    private String id;
    private String descripcion;
    private EstadoSolicitudBorradoEnum estado;

}