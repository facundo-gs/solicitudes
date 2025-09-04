package ar.edi.itn.dds.k3003.model;

import ar.edu.utn.dds.k3003.RestClient.FuenteRestClient;
import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.app.ServicioAntiSpam;
import ar.edu.utn.dds.k3003.entity.Solicitud;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import ar.edu.utn.dds.k3003.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolicitudesTest {

    private Fachada fachada;
    private SolicitudRepository mockRepo;
    private ServicioAntiSpam mockSpam;
    private FuenteRestClient mockFuente;

    @BeforeEach
    void setUp() {
        mockRepo = mock(SolicitudRepository.class);
        mockSpam = mock(ServicioAntiSpam.class);
        mockFuente = mock(FuenteRestClient.class);

        fachada = new Fachada(mockRepo, mockSpam, mockFuente);

    }

    @Test
    void agregar_agregaSolicitud_cuandoHechoExiste() {
        HechoDTO hecho = new HechoDTO("unHecho", "coleccionX", "titulo");
        when(mockFuente.findHechoById("unHecho")).thenReturn(hecho);
        Solicitud solicitudMock = new Solicitud("unHecho", "descripcion");
        solicitudMock.setId(1L);

        when(mockRepo.save(any(Solicitud.class))).thenReturn(solicitudMock);

        SolicitudDTO dto = new SolicitudDTO(null, "descripcion", null, "unHecho");
        SolicitudDTO resultado = fachada.agregar(dto);

        assertEquals("descripcion", resultado.descripcion());
        assertEquals("unHecho", resultado.hechoId());
        assertEquals("1", resultado.id());
    }

    @Test
    void modificar_modificaSolicitud_cuandoExiste() {
        Solicitud solicitud = new Solicitud("unHecho", "desc vieja");
        solicitud.setId(10L);

        when(mockRepo.findById(10L)).thenReturn(Optional.of(solicitud));
        when(mockRepo.save(any(Solicitud.class))).thenReturn(solicitud);

        SolicitudDTO modificado = fachada.modificar("10", EstadoSolicitudBorradoEnum.RECHAZADA, "desc nueva");

        assertEquals("desc nueva", modificado.descripcion());
        assertEquals(EstadoSolicitudBorradoEnum.RECHAZADA, modificado.estado());
    }

    @Test
    void buscarPorHecho_filtraCorrectamente() {
        Solicitud s1 = new Solicitud("hecho1", "desc 1"); s1.setId(1L);
        Solicitud s2 = new Solicitud("hecho1", "desc 2"); s2.setId(2L);

        when(mockRepo.findByHechoId("hecho1")).thenReturn(List.of(s1, s2));

        List<SolicitudDTO> lista = fachada.buscarSolicitudXHecho("hecho1");

        assertEquals(2, lista.size());
    }



    @Test
    void estaActivo_devuelveTrue_cuandoEsCREADA() {
        Solicitud solicitud = new Solicitud("h", "d");
        solicitud.setId(1L);
        solicitud.setEstado(EstadoSolicitudBorradoEnum.CREADA);
        when(mockRepo.findById(1L)).thenReturn(Optional.of(solicitud));

        assertTrue(fachada.estaActivo("1"));
    }

    @Test
    void estaActivo_devuelveFalse_siEsRECHAZADA() {
        Solicitud solicitud = new Solicitud("h", "d");
        solicitud.setId(1L);
        solicitud.setEstado(EstadoSolicitudBorradoEnum.RECHAZADA);
        when(mockRepo.findById(1L)).thenReturn(Optional.of(solicitud));

        assertFalse(fachada.estaActivo("1"));
    }
}
