<<<<<<<< HEAD:src/main/java/com/incresol/app/repositories/UserRepository.java
package com.incresol.app.repositories;
========
package com.incresol.app.repository;
>>>>>>>> d4352a62435660cbfa165f4bddcb64e23b913196:src/main/java/com/incresol/app/repository/UserRepository.java

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.incresol.app.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	public User findByEmail(String email);


	public List<User> findByAccountNonLockedFalseAndLockedUntilBefore(LocalDateTime now);


}
