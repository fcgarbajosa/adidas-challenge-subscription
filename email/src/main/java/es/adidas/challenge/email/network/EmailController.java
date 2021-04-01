package es.adidas.challenge.email.network;

import es.adidas.challenge.email.network.emailControllerRequests.UserEmailRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;


@RestController
@RequestMapping("/email")
public class EmailController {

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody UserEmailRequest userEmailRequest) {

        // Validate parameters

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<UserEmailRequest>> validated = validator.validate(userEmailRequest);
        if (!validator.validate(userEmailRequest).isEmpty()) {
            return new ResponseEntity<>(new String("No email address or no valid email address"), HttpStatus.BAD_REQUEST);
        }

        // Simulation of mail sending

        String message = "Email sent to " + userEmailRequest.getEmailAddress();

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
