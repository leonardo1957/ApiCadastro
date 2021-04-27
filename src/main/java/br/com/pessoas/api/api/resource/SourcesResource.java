package br.com.pessoas.api.resource;
import java.util.HashMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(tags = "Source")
@RestController
public class SourcesResource {
	
	private String urlCodigoFonteBackend = "https://github.com/leonardo1957/api-backend-cadastro-pessoas";
	private String urlDockerHubBackend = "https://hub.docker.com/repository/docker/leonardo1957/cadastro_pessoas";
	

	@ApiOperation("git")
	@GetMapping("/source")
	public ResponseEntity<HashMap<String, String>> source() {
		HashMap<String, String> map = new HashMap<>();
		map.put("urlBackend",urlCodigoFonteBackend);
		map.put("urlBackendDocker", urlDockerHubBackend);
		return ResponseEntity.ok(map);
	}
	
	

}
