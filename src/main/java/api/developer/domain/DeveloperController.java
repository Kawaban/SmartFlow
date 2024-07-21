package api.developer.domain;

import api.developer.dto.DeveloperRequest;
import api.developer.dto.DeveloperResponse;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public record DeveloperController(DeveloperService developerService) {
    @GetMapping("/{userId}")
    public DeveloperResponse getDevelopers(@PathVariable UUID userId) {
        return developerService.getDevelopers(userId);
    }

    @PostMapping
    public void addDeveloper (@RequestBody DeveloperRequest developerRequest) {
        developerService.addDeveloper(developerRequest);
    }
}
