package cn.plmnext;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "tc")
public class TcConfig {
	private String type;
	private String host;
	private int port;
	private String name;
	private String username;
	private String password;
	private String local;
}
