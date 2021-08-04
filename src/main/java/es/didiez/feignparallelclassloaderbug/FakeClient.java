package es.didiez.feignparallelclassloaderbug;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author didiez
 */
@FeignClient(name = "fake-client")
interface FakeClient {
    
    @GetMapping("albums/{id}")
    Album getAlbum(@PathVariable("id") Long id);
    
    @Data
    static class Album {
        private Long id;
        private Long userId;
        private String title;
    }
}
