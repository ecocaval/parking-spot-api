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

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spots")

public class ParkingSpotController {
    final ParkingSpotService parkingSpotService;
    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @GetMapping("/{parkingSpotNumber}")
    public ResponseEntity<Object> listParkingSpot(
        @PathVariable(value = "parkingSpotNumber") String parkingSpotNumber
    ) {
        if(!parkingSpotService.existsByParkingSpotNumber(parkingSpotNumber)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "This parking spot is not occupied!"
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                parkingSpotService.getParkingSpotByNumber(parkingSpotNumber)
        );
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpotModel>> listParkingSpots() {
        return ResponseEntity.status(HttpStatus.OK).body(
                parkingSpotService.getParkingSpots()
        );
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {
        if(parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    "License plate in use!"
            );
        }
        if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    "Parking spot in use!"
            );
        }
        if(parkingSpotService.existsByApartmentBLock(
                parkingSpotDto.getApartment(),
                parkingSpotDto.getBlock()
        )) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    "Apartment Block in use!"
            );
        }
        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")) );
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

    @DeleteMapping("/{parkingSpotNumber}")
    public ResponseEntity<Object> clearParkingSpot(
        @PathVariable(value = "parkingSpotNumber") String parkingSpotNumber
    ) {
        if(!parkingSpotService.existsByParkingSpotNumber(parkingSpotNumber)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "This parking spot is not occupied!"
            );
        }
        if(parkingSpotService.clear(parkingSpotNumber) < 1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Sorry, we could not clear this parking spot!"
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                "Parking Spot " + parkingSpotNumber + " deleted successfully!"
        );
    }
}
