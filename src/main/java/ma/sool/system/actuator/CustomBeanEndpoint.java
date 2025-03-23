package ma.sool.system.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "custom-beans")
public class CustomBeanEndpoint {
    private final ApplicationContext applicationContext;

    public CustomBeanEndpoint(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @ReadOperation
    public int beanCount() {
        return applicationContext.getBeanDefinitionCount();
    }
}
