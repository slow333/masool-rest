package ma.sool.system.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class UsableDiskHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        File path = new File("."); // 가용 디스크 크기 , 현재 폴더
        long diskUsableInBytes = path.getUsableSpace();
        boolean isHealth = diskUsableInBytes >= 10*1024*1024;
        Status status = isHealth ? Status.UP : Status.DOWN;
        return Health
                .status(status)
                .withDetail("usable disk", diskUsableInBytes)
                .withDetail("threshold", 10*1024*1024)
                .build();
    }
}
