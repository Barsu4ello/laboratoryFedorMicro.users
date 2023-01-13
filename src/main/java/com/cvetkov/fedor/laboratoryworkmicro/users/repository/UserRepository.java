package com.cvetkov.fedor.laboratoryworkmicro.users.repository;

import com.cvetkov.fedor.laboratoryworkmicro.entities.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByCityId(Long id);

    List<User> findUsersByAuthorId(Long id);

    Optional<User> findByUsername(String userName);

    boolean existsByUsername(String username);

    @Modifying
    @Query("update User u set u.cityId = null where u.cityId = :cityId")
    void setCityIdToNullByCityId(@Param("cityId") Long cityId);

    @Modifying
    @Query("update User u set u.authorId = null where u.authorId = :authorId")
    void setAuthorIdToNullByCityId(@Param("authorId") Long authorId);
}
