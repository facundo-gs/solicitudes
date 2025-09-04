package ar.edu.utn.dds.k3003.entity;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hechoId;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitudBorradoEnum estado;

    public Solicitud(String hechoId, String descripcion) {
        this.hechoId = hechoId;
        this.descripcion = descripcion;
        this.estado = EstadoSolicitudBorradoEnum.CREADA;
    }
}
