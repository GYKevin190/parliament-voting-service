package hu.gyanikevin.szavazas.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonStrukturaValidacio() {
        return builder -> {
            builder.featuresToDisable(MapperFeature.ALLOW_COERCION_OF_SCALARS, DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
            builder.postConfigurer(mapper -> {
                mapper.coercionConfigFor(LogicalType.Textual).setCoercion(CoercionInputShape.Integer, CoercionAction.Fail).setCoercion(CoercionInputShape.Float, CoercionAction.Fail).setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail);
                mapper.coercionConfigFor(LogicalType.DateTime).setCoercion(CoercionInputShape.Integer, CoercionAction.Fail).setCoercion(CoercionInputShape.Float, CoercionAction.Fail);
            });
        };
    }
}
