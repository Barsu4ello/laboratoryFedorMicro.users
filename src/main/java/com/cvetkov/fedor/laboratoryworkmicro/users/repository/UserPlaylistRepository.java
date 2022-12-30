package com.cvetkov.fedor.laboratoryworkmicro.users.repository;

import com.cvetkov.fedor.laboratoryworkmicro.entities.model.UserPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPlaylistRepository extends JpaRepository<UserPlaylist, Long> {
}
