package ccbs.model.base.media;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PairSchema {
    private SchemaType schemaType;
    private String key;
    private Class aClass;
    private PairSchema[] pairSchemas;
}

