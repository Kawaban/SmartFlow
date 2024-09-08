package api.developer.domain;

import api.developer.dto.DeveloperRequest;
import api.developer.dto.DeveloperResponse;
import api.infrastructure.exception.EntityNotFoundException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/developers")
class DeveloperController {
    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @GetMapping("/{developerId}")
    @PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.username == authentication.principal.username")
    public DeveloperResponse getDevelopers(@PathVariable UUID developerId) throws EntityNotFoundException {
        return developerService.getDevelopers(developerId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addDeveloper(@RequestBody DeveloperRequest developerRequest) throws IllegalArgumentException {
        developerService.addDeveloper(developerRequest);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<DeveloperResponse> getAllDevelopers() {
        return developerService.getAllDevelopers();
    }
}
