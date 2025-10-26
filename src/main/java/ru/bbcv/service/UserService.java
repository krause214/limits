package ru.bbcv.service;

import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.bbcv.entity.User;
import ru.bbcv.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LimitService limitService;

    public UserService(UserRepository userRepository,
                       LimitService limitService) {
        this.userRepository = userRepository;
        this.limitService = limitService;
    }

    @NonNull
    @Transactional
    public User getOrCreateUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            User userToCreate = new User();
            userToCreate.setUsername(username);
            userToCreate.setLimit(limitService.createLimit(userToCreate));
            return userRepository.save(userToCreate);
        } else {
            return user.get();
        }
    }

    public User get(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException(String.format("Не найден пользователя с именем %s", username)));
    }
}
