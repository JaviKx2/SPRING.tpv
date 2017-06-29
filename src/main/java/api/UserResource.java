package api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import api.exceptions.UserFieldAlreadyExistException;
import api.exceptions.UserNotDeleteException;
import api.exceptions.UserFieldInvalidException;
import api.exceptions.TicketReferenceNotFoundException;
import api.exceptions.UserIdNotFoundException;
import api.exceptions.UserMobileNotFoundException;
import controllers.UserController;
import entities.users.Role;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import wrappers.UserDetailsWrapper;
import wrappers.UserWrapper;

@RestController
@RequestMapping(Uris.VERSION + Uris.USERS)
public class UserResource {

    private UserController userController;

    @Autowired
    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void userRegistration(@RequestBody UserWrapper userWrapper, String role)
            throws UserFieldInvalidException, UserFieldAlreadyExistException {
        this.validateFieldRegiter(userWrapper, "UserWrapper:usuario");
        validateField(userWrapper.getUsername(), "username");
        if (this.StringToRole(role) == null)
            return;
        if (!this.userController.registration(userWrapper, Role.valueOf(Role.class, role))) {
            throw new UserFieldAlreadyExistException();
        }
    }
    
    @ApiOperation(value = "Find partners")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format: property(,asc|desc). "
                    + "Default sort order is ascending. " + "Multiple sort criteria are supported.")})
    @RequestMapping(method = RequestMethod.GET)
    // @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Page<UserWrapper> userList(@ApiIgnore final Pageable pageable, String role) throws UserFieldInvalidException {
        this.validateFieldObject(pageable, "Pageable: objeto para paginar");
        if (this.StringToRole(role) == null)
            return null;
        return userController.getAllAndRole(pageable, Role.valueOf(Role.class, role));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateUser(@RequestBody UserWrapper userWrapper) throws UserIdNotFoundException {
        if (!userController.userExists(userWrapper.getId())) {
            throw new UserIdNotFoundException("User id: " + userWrapper.getId());
        }
        userController.updateUser(userWrapper);
    }


    @RequestMapping(value = Uris.USERS_PHONE, method = RequestMethod.GET)
    public UserDetailsWrapper findUserByMobilePhone(@PathVariable long mobilePhone) throws UserMobileNotFoundException {
        if (!userController.userExists(mobilePhone)) {
            throw new UserMobileNotFoundException();
        }
        return userController.findUserByMobilePhone(mobilePhone);
    }

    @RequestMapping(value = Uris.USERS_MOBILE, method = RequestMethod.GET)
    // @PreAuthorize("hasRole('ADMIN')")
    public UserWrapper userMobile(long mobile, String role) throws UserFieldInvalidException {
        this.validateFieldObject(mobile, "mobile");
        if (this.StringToRole(role) == null)
            return null;
        return userController.getByMobileAndRole(mobile, Role.valueOf(Role.class, role));
    }

    @RequestMapping(value = Uris.USERS_IDENTIFICATION, method = RequestMethod.GET)
    // @PreAuthorize("hasRole('ADMIN')")
    public UserWrapper userIdentificacion(String identification, String role) throws UserFieldInvalidException {
        this.validateField(identification, "identification:dni");
        if (this.StringToRole(role) == null)
            return null;
        return userController.getByDniAndRole(identification, Role.valueOf(Role.class, role));
    }

    @RequestMapping(value = Uris.USERS_EMAIL, method = RequestMethod.GET)
    // @PreAuthorize("hasRole('ADMIN')")
    public UserWrapper userEmail(@RequestParam(value = "email") String email, String role) throws UserFieldInvalidException {
        this.validateField(email, "email");
        if (this.StringToRole(role) == null)
            return null;
        return userController.getByEmailAndRole(email, Role.valueOf(Role.class, role));
    }

    @RequestMapping(value = Uris.USER_MOBILE_ID, method = RequestMethod.DELETE)
    // @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable long mobile) throws UserNotDeleteException {
        try {
            this.userController.deleteUser(mobile);
        } catch (Exception e) {
            throw new UserNotDeleteException();
        }
    }

    @RequestMapping(value = Uris.USERS_TICKET_REFERENCE + Uris.USERS_REFERENCE_ID, method = RequestMethod.GET)
    public UserWrapper getByTicketReference(@PathVariable(value = "reference") String reference)
            throws TicketReferenceNotFoundException, UserFieldInvalidException {
        validateField(reference, "ticketReference");
        return this.userController.getByTicketReference(reference);
    }

    private void validateFieldObject(Object objeto, String msg) throws UserFieldInvalidException {
        if (objeto == null)
            throw new UserFieldInvalidException(msg);
    }

    private void validateFieldRegiter(UserWrapper user, String msg) throws UserFieldInvalidException {
        if (user == null) {
            throw new UserFieldInvalidException(msg);
        } else {
            if ((user.getPassword() == null) || (user.getUsername() == null))
                throw new UserFieldInvalidException(msg);
        }
    }

    private void validateField(String field, String msg) throws UserFieldInvalidException {
        if (field == null || field.isEmpty()) {
            throw new UserFieldInvalidException(msg);
        }
    }

    private Role StringToRole(String roleParam) {
        Role rolRetorno = null;
        try {
            rolRetorno = Role.valueOf(Role.class, roleParam);
            return rolRetorno;
        } catch (Exception e) {
            return rolRetorno;
        }

    }

}
