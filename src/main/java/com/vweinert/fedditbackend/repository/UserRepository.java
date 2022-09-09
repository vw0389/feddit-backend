package com.vweinert.fedditbackend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vweinert.fedditbackend.models.User;
public interface UserRepository extends JpaRepository<User, Long> {
}
