package ccbs.conf.base;

import ccbs.model.base.AddressVo;
import ccbs.model.base.ApiSchemaUtils;
import ccbs.model.base.SampleVo;
import ccbs.model.base.media.PairSchema;
import ccbs.model.base.media.SchemaType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
@ComponentScan(basePackages = {"org.springdoc"})
@OpenAPIDefinition(info = @Info(title = "CHT-RPT-API", version = "v1.0"))
public class OpenApiConfig {
    /**
     * 為地址產生不同的 Schema
     *
     * @return
     */
    @Bean
    public OpenAPI customOpenAPI() {
        @SuppressWarnings("rawtypes")
        Schema mapSchema = ApiSchemaUtils.mapSchema(
                // 第一層
                new PairSchema(SchemaType.Object, "prop1", SampleVo.class, null),
                new PairSchema(SchemaType.Array, "prop2", SampleVo.class, null),
                new PairSchema(SchemaType.Map, "prop3", null, new PairSchema[]{
                        // 第二層
                        new PairSchema(SchemaType.Object, "prop1", SampleVo.class, null),
                        new PairSchema(SchemaType.Array, "prop2", SampleVo.class, null),
                        new PairSchema(SchemaType.Map, "prop3", null, new PairSchema[]{
                                // 第三層
                                new PairSchema(SchemaType.Object, "prop1", SampleVo.class, null),
                                new PairSchema(SchemaType.Array, "prop2", SampleVo.class, null)})
                }));

        return new OpenAPI().components(new Components()
                .addSchemas("InstallAddress", ApiSchemaUtils.schema(AddressVo.class, "設置地址"))
                .addSchemas("InstallParcel", ApiSchemaUtils.schema(AddressVo.class, "設置地號"))
                .addSchemas("OwnerAddress", ApiSchemaUtils.schema(AddressVo.class, "負責人地址"))
                .addSchemas("ContactAddress", ApiSchemaUtils.schema(AddressVo.class, "聯絡人地址"))
                .addResponses("mySampleEntity", new io.swagger.v3.oas.models.responses.ApiResponse().description("This specific map for SampleController")
                        .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                                new io.swagger.v3.oas.models.media.MediaType().schema(mapSchema))
                        ))
        );
    }

}
