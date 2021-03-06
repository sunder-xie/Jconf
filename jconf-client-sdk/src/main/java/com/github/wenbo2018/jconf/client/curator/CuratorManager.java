package com.github.wenbo2018.jconf.client.curator;

import com.github.wenbo2018.jconf.client.constants.Constants;
import com.github.wenbo2018.jconf.common.config.ConfigManager;
import com.github.wenbo2018.jconf.common.config.ConfigManagerLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by shenwenbo on 2017/4/16.
 */
public class CuratorManager {

    private static Logger logger = LoggerFactory.getLogger(CuratorManager.class);
    private static volatile boolean isInit = false;
    private static CuratorManager instance = new CuratorManager();
    private static ConcurrentMap<String, CuratorClient> curatorClientCache = new ConcurrentHashMap<>();
    private static ConfigManager configManager;
    private static List<CuratorClient> clientList = new ArrayList<>();

    public static CuratorManager getInstance() {
        if (!isInit) {
            synchronized (CuratorManager.class) {
                if (!isInit) {
                    instance.init();
                    isInit = true;
                }
            }
        }
        return instance;
    }

    private void init() {
        this.configManager = ConfigManagerLoader.getConfigManager();
        String addressSplits = configManager.getStringValue(Constants.ZOOKEEPER_REGISTER_ADDRESS);
        String[] addressArray = addressSplits.trim().split(",");
        if (addressArray.length <= 0) {
            logger.error("zk curator address is not found");
        } else {
            for (int i = 0; i < addressArray.length; i++) {
                String address = addressArray[i];
                CuratorClient curatorClient = createCuratorClient(address);
                curatorClientCache.put(address, curatorClient);
                clientList.add(curatorClient);
            }
        }
    }

    public CuratorClient createCuratorClient(String address) {
        CuratorClient curatorClient = null;
        try {
            curatorClient = new CuratorClient(address);
            logger.info("zk curator success create{}:", address);
        } catch (InterruptedException e) {
            logger.error("create curator curator error", e);
        }
        return curatorClient;
    }

    public CuratorClient getCuratorClient() {
        CuratorClient curatorClient = null;
        if (clientList.size() <= 0) {
            logger.error("not found one curator curator");
        }
        return clientList.get(0);
    }
}
