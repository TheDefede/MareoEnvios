package sube.interviews.mareoenvios.enums;

import lombok.Getter;

@Getter
public enum ShippingState {
    INICIAL("Inicial"),
    ENTREGADO_CORREO("Entregado al correo"),
    EN_CAMINO("En camino"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    private final String description;

    ShippingState(String description) {
        this.description = description;
    }

    public boolean canTransitionTo(ShippingState nextState) {
        return switch (this) {

            case INICIAL -> nextState == ENTREGADO_CORREO || nextState == CANCELADO;
            case ENTREGADO_CORREO -> nextState == EN_CAMINO || nextState == CANCELADO;
            case EN_CAMINO -> nextState == ENTREGADO;
            case ENTREGADO, CANCELADO -> false;
        };
    }
}
