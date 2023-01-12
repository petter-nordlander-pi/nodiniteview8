package se.martinservera.integration.nodiniteviewerj8;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LogReceiver {

    static Logger LOG = LoggerFactory.getLogger(LogReceiver.class);

    @JmsListener(destination = "Nodinite.LogAgent.PickupService")
    public void receiveMessage(String body) {
        LOG.debug("Event <" + body + ">");
        try {
            LOG.info("\nNODINITE EVENT -- \n{}", asYaml(body));
        } catch (IOException e) {
            LOG.error("Error parsing msg");
        }
    }

    String asYaml(String jsonString) throws JsonProcessingException, IOException {
        // parse JSON
        JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
        ((ObjectNode)jsonNodeTree).remove("Body");
        // save it as YAML
        String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);
        return jsonAsYaml;
    }
}
