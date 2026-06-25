package sube.interviews.mareoenvios.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import sube.interviews.mareoenvios.enums.ShippingState;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class ShippingStateConverter implements AttributeConverter<ShippingState, String> {

    @Override
    public String convertToDatabaseColumn(ShippingState state) {
        if (state == null) {
            return null;
        }
        return state.getDescription();
    }

    @Override
    public ShippingState convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return Stream.of(ShippingState.values())
                .filter(s -> s.getDescription().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
