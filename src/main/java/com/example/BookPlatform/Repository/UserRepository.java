package com.example.BookPlatform.Repository;

import com.example.BookPlatform.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
