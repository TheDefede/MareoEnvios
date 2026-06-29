package sube.interviews.mareoenvios.unit.enums;

import org.junit.jupiter.api.Test;
import sube.interviews.mareoenvios.enums.ShippingState;
import static org.junit.jupiter.api.Assertions.*;

public class ShippingStateTest {

    @Test
    void testCanTransitionTo_FromInicial() {
        ShippingState state = ShippingState.INICIAL;

        assertTrue(state.canTransitionTo(ShippingState.ENTREGADO_CORREO));
        assertTrue(state.canTransitionTo(ShippingState.CANCELADO));

        assertFalse(state.canTransitionTo(ShippingState.INICIAL));
        assertFalse(state.canTransitionTo(ShippingState.EN_CAMINO));
        assertFalse(state.canTransitionTo(ShippingState.ENTREGADO));
    }

    @Test
    void testCanTransitionTo_FromEntregadoCorreo() {
        ShippingState state = ShippingState.ENTREGADO_CORREO;

        assertTrue(state.canTransitionTo(ShippingState.EN_CAMINO));
        assertTrue(state.canTransitionTo(ShippingState.CANCELADO));

        assertFalse(state.canTransitionTo(ShippingState.INICIAL));
        assertFalse(state.canTransitionTo(ShippingState.ENTREGADO_CORREO));
        assertFalse(state.canTransitionTo(ShippingState.ENTREGADO));
    }

    @Test
    void testCanTransitionTo_FromEnCamino() {
        ShippingState state = ShippingState.EN_CAMINO;

        assertTrue(state.canTransitionTo(ShippingState.ENTREGADO));

        assertFalse(state.canTransitionTo(ShippingState.INICIAL));
        assertFalse(state.canTransitionTo(ShippingState.ENTREGADO_CORREO));
        assertFalse(state.canTransitionTo(ShippingState.EN_CAMINO));
        assertFalse(state.canTransitionTo(ShippingState.CANCELADO));
    }

    @Test
    void testCanTransitionTo_FromFinalStates_ShouldAlwaysReturnFalse() {

        ShippingState entregado = ShippingState.ENTREGADO;
        for (ShippingState target : ShippingState.values()) {
            assertFalse(entregado.canTransitionTo(target), "Entregado no debería poder transicionar a " + target);
        }

        ShippingState cancelado = ShippingState.CANCELADO;
        for (ShippingState target : ShippingState.values()) {
            assertFalse(cancelado.canTransitionTo(target), "Cancelado no debería poder transicionar a " + target);
        }
    }
}
