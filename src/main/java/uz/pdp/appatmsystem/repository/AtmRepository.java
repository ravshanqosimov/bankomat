package uz.pdp.appatmsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appatmsystem.entity.ATM;

import java.util.Optional;
import java.util.UUID;

public interface AtmRepository extends JpaRepository<ATM, UUID> {
    boolean existsBySerialNumber(String serialNumber);

    Optional<ATM> findBySerialNumber(String serialNumber);
}
