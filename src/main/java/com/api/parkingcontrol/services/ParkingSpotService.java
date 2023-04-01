package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
    public Object save(ParkingSpotModel parkingSpotModel) {
        return parkingSpotRepository.save(parkingSpotModel);
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
        return (parkingSpotRepository.existsByApartment(apartment) && parkingSpotRepository.existsByBlock(block));
    }

}
