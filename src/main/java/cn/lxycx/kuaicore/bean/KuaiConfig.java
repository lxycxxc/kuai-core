package cn.lxycx.kuaicore.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "kuai")
public class KuaiConfig {
    private Manage manage;
    private List<Classify> classifyList;
    private List<KuaiRegx> regxList;
    private String fileStoresRootPath;
    private Map<String, FileStoreConf> fileStores;
    private List<Module> modules;
}
