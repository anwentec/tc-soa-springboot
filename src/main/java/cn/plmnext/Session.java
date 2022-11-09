package cn.plmnext;

import com.teamcenter.soa.client.model.ModelObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.soa.client.Connection;

@Component
public class Session {

    @Autowired
    private TcConfig config;

    private Connection connection;
    private ModelObject user;

    @Bean
    public void init() {
        if(null == connection)
        {
            String type = config.getType();
            String host = config.getHost();
            String local = config.getLocal();

            int port = config.getPort();
            String name = config.getName();

            AppXSession session = new AppXSession(type, host, port, name,local);
            connection = AppXSession.getConnection();

            user = session.login(config.getUsername(), config.getPassword(), "SoaAppX");
        }

    }

    public ModelObject getUser() {
        return user;
    }


    public Connection getConnection() {
        return connection;
    }



}

