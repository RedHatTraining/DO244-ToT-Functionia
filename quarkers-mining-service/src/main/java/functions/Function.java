package functions;

import io.quarkus.funqy.Funq;
import io.quarkus.funqy.knative.events.CloudEvent;
import io.quarkus.funqy.knative.events.CloudEventBuilder;

public class Function {

    @Funq
    public void function(CloudEvent<Bitmine> input) {
        Bitmine bitmine = input.data();
        bitmine.setStatus("processed");

        System.out.println(input);
    }

}
