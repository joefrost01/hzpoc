package com.lbg.mar.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapStore;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;
import java.util.Set;


@Component
public class PersistentFileCache {


    @Bean
    public IMap<String, String> hzMap() {
        String mapName = "TestMap";
        FileStore fileStore = new FileStore();

        MapStoreConfig mapStoreConfig = new MapStoreConfig();
        mapStoreConfig.setImplementation(fileStore);
        mapStoreConfig.setWriteDelaySeconds(0);

        XmlConfigBuilder configBuilder = new XmlConfigBuilder();
        Config config = configBuilder.build();
        MapConfig mapConfig = config.getMapConfig(mapName);
        mapConfig.setMapStoreConfig(mapStoreConfig);
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);

        return hz.getMap(mapName);
    }

    private static class FileStore implements MapStore<String, String> {

        Logger log = LoggerFactory.getLogger("cache");

        String FileStoreLocation = "FileStore/";

        @Override
        public void store(String key, String value) {
            log.info("FileStore - storing key: " + key);
            File file = new File(FileStoreLocation + key);
            try {
                Files.write(file.toPath(), value.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void storeAll(Map<String, String> map) {
            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                String key = entry.getKey();
                String value = entry.getValue();
                File file = new File(FileStoreLocation + key);
                try {
                    Files.write(file.toPath(), value.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void delete(String key) {
        }

        @Override
        public void deleteAll(Collection<String> keys) {
        }

        @Override
        public String load(String key) {
            log.info("FileStore - loading value for key: " + key);
            File file = new File(FileStoreLocation + key);
            try {
                return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.warn("Could not find " + key);
            }
            return null;
        }

        @Override
        public Map<String, String> loadAll(Collection<String> keys) {
            return null;
        }

        @Override
        public Set<String> loadAllKeys() {
            return null;
        }
    }
}
