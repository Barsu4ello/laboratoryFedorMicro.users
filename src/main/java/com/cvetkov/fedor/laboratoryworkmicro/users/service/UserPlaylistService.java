package com.cvetkov.fedor.laboratoryworkmicro.users.service;

import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.UserPlaylistRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.UserPlaylistResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.UserPlaylistUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserPlaylistService {
    Page<UserPlaylistResponse> getAllPage(Pageable pageable);

    List<UserPlaylistResponse> getAllList();

    UserPlaylistResponse findById(Long id);

    void save(UserPlaylistRequest userPlaylistRequest);

    void update(UserPlaylistUpdate userPlaylistUpdate);

    void deleteById(Long id);
}
