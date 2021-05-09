package uz.pdp.appatmsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appatmsystem.entity.Transfer;
import uz.pdp.appatmsystem.enums.RoleName;
import uz.pdp.appatmsystem.enums.TransferType;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Integer> {
    List<Transfer> findAllByAtmSerialNumberAndTransferTypeAndDate(String atmSerialNumber, TransferType transferType, SimpleDateFormat date);

    List<Transfer> findAllByAtmSerialNumberAndRoleName(String atmSerialNumber, RoleName roleName);
}
