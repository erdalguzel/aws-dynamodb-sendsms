package springaws.handlingformsubmission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GreetingController {

    private final DynamoDBEnhanced dde;
    private final PublishTextSMS msg;

    @Autowired
    public GreetingController(DynamoDBEnhanced dde, PublishTextSMS msg) {
        this.dde = dde;
        this.msg = msg;
    }

    @GetMapping("/")
    public String greetingForm(Model model) {
        model.addAttribute("greeting", new Greeting());
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(Greeting greeting) {
        // Persist submitted data in a DynamoDB table
        dde.injectDynamoItem(greeting);

        //Send a mobile notification
        msg.sendMessage(greeting.getId());

        return "result";
    }

}
