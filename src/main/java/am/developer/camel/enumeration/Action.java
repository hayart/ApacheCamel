/**
 * Copyright (c) Worldline 2019 - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 **/
package am.developer.camel.enumeration;

import lombok.Getter;

@Getter
public enum Action {

    TERMINAL_BOOK("Réservation d'un DAB"),
    TERMINAL_RELEASE("Libération d'un DAB"),
    TERMINAL_UPDATE("MAJ d'un DAB"),
    TERMINAL_CREATION("Création d'un DAB"),
    CMD_TERMINAL_CLOSE("Mise hors service d'un DAB"),
    CMD_TERMINAL_OPEN("Mise en service d'un DAB"),
    CMD_TERMINAL_DISCONNECT("Deconnexion CV d'un DAB"),
    CMD_TERMINAL_ERASE_KEY("Effacement des cles d'un DAB (NAC) "),
    CMD_PUT_QUARANTINE_ON("Mise en quarantaine d'un DAB "),
    CMD_PUT_QUARANTINE_OFF("Sortie de quarantaine d'un DAB"),
    CMD_EXIT_YELLOW_LIST("Sortie de liste jaune"),
    CMD_IS_IN_YELLOW_LIST("Présence en liste jaune"),
    RJ_HOLDER_ACTIVITY("Activite d'un porteur en RJ"),
    RJ_OPERATIONS_LIST("Listage des opérations en RJ"),
    TERMINAL_CREATION_INIT("Init Création d'un DAB");

    /**
     * Libelle du Type.
     */
    private final String label;

    Action(String label) {
        this.label = label;
    }

}
