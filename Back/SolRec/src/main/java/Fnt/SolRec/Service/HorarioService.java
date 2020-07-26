package Fnt.SolRec.Service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import Fnt.SolRec.Model.Horario;
import Fnt.SolRec.Model.Solicitud;
import Fnt.SolRec.Reposoitory.HorarioRepository;

@Service
public class HorarioService {
    
    @Autowired
    private HorarioRepository horarioRepository;
    
    public Horario saveOrUpdateHorario(Horario horario){
        return horarioRepository.save(horario);
    }

    public Iterable<Horario> listAll() {
        return horarioRepository.findAll();
    }

    public List<Horario> getByHorario(List<String> horario) {
        return horarioRepository.findByHorarioIn(horario);
    }

    public void delete(Long id) {
		horarioRepository.deleteById(id);
    }
    
    public Optional<Horario> getbyId(Long id){
        return horarioRepository.findById(id);
    }

    public ResponseEntity<List<Horario>> revisarChoque(Solicitud sol){
        String choques = "";
        List<Horario> bloques = horarioRepository.findByHorarioIn(sol.getBloques());
        for (Horario bloque : bloques){
            String hora = bloque.getHorario();
            if(bloque.getPaciente().contains(sol.getPaciente().getId())){
                choques+="Paciente ya reservado  en bloque "+ hora +"\n";
            }
            if(bloque.getIdEquipo().contains(sol.getIdEquipo())){
                choques+="Equipo ya reservado  en bloque "+ hora +"\n";
            }
            List<Integer> comun = bloque.getIdEquipamiento();
            comun.retainAll(sol.getIdEquipamiento());
            if(!comun.isEmpty()){
                choques+="Equipamiento n°"+ comun +" ya reservado en bloque "+ hora +"\n";
            }
            if(bloque.getSillon().contains(sol.getSillon())){
                choques+="Sillon ya reservado  en bloque "+ hora +"\n";
            }
            if(bloque.getSalaRecuperacion().contains(sol.getSalaRecuperacion())){
                choques+="Sala ya reservada  en bloque "+ hora +"\n";
            }
            if(bloque.getPabellon().contains(sol.getPabellon())){
                choques+="Pabellon ya reservado  en bloque "+ hora +"\n";
            }
        }
        if(choques.isBlank()){
            List<String> nuevos = sol.getBloques();
            if(bloques.size() != nuevos.size()){
                List<String> hechos = new ArrayList<>();
                for (Horario b : bloques){
                    hechos.add(b.getHorario());
                }
                nuevos.removeAll(hechos);
                for (String n : nuevos){
                    Horario nuevo = new Horario();
                    nuevo.setHorario(n);
                    this.saveOrUpdateHorario(nuevo);
                    bloques.add(nuevo);
                }
            }
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(bloques); 
        } else {
            return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .header("choques", choques)
            .body(null); 
        }
    }

    public List<Horario> agregarRes(Solicitud sol, List<Horario> horarios){
        for (Horario horario : horarios){
            horario.masUno();
            horario.addPaciente(sol.getPaciente().getId());
            horario.addIdEquipo(sol.getIdEquipo());
            horario.addIdEquipamiento(sol.getIdEquipamiento());
            horario.addSillon(sol.getSillon());
            horario.addSalaRecuperacion(sol.getSalaRecuperacion());
            horario.addPabellon(sol.getPabellon());
            this.saveOrUpdateHorario(horario);
        }
        return horarios;
    }

    public List<Horario> removerRes(Solicitud sol, List<Horario> horarios){
        for (Horario horario : horarios){
            horario.menosUno();
            horario.remPaciente(sol.getPaciente().getId());
            horario.remIdEquipo(sol.getIdEquipo());
            horario.remIdEquipamiento(sol.getIdEquipamiento());
            horario.remSillon(sol.getSillon());
            horario.remSalaRecuperacion(sol.getSalaRecuperacion());
            horario.remPabellon(sol.getPabellon());
            this.saveOrUpdateHorario(horario);
        }
        return horarios;
    }


}