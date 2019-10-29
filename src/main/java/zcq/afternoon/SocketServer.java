package zcq.afternoon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import zcq.afternoon.service.ConnectService;
import zcq.afternoon.util.ThreadPoolUtils;

/**
 * @author zhengchuqin
 * @version 1.0
 * @since 2019/10/10
 */
@Service
public class SocketServer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ConnectService connectService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                //处理客户端连接线程
                try {
                    connectService.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }, "monitor-client");
    }

}
