package ca.jdelreyes.moviereservationsystem.service.impl;

import ca.jdelreyes.moviereservationsystem.dto.user.UpdateOwnPasswordRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UpdateOwnProfileRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UpdateUserRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UserResponse;
import ca.jdelreyes.moviereservationsystem.exception.BadRequestException;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.helper.Mapper;
import ca.jdelreyes.moviereservationsystem.model.User;
import ca.jdelreyes.moviereservationsystem.repository.UserRepository;
import ca.jdelreyes.moviereservationsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getUsers(PageRequest pageRequest) {
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

        return Mapper.mapUserToUserResponse(user);
    }

    @Override
    public UserResponse updateOwnProfile(String username,
                                         UpdateOwnProfileRequest updateOwnProfileRequest) throws NotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(NotFoundException::new);
        user.setUsername(updateOwnProfileRequest.username());

        userRepository.save(user);

        return Mapper.mapUserToUserResponse(user);
    }

    @Override
    public void updateOwnPassword(String username,
                                  UpdateOwnPasswordRequest updateOwnPasswordRequest) throws NotFoundException, BadRequestException {
        User user = userRepository.findUserByUsername(username).orElseThrow(NotFoundException::new);

        if (!passwordEncoder.matches(updateOwnPasswordRequest.oldPassword(), user.getPassword()))
            throw new BadRequestException();

        user.setPassword(updateOwnPasswordRequest.newPassword());

        userRepository.save(user);
    }

    @Override
    public void deleteOwnAccount(String username) throws NotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(NotFoundException::new);

        // remove user from security context, basically log out
        SecurityContextHolder.getContext().setAuthentication(null);

        userRepository.delete(user);
    }

    @Override
    public void deleteUser(Long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        userRepository.delete(user);
    }
}
