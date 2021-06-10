package com.buster.api.controllers;

import com.buster.api.models.entity.Alumno;
import com.buster.api.models.services.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AlumnoController {
    @Autowired
    private AlumnoService alumnoService;

    @GetMapping("/alumnos")
    public List<Alumno> getAll() {
        return alumnoService.findAll();
    }

    @GetMapping("/alumnos/{id}")
    public ResponseEntity<?> showStudentById(@PathVariable Long id) {
        Alumno alumno = null;
        Map<String, Object> resp = new HashMap<>();
        try {
            alumno = alumnoService.findById(id);
        } catch (DataAccessException e) {
            resp.put("message", "There has been an error");
            return new ResponseEntity<Map<String, Object>>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (alumno == null) {
            resp.put("message", "Does not exist");
            return new ResponseEntity<Map<String, Object>>(resp, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Alumno>(alumno, HttpStatus.OK);
    }

    @PostMapping("/alumnos/create")
    public ResponseEntity<?> createStudent(@RequestBody Alumno alumno) {
        Alumno newStudent = null;
        Map<String, Object> resp = new HashMap<>();
        try {
            newStudent = alumnoService.save(alumno);
        } catch (DataAccessException e) {
            resp.put("message", "There has been an error");
            resp.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        resp.put("message", "Successfully added");
        resp.put("student", newStudent);
        return new ResponseEntity<Map<String, Object>>(resp, HttpStatus.CREATED);
    }

    @PutMapping("/alumnos/edit/{id}")
    public ResponseEntity<?> updateStudent(@RequestBody Alumno alumno, @PathVariable Long id) {
        Alumno alumnoActual = alumnoService.findById(id);
        Alumno alumnoActualizado = null;

        Map<String, Object> response = new HashMap<>();
        if (alumnoActual == null) {
            response.put("message", "Does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            alumnoActual.setNombre(alumno.getNombre());
            alumnoActualizado = alumnoService.save(alumnoActual);
        } catch (DataAccessException e) {
            response.put("message", "There has been an error");
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "Successfully updated");
        response.put("student", alumnoActualizado);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("alumnos/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            alumnoService.delete(id);
        } catch (DataAccessException e) {
            response.put("message", "Delete failed");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "Successfully deleted");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
