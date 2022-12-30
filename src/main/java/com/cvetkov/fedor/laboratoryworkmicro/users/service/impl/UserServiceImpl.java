package com.cvetkov.fedor.laboratoryworkmicro.users.service.impl;

import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.UserRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.UserResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.UserUpdate;
import com.cvetkov.fedor.laboratoryworkmicro.entities.mapper.UserMapper;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.User;
import com.cvetkov.fedor.laboratoryworkmicro.users.repository.UserRepository;
import com.cvetkov.fedor.laboratoryworkmicro.users.service.UserService;
import com.cvetkov.fedor.laboratoryworkmicro.utils.enums.UserStatus;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
//    private final AudioRepository audioRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    //Когда появится секьюрити и с ним бин encoder'а убрать конструктор
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public Page<UserResponse> getAllPage(Pageable pageable) {
        return userMapper.userToUserResponsePage(userRepository.findAll(pageable));
    }

    @Override
    public List<UserResponse> getAllList() {
        return userMapper.userToUserResponseList(userRepository.findAll());
    }

    @Override
    public UserResponse findById(Long id) {
        return userMapper
                .userToUserResponse(userRepository
                        .findById(id)
                        .orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " not found")));
    }

    @Override
    public void save(UserRequest userRequest) {
        User user = userMapper.userRequestToUser(userRequest);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void update(UserUpdate userUpdate) {
        User user = userMapper.userUpdateToUser(userUpdate);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " not found"));
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void addAudiosByIdForUser(Long userId, List<Long> audiosId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ObjectNotFoundException("User with id " + userId + " not found"));
//        List<Long> audiosIdByUser = user.getAudios().stream().map(Audio::getId).collect(Collectors.toList());
//        audiosId.removeAll(audiosIdByUser);
//        List<Audio> audios = audioRepository.findAllById(audiosId);
//        user.getAudios().addAll(audios);
//        userRepository.save(user);
    }
}
