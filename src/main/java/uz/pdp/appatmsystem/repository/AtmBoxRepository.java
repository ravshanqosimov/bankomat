package uz.pdp.appatmsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appatmsystem.entity.AtmBox;

public interface AtmBoxRepository extends JpaRepository<AtmBox,Integer> {
}
