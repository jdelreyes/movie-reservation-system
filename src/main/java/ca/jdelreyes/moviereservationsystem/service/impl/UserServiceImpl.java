package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.user.UpdateUserRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UserResponse;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.helper.Mapper;
import ca.jdelreyes.moviereservationsystem.model.User;
import ca.jdelreyes.moviereservationsystem.repository.UserRepository;
import ca.jdelreyes.moviereservationsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(Mapper::mapUserToUserResponse).toList();
    }

    @Override
    public UserResponse getUser(Long id) throws NotFoundException {
        return Mapper.mapUserToUserResponse(userRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest updateUserRequest) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        user.setUsername(updateUserRequest.username());

        userRepository.save(user);

        return Mapper.mapUserToUserResponse(user);
    }

    @Override
    public void deleteUser(Long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        userRepository.delete(user);
    }
}
