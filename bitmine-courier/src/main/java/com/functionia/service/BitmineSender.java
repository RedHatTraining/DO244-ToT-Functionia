package com.functionia.service;

import io.smallrye.reactive.messaging.ce.OutgoingCloudEventMetadata;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.functionia.model.Bitmine;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Path("/")
public class BitmineSender {

    final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    OffsetDateTime offsetDateTime = OffsetDateTime.now();
    ZonedDateTime zonedDateTime = offsetDateTime.toZonedDateTime();

    @Inject
    @Channel("bitmines")
    private Emitter<Bitmine> emitter;

    @GET
    @Path("/{messagestosend}")
    public String sendNMessages(@PathParam("messagestosend") int messagestosend) {
        System.out.println("messagestosend: " + messagestosend);
        for (int i = 0; i < messagestosend; i++) {
            Bitmine bitmine = new Bitmine();
            emitter.send(Message.of(bitmine)
                    .addMetadata(OutgoingCloudEventMetadata.builder()
                            .withId("id-" + i)
                            .withTimestamp(zonedDateTime)
                            .withType(getRandomEventType())
                            .build()));
        }
        String output = "Sent " + messagestosend + " messages";
        return output;
    }

    private static final List<String> VALUES = Arrays.asList(
        "noders",
        "quarkers"
    );

    public static String getRandomEventType() {
        Random random = new Random();
        int index = random.nextInt(VALUES.size());
        return VALUES.get(index);
    }
}
