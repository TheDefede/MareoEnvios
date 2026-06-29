package sube.interviews.mareoenvios.unit.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sube.interviews.mareoenvios.converter.ShippingStateConverter;
import sube.interviews.mareoenvios.enums.ShippingState;
import static org.junit.jupiter.api.Assertions.*;

public class ShippingStateConverterTest {

    private ShippingStateConverter converter;

    @BeforeEach
    void setUp() {
        converter = new ShippingStateConverter();
    }

    @Test
    void testConvertToDatabaseColumn_WithValidState_ReturnsDescription() {
        String dbValue = converter.convertToDatabaseColumn(ShippingState.ENTREGADO_CORREO);
        assertEquals("Entregado al correo", dbValue);
    }

    @Test
    void testConvertToDatabaseColumn_WithNullState_ReturnsNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void testConvertToEntityAttribute_WithValidDescription_ReturnsEnum() {
        ShippingState state = converter.convertToEntityAttribute("Entregado al correo");
        assertEquals(ShippingState.ENTREGADO_CORREO, state);
    }

    @Test
    void testConvertToEntityAttribute_WithNullDescription_ReturnsNull() {
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    void testConvertToEntityAttribute_WithInvalidDescription_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            converter.convertToEntityAttribute("Estado Fantasma");
        });
    }
}
