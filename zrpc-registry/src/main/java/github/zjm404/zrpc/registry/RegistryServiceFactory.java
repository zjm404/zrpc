package github.zjm404.zrpc.registry;

import github.zjm404.zrpc.core.RegistryService;

/**
 * @author zjm
 * @date 2021/2/4
 */
public class RegistryServiceFactory {
    /**
     * TODO:根据条件选择不同的 registry
     * @param registryType
     * @param registryAddr
     * @return
     */
    public static RegistryService getRegistryService(String registryType,String registryAddr) throws Exception {
        return new ZooKeeperRegistryService(registryAddr);
    }
}
