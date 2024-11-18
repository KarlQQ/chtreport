package ccbs.model.base;

import ccbs.model.base.media.PairSchema;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.*;
import ccbs.model.base.media.SchemaType;

import java.util.Arrays;

public class ApiSchemaUtils {
    /**
     * Convert class to Schema
     *
     * @param clazz
     * @return
     */
    public static Schema schema(Class clazz) {
        return schema(clazz, null);
    }

    /**
     * Convert class to Schema
     *
     * @param clazz
     * @param description
     * @return
     */
    public static Schema schema(Class clazz, String description) {
        return ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(clazz).resolveAsRef(false)).schema.description(description);
    }


    /**
     * Convert List to Schema
     *
     * @param clazz
     * @return
     */
    public static Schema arraySchema(Class clazz) {
        return new ArraySchema().items(schema(clazz));
    }

    /**
     * Convert Map to Schema
     *
     * @param pairs
     * @return
     */
    public static Schema mapSchema(PairSchema...pairs) {
        final Schema schema = new MapSchema();
        Arrays.stream(pairs).forEach(p -> {
            if (SchemaType.Object.equals(p.getSchemaType())) {
                schema.addProperties(p.getKey(), schema(p.getAClass()));
            } else if (SchemaType.Array.equals(p.getSchemaType())) {
                schema.addProperties(p.getKey(), arraySchema(p.getAClass()));
            } else if (SchemaType.Map.equals(p.getSchemaType())) {
                schema.addProperties(p.getKey(), mapSchema(p.getPairSchemas()));
            } else if (SchemaType.Integer.equals(p.getSchemaType())) {
                schema.addProperties(p.getKey(), new IntegerSchema());
            } else if (SchemaType.String.equals(p.getSchemaType())) {
                schema.addProperties(p.getKey(), new StringSchema());
            }
        });
        return schema;
    }

}
