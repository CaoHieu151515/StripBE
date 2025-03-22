package strip.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.apidoc.customizer.JHipsterOpenApiCustomizer;

@Configuration
@Profile(JHipsterConstants.SPRING_PROFILE_API_DOCS)
public class OpenApiConfiguration {

    public static final String API_FIRST_PACKAGE = "strip.web.api";

    @Bean
    @ConditionalOnMissingBean(name = "apiFirstGroupedOpenAPI")
    public GroupedOpenApi apiFirstGroupedOpenAPI(
        JHipsterOpenApiCustomizer jhipsterOpenApiCustomizer,
        JHipsterProperties jHipsterProperties
    ) {
        JHipsterProperties.ApiDocs properties = jHipsterProperties.getApiDocs();
        return GroupedOpenApi.builder()
            .group("openapi")
            .addOpenApiCustomizer(jhipsterOpenApiCustomizer)
            .packagesToScan(API_FIRST_PACKAGE)
            .pathsToMatch(properties.getDefaultIncludePattern())
            .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("S-Trip API").version("v1").description("API documentation for S-Trip"))
            .addServersItem(new Server().url("https://stripbe-production.up.railway.app")); // 👈 Đặt URL đúng của
        // bạn ở đây
    }
}
