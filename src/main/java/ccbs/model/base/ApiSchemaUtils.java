package ccbs.model.base;

import ccbs.model.base.media.PairSchema;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.*;
import ccbs.model.base.media.SchemaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
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
        final MapSchema schema = new MapSchema();
        Map<String, Schema> properties = new HashMap<>();
        
        Arrays.stream(pairs).forEach(p -> {
            Schema propertySchema;
            if (SchemaType.Object.equals(p.getSchemaType())) {
                propertySchema = schema(p.getAClass());
            } else if (SchemaType.Array.equals(p.getSchemaType())) {
                propertySchema = arraySchema(p.getAClass());
            } else if (SchemaType.Map.equals(p.getSchemaType())) {
                propertySchema = mapSchema(p.getPairSchemas());
            } else if (SchemaType.Integer.equals(p.getSchemaType())) {
                propertySchema = new IntegerSchema();
            } else if (SchemaType.String.equals(p.getSchemaType())) {
                propertySchema = new StringSchema();
            } else {
                propertySchema = new ObjectSchema();
            }
            properties.put(p.getKey(), propertySchema);
        });
        
        schema.setProperties(properties);
        return schema;
    }

}
