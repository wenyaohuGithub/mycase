package com.hu.cm.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.hu.cm.domain.admin.Role;
import com.hu.cm.domain.admin.User;
import com.hu.cm.repository.admin.DepartmentRepository;
import com.hu.cm.repository.admin.RoleRepository;
import com.hu.cm.repository.admin.UserRepository;
import com.hu.cm.security.SecurityUtils;
import com.hu.cm.service.UserService;
import com.hu.cm.web.rest.dto.UserDTO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api/users")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private DepartmentRepository deptRepository;

    @Inject
    private UserService userService;
    /**
     * GET  /users -> get all users.
     */
    @RequestMapping(
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> getAll() {
        log.debug("REST request to get all Users");
        return userRepository.findAll();
    }

    /**
     * GET  /users/:login -> get the "login" user.
     */
    @RequestMapping(value = "/login/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<User> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userRepository.findOneByLogin(login)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /users/:id -> get the "id" user.
     */
    @RequestMapping(value = "/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        log.debug("REST request to get User : {}", id);
        return Optional.ofNullable(userRepository.findByIdAndFetchUserRoles(id))
            .map(user -> {
                return new ResponseEntity<>(
                    new UserDTO(
                        user.getId(),
                        user.getLogin(),
                        null,
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getLangKey(),
                        (user.getDepartmentId()==null?null:deptRepository.findOne(user.getDepartmentId()).getName()),
                        user.getRoles().stream().map(Role::getName)
                            .collect(Collectors.toList())),
                    HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * PUT  /user -> Updates an existing user.
     */
    @RequestMapping(value = "/{id}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UserDTO userDto) throws URISyntaxException {
        log.debug("REST request to update User : {}", userDto);
        User user = userRepository.findOne(id);
        user.setLogin(userDto.getLogin());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setLangKey(userDto.getLangKey());
        user.setLastModifiedDate(new DateTime());
        user.setLastModifiedBy(SecurityUtils.getCurrentLogin());
        User result = userRepository.save(user);
        return ResponseEntity.ok().body(result);
    }

    /**
     * DELETE  /user/:id -> delete the "id" user.
     */
    @RequestMapping(value = "/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete User : {}", id);
        userRepository.delete(id);
    }

    /**
     * PUT  /user -> Remove a role from a user
     */
    @RequestMapping(value = "/{userId}/removerole/{roleName}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<User> removeUserRole(@Valid @PathVariable Long userId, @Valid @PathVariable String roleName)
        throws URISyntaxException {
        log.debug("REST request to remove a role " + roleName + " from user: " + userId);
        User user = userRepository.findOne(userId);
        Role removeRole = null;
        for(Role role : user.getRoles()){
            if(role.getName().equals(roleName)){
                removeRole = role;
                break;
            }
        }
        if(removeRole != null){
            user.getRoles().remove(removeRole);
            user = userRepository.save(user);
        }
        return ResponseEntity.ok().body(user);
    }

    /**
     * PUT  /users -> update(add/remove) a user's roles
     */
    @RequestMapping(value = "/roles/{userId}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<User> addUserRole(@Valid @PathVariable Long userId, @RequestBody ArrayList<String> roles)
        throws URISyntaxException {
        log.debug("REST request to update user " + userId + " roles: " + roles.toString());
        User user = userRepository.findByIdAndFetchUserRoles(userId);
        if(user == null){
            return ResponseEntity.badRequest().header("Failure", "user not found").body(null);
        }
        user.getRoles().clear();
        for(String rolename : roles){
            Role role = roleRepository.findOne(rolename);
            if(role != null) {
                user.getRoles().add(role);
            }
        }
        user = userRepository.save(user);
        return ResponseEntity.ok().body(user);
    }

    /**
     * POST  /create -> create a user.
     */
    @RequestMapping(value = "/new",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<User> create(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        if(userRepository.findOneByLogin(userDTO.getLogin()).isPresent()){
            return ResponseEntity.badRequest().header("Failure", "login already in use").body(null);
        }
        if(userRepository.findOneByEmail(userDTO.getEmail()).isPresent()){
            return ResponseEntity.badRequest().header("Failure", "e-mail address already in use").body(null);
        }
        User user = userService.createUserInformation(userDTO.getLogin(), userDTO.getPassword(),
            userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail().toLowerCase(),
            userDTO.getLangKey(), userDTO.getDepartmentId());
        return ResponseEntity.created(new URI("/api/users/new/" + user.getId())).body(user);
    }
}
