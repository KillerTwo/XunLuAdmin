package org.wm.jackson2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 功能描述：<功能描述>
 *     Collection<? extends GrantedAuthority> 序列化器
 *
 * @author dove 
 * @date 2023/08/08 00:17
 * @since 1.0
**/
public class GrantedAuthoritySerializer extends StdSerializer<Collection<? extends GrantedAuthority>> {

    protected GrantedAuthoritySerializer(Class<Collection<? extends GrantedAuthority>> t) {
        super(t);
    }

    protected GrantedAuthoritySerializer(JavaType type) {
        super(type);
    }

    protected GrantedAuthoritySerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected GrantedAuthoritySerializer(StdSerializer<?> src) {
        super(src);
    }

    @Override
    public void serialize(Collection<? extends GrantedAuthority> authorities,
                          JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<String> authorityStrings = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            authorityStrings.add(authority.getAuthority());
        }
        jsonGenerator.writeObject(authorityStrings);
    }
}
