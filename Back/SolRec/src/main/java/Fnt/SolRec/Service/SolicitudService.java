package Fnt.SolRec.Service;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import Fnt.SolRec.Model.Solicitud;
import Fnt.SolRec.Reposoitory.SolicitudRepository;

@Service
public class SolicitudService {
    
    @Autowired
    private SolicitudRepository solicitudRepository;
    
    public Solicitud saveOrUpdateSolicitud(Solicitud solicitud){
        return solicitudRepository.save(solicitud);
    }
    public Iterable<Solicitud> listAll() {
        return solicitudRepository.findAll();
    }
    public Iterable<Solicitud> getSolicitudByEstado(List<String> estado) {
        return solicitudRepository.findByEstado(estado);
    }
}