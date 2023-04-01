package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spots")

public class ParkingSpotController {
    final ParkingSpotService parkingSpotService;
    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listParkingSpot(
        @PathVariable(value = "id") UUID id
    ) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(parkingSpotModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "This parking spot is not occupied!"
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                parkingSpotModelOptional.get()
        );
    }

    @GetMapping
    public ResponseEntity<Object> listParkingSpots() {
        List<ParkingSpotModel> parkingSpotModelsOptional = parkingSpotService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelsOptional);
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {
        if(parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("License plate in use!");
        }
        if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Parking spot in use!");
        }
        if(parkingSpotService.existsByApartmentBLock(
                parkingSpotDto.getApartment(),
                parkingSpotDto.getBlock()
        )) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Apartment Block in use!");
        }
        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")) );
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> changeParkingSpot(
            @RequestBody @Valid ParkingSpotDto parkingSpotDto,
            @PathVariable(value = "id") UUID id
    ) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(parkingSpotModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This parking spot is not occupied!");
        }
        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> clearParkingSpot(
        @PathVariable(value = "id") UUID id
    ) {
        if(!parkingSpotService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This parking spot is not occupied!");
        }
        parkingSpotService.clear(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                "Parking Spot cleared successfully!"
        );
    }
}
