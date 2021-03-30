package es.adidas.challenge.email.network;

import es.adidas.challenge.email.network.emailControllerRequests.UserEmailRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/email")
public class EmailController {

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody UserEmailRequest userEmailRequest) {

        // Simulation of mail sending

        if ("".equals(userEmailRequest.getEmailAddress())){
            return new ResponseEntity<>("Error. emailAddress parameter can't be empty string", HttpStatus.BAD_REQUEST);
        }

        String message = "Email sent to " + userEmailRequest.getEmailAddress();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
