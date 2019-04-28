package co.edu.uniandes.xrepo.service.util;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncDelegator {

    @Async
    public void async(Runnable asyncRoutine){
        asyncRoutine.run();
    }
}
