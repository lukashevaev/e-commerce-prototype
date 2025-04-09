package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.dto.MyUserDetails;
import com.bubusyaka.demo.model.dto.UserDTO;
import com.bubusyaka.demo.model.entity.UserEntity;
import com.bubusyaka.demo.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public List<UserDTO> allUsers() {
        return userRepository.findAll()
                .stream()
                .map(entity -> new UserDTO(entity.getId(),entity.getUsername(),entity.getAge(),entity.getCity(),entity.getRole(), entity.getUserPassword()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findAll().stream().filter(userEntity -> userEntity.getUsername().equals(username)).findFirst().orElse(null);

        return new MyUserDetails(user);
    }
}
