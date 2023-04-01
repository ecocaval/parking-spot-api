package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingSpotService {
    final ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    public ParkingSpotModel getParkingSpotByNumber(String parkingSpotNumber) {
        return parkingSpotRepository.findByParkingSpotNumber(parkingSpotNumber);
    }

    @Transactional
    public List<ParkingSpotModel> getParkingSpots() {
        return parkingSpotRepository.findAll();
    }

    @Transactional
    public Object save(ParkingSpotModel parkingSpotModel) {
        return parkingSpotRepository.save(parkingSpotModel);
    }
    @Transactional
    public Integer clear(String parkingSpotNumber) {
        return parkingSpotRepository.deleteByParkingSpotNumber(parkingSpotNumber);
    }

    @Transactional
    public Boolean existsByLicensePlateCar(String licensePlateCar) {
        return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
    }

    @Transactional
    public Boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    @Transactional
    public Boolean existsByApartmentBLock(String apartment, String block) {
        return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
    }



}
