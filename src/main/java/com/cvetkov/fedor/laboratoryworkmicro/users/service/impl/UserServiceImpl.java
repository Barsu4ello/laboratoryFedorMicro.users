package com.cvetkov.fedor.laboratoryworkmicro.users.service.impl;

import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.request.UserRequest;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.response.UserResponse;
import com.cvetkov.fedor.laboratoryworkmicro.entities.dto.update.UserUpdate;
import com.cvetkov.fedor.laboratoryworkmicro.entities.mapper.UserMapper;
import com.cvetkov.fedor.laboratoryworkmicro.entities.model.User;
import com.cvetkov.fedor.laboratoryworkmicro.users.feign.AudioFeignClient;
import com.cvetkov.fedor.laboratoryworkmicro.users.feign.ConcertFeignClient;
import com.cvetkov.fedor.laboratoryworkmicro.users.repository.UserRepository;
import com.cvetkov.fedor.laboratoryworkmicro.users.service.UserService;
import com.cvetkov.fedor.laboratoryworkmicro.utils.enums.UserStatus;
import com.cvetkov.fedor.laboratoryworkmicro.utils.exception.ObjectNotFoundException;
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
    private final ConcertFeignClient concertFeignClient;
    private final AudioFeignClient audioFeignClient;

    //Когда появится секьюрити и с ним бин encoder'а убрать конструктор
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ConcertFeignClient concertFeignClient, AudioFeignClient audioFeignClient) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.concertFeignClient = concertFeignClient;
        this.audioFeignClient = audioFeignClient;
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
    @Transactional
    public void save(UserRequest userRequest) {
        // Проверим есть такой город в микросервисе concerts, иначе будет FeignException
        Long cityId = userRequest.getCityId();
        concertFeignClient.getCityById(cityId);


        // Проверим есть такой автор в микросервисе audios, иначе будет FeignException
        Long authorId = userRequest.getAuthorId();
        audioFeignClient.getAuthorById(authorId);

        User user = userMapper.userRequestToUser(userRequest);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(UserUpdate userUpdate) {
        // Проверим есть такой город в микросервисе concerts, иначе будет FeignException
        Long cityId = userUpdate.getCityId();
        concertFeignClient.getCityById(cityId);


        // Проверим есть такой автор в микросервисе audios, иначе будет FeignException
        Long authorId = userUpdate.getAuthorId();
        audioFeignClient.getAuthorById(authorId);

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
    public void changeCityIdToNull(Long cityId) {
        userRepository.setCityIdToNullByCityId(cityId);
    }

    @Override
    @Transactional
    public void changeAuthorIdToNull(Long authorId) {
        userRepository.setAuthorIdToNullByCityId(authorId);
//        return Mono.empty();
    }
}
