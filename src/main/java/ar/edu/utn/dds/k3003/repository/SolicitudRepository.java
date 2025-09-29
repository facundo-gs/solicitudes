package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.entity.Solicitud;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findByHechoId(String hechoId);
    List<Solicitud> findByEstado(EstadoSolicitudBorradoEnum estado);

}