package uk.co.automatictester.lightning.standalone.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.automatictester.lightning.core.facades.LightningCoreLocalFacade;

@Configuration
public class LightningCoreBeanFactory {

    @Bean
    public LightningCoreLocalFacade lightningCoreLocalFacade() {
        return new LightningCoreLocalFacade();
    }
}
