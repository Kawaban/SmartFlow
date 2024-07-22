package api.developer.domain;

import api.developer.dto.DeveloperRequest;
import api.developer.dto.DeveloperResponse;
import api.infrastructure.exception.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/developers")
public record DeveloperController(DeveloperService developerService) {
    @GetMapping("/{developerId}")
    public DeveloperResponse getDevelopers(@PathVariable UUID developerId) throws EntityNotFoundException {
        return developerService.getDevelopers(developerId);
    }

    @PostMapping
    public void addDeveloper(@RequestBody DeveloperRequest developerRequest) throws IllegalArgumentException {
        developerService.addDeveloper(developerRequest);
    }
}
